"""
🟦 CAPA PRESENTACIÓN - Schemas
Modelos Pydantic para request/response
"""
from pydantic import BaseModel, Field, field_validator
from typing import Optional, Dict, Any, List
from decimal import Decimal


class SearchRequest(BaseModel):
    """
    Request para búsqueda de refacciones
    """
    nombre_pieza: str = Field(..., min_length=2, max_length=200, description="Nombre de la pieza (ej: 'bujia NGK')")
    marca: Optional[str] = Field(None, max_length=100, description="Marca del vehículo (ej: 'Honda')")
    modelo: Optional[str] = Field(None, max_length=100, description="Modelo del vehículo (ej: 'Civic')")
    anio: Optional[int] = Field(None, ge=1900, le=2030, description="Año del vehículo")
    version: Optional[str] = Field(None, max_length=100, description="Versión del modelo (ej: 'EX')")
    mecanica: Optional[str] = Field(None, max_length=100, description="Tipo de mecánica (ej: 'Manual')")
    
    @field_validator('nombre_pieza')
    @classmethod
    def validate_nombre_pieza(cls, v: str) -> str:
        """Validar que nombre_pieza no esté vacío"""
        if not v or v.strip() == "":
            raise ValueError("nombre_pieza no puede estar vacío")
        return v.strip()
    
    class Config:
        json_schema_extra = {
            "example": {
                "nombre_pieza": "bujia NGK",
                "marca": "Honda",
                "modelo": "Civic",
                "anio": 2015,
                "version": "EX",
                "mecanica": "Manual"
            }
        }


class ProductOfferSchema(BaseModel):
    """
    Esquema de producto encontrado
    """
    titulo: str = Field(..., description="Título del producto")
    precio: Decimal = Field(..., ge=0, description="Precio en MXN")
    url: str = Field(..., description="URL del producto")
    fuente: str = Field(..., description="Fuente: MERCADO_LIBRE, AMAZON, AUTOZONE")
    metadata: Dict[str, Any] = Field(default_factory=dict, description="Metadata adicional (shipping, rating, etc.)")
    
    class Config:
        json_schema_extra = {
            "example": {
                "titulo": "Bujia NGK Original Honda Civic 2015",
                "precio": 120.00,
                "url": "https://mercadolibre.com.mx/...",
                "fuente": "MERCADO_LIBRE",
                "metadata": {
                    "shipping": "Gratis",
                    "rating": 4.8,
                    "vendidos": 150
                }
            }
        }


class SearchResponse(BaseModel):
    """
    Response con resultados de búsqueda
    """
    success: bool = Field(..., description="Indica si la búsqueda fue exitosa")
    total_results: int = Field(..., ge=0, description="Número total de resultados")
    search_params: Dict[str, Any] = Field(..., description="Parámetros usados en la búsqueda")
    results: List[ProductOfferSchema] = Field(default_factory=list, description="Lista de productos encontrados")
    
    class Config:
        json_schema_extra = {
            "example": {
                "success": True,
                "total_results": 15,
                "search_params": {
                    "nombre_pieza": "bujia NGK",
                    "marca": "Honda",
                    "modelo": "Civic",
                    "anio": 2015
                },
                "results": [
                    {
                        "titulo": "Bujia NGK Original Honda Civic 2015",
                        "precio": 120.00,
                        "url": "https://mercadolibre.com.mx/...",
                        "fuente": "MERCADO_LIBRE",
                        "metadata": {
                            "shipping": "Gratis",
                            "rating": 4.8
                        }
                    }
                ]
            }
        }


class IngestRequest(BaseModel):
    """
    Request para ingestión manual de productos.

    Si `cache_id` está presente, los productos se asociarán a ese cache.
    Si no, se creará una nueva entrada en `cache_scrapy` automáticamente.
    """
    cache_id: Optional[str] = Field(None, description="ID opcional del cache al que asociar los productos")
    products: List[ProductOfferSchema] = Field(..., description="Lista de productos a guardar")

    class Config:
        json_schema_extra = {
            "example": {
                "cache_id": None,
                "products": [
                    {
                        "titulo": "Bujia NGK Original Honda Civic 2015",
                        "precio": 120.00,
                        "url": "https://mercadolibre.com.mx/...",
                        "fuente": "MERCADO_LIBRE",
                        "metadata": {"shipping": "Gratis"}
                    }
                ]
            }
        }
