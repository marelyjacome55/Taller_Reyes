"""
🟨 CAPA APLICACIÓN - SearchService
Orquesta el proceso completo de búsqueda
"""
from typing import List, Optional
from ..domain.search_params import SearchParams
from ..domain.product_offer import ProductOffer
from ..domain.provider_enum import Provider
from ..domain.errors import NoResultsFoundError, InvalidSearchError
from .cache_service import CacheService
from .ranking_service import RankingService
from ..infrastructure.runners.scrapy_runner import ScrapyRunner


class SearchService:
    """
    Servicio de aplicación que orquesta la búsqueda de productos
    
    Flujo:
    1. Valida parámetros
    2. Consulta cache
    3. Si no hay cache: ejecuta spiders
    4. Rankea resultados
    5. Guarda en cache
    6. Retorna resultados
    """
    
    def __init__(
        self,
        cache_service: CacheService,
        ranking_service: RankingService,
        scrapy_runner: ScrapyRunner
    ):
        self.cache_service = cache_service
        self.ranking_service = ranking_service
        self.scrapy_runner = scrapy_runner
    
    async def search(
        self,
        nombre_pieza: str,
        marca: Optional[str] = None,
        modelo: Optional[str] = None,
        anio: Optional[int] = None,
        version: Optional[str] = None,
        mecanica: Optional[str] = None
    ) -> List[ProductOffer]:
        """
        Busca productos en todas las plataformas
        
        Args:
            nombre_pieza: Nombre de la pieza (obligatorio)
            marca, modelo, anio, version, mecanica: Filtros opcionales
        
        Returns:
            Lista de ProductOffer ordenados por precio
        
        Raises:
            InvalidSearchError: Si los parámetros son inválidos
            NoResultsFoundError: Si no se encuentran productos
        """
        # 1. Crear SearchParams (valida en constructor)
        try:
            search_params = SearchParams(
                nombre_pieza=nombre_pieza,
                marca=marca,
                modelo=modelo,
                anio=anio,
                version=version,
                mecanica=mecanica
            )
        except ValueError as e:
            raise InvalidSearchError(str(e))
        
        # 2. Consultar cache
        cached_results = await self.cache_service.get_cached_results(search_params)
        if cached_results:
            print(f"✅ Cache hit para: {search_params}")
            return cached_results
        
        print(f"⚠️  Cache miss para: {search_params} - Ejecutando scraping...")
        
        # 3. Ejecutar scraping en todos los proveedores
        all_products = await self._scrape_all_providers(search_params)
        
        # 4. Validar que haya resultados
        if not all_products:
            raise NoResultsFoundError(
                f"No se encontraron productos para: {search_params.to_query_string()}"
            )
        
        # 5. Rankear resultados (ordenar por precio, eliminar duplicados)
        ranked_products = self.ranking_service.rank_by_price(all_products)
        
        # 6. Guardar en cache
        await self.cache_service.save_results(search_params, ranked_products)
        
        print(f"✅ Búsqueda completada: {len(ranked_products)} productos encontrados")
        return ranked_products
    
    async def _scrape_all_providers(
        self,
        search_params: SearchParams
    ) -> List[ProductOffer]:
        """
        Ejecuta spiders para todos los proveedores en paralelo
        
        Returns:
            Lista combinada de todos los productos
        """
        all_products = []
        
        # TODO: Ejecutar en paralelo con asyncio.gather()
        for provider in Provider.get_all():
            try:
                products = await self.scrapy_runner.run_spider(
                    provider=provider,
                    search_params=search_params
                )
                all_products.extend(products)
                print(f"  ✅ {provider.value}: {len(products)} productos")
            except Exception as e:
                # No fallar toda la búsqueda si un proveedor falla
                print(f"  ❌ {provider.value}: Error - {e}")
                continue
        
        return all_products
