"""
CAPA PRESENTACIÓN - Routers
Definición de endpoints HTTP
"""

from fastapi import APIRouter, Depends, HTTPException
from typing import List
from .schemas import SearchRequest, SearchResponse, ProductOfferSchema, IngestRequest
from .dependencies import get_search_service, get_cache_repository, get_results_repository
from ..application.search_service import SearchService
from ..domain.errors import NoResultsFoundError, InvalidSearchError
from ..infrastructure.db.cache_repository import CacheRepository
from ..infrastructure.db.results_repository import ResultsRepository
from ..domain.product_offer import ProductOffer
from ..domain.provider_enum import Provider
import uuid
from datetime import datetime, timedelta
import os

"""
APIRouter	Permite agrupar endpoints /scrapy/*
Depends	Inyección de dependencias (SearchService, repos, etc.)
HTTPException	Manejo de errores HTTP
SearchRequest	Esquema de entrada (validación de lo que manda Java)
SearchResponse	Esquema de salida (lo que React recibe)
ProductOfferSchema	Modelo para un producto individual
get_search_service()	Factory que construye el motor de scraping
InvalidSearchError, NoResultsFoundError	Errores de dominio controlados
CacheRepository, ResultsRepository	Acceso a PostgreSQL
ProductOffer	Entidad de dominio
Provider	Enum para identificar proveedor
uuid	Para generar IDs únicos
datetime/timedelta	Para manejar expiración de caché
os	Para leer variables de entorno como TTL
"""
router = APIRouter()


@router.post("/buscar", response_model=SearchResponse)
async def buscar_refacciones(
    request: SearchRequest,
    search_service: SearchService = Depends(get_search_service)
):
    """
    Busca refacciones en Mercado Libre, Amazon y Autozone
    
    Args:
        request: Parámetros de búsqueda (nombre_pieza, marca, modelo, año, etc.)
        search_service: Servicio de búsqueda (inyectado)
    
    Returns:
        SearchResponse con lista de productos ordenados por precio
    
    Raises:
        HTTPException 400: Parámetros inválidos
        HTTPException 404: No se encontraron resultados
        HTTPException 500: Error interno del servidor
    """
    try:
        # TODO: Ejecutar búsqueda usando SearchService
        productos = await search_service.search(
            nombre_pieza=request.nombre_pieza,
            marca=request.marca,
            modelo=request.modelo,
            anio=request.anio,
            version=request.version,
            mecanica=request.mecanica
        )
        
        # Convertir ProductOffer a ProductOfferSchema
        results = [
            ProductOfferSchema(
                titulo=p.titulo,
                precio=p.precio,
                url=p.url,
                fuente=p.fuente.value,
                metadata=p.metadata
            )
            for p in productos
        ]
        
        return SearchResponse(
            success=True,
            total_results=len(results),
            search_params={
                "nombre_pieza": request.nombre_pieza,
                "marca": request.marca,
                "modelo": request.modelo,
                "anio": request.anio
            },
            results=results
        )
        
    except InvalidSearchError as e:
        raise HTTPException(status_code=400, detail=str(e))
    
    except NoResultsFoundError as e:
        raise HTTPException(status_code=404, detail=str(e))
    
    except Exception as e:
        # Log error (implementar logging)
        print(f"❌ Error en búsqueda: {e}")
        raise HTTPException(status_code=500, detail="Error interno del servidor")


@router.get("/providers")
async def get_providers():
    """
    Retorna lista de proveedores disponibles
    """
    return {
        "providers": [
            {"id": "MERCADO_LIBRE", "name": "Mercado Libre"},
            {"id": "AMAZON", "name": "Amazon México"},
            {"id": "AUTOZONE", "name": "Autozone"}
        ]
    }


@router.post("/ingest")
async def ingest_products(
    request: IngestRequest,
    cache_repo: CacheRepository = Depends(get_cache_repository),
    results_repo: ResultsRepository = Depends(get_results_repository)
):
    """
    Endpoint para insertar manualmente productos en la base de datos.

    Body: IngestRequest { cache_id?: string, products: [ProductOfferSchema] }
    """
    try:
        # Si no se proporciona cache_id, creamos una nueva entrada en cache_scrapy
        cache_id = request.cache_id
        if not cache_id:
            # Generar search_hash aleatorio
            search_hash = str(uuid.uuid4())
            expires_at = datetime.utcnow() + timedelta(hours=int(os.getenv("CACHE_TTL_HOURS", "24")))
            data_json = {"products": [p.dict() for p in request.products], "count": len(request.products), "timestamp": datetime.utcnow().isoformat()}
            cache_id = await cache_repo.save(search_hash=search_hash, data_json=data_json, expires_at=expires_at)

        # Guardar cada producto en resultados_scrapy
        inserted = 0
        for p in request.products:
            # Convertir a entidad de dominio
            provider = Provider.from_string(p.fuente)
            product_entity = ProductOffer(
                titulo=p.titulo,
                precio=p.precio,
                url=p.url,
                fuente=provider,
                metadata=p.metadata,
                imagen_url=p.metadata.get("imagen_url") if p.metadata else None,
            )
            await results_repo.save(cache_id=cache_id, product=product_entity)
            inserted += 1

        return {"success": True, "cache_id": cache_id, "inserted": inserted}

    except Exception as e:
        print(f"❌ Error en ingest: {e}")
        raise HTTPException(status_code=500, detail="Error al insertar productos")
