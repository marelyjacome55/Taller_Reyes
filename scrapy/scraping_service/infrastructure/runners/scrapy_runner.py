"""
Capa de infraestructura - Scrapy Runner
Ejecuta spiders programaticamente
"""
from typing import List
from ...domain.search_params import SearchParams
from ...domain.product_offer import ProductOffer
from ...domain.provider_enum import Provider
from ..scraping.spider_factory import SpiderFactory


class ScrapyRunner:
    """
    Runner para ejecutar spiders desde codigo
    
    Permite ejecutar spiders sin usar la linea de comandos de Scrapy
    """
    
    async def run_spider(
        self,
        provider: Provider,
        search_params: SearchParams
    ) -> List[ProductOffer]:
        """
        Ejecuta un spider para un proveedor especifico
        
        Args:
            provider: Provider enum
            search_params: Parametros de busqueda
        
        Returns:
            Lista de ProductOffer encontrados
        """
        # Crear spider usando factory
        spider = SpiderFactory.create_spider(provider)
        
        # Ejecutar scraping
        products = await spider.scrape(search_params)
        
        return products
    
    async def run_all_spiders(
        self,
        search_params: SearchParams
    ) -> List[ProductOffer]:
        """
        Ejecuta todos los spiders en paralelo
        
        Args:
            search_params: Parametros de busqueda
        
        Returns:
            Lista combinada de todos los productos
        """
        all_products = []
        
        for provider in Provider.get_all():
            try:
                products = await self.run_spider(provider, search_params)
                all_products.extend(products)
            except Exception as e:
                print(f"Error en {provider.value}: {e}")
                continue
        
        return all_products
