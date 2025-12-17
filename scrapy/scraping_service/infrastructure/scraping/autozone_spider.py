"""
Capa de infraestructura - Autozone Spider
Spider especifico para Autozone Mexico
"""
from typing import List
from urllib.parse import quote_plus

from .base_spider import BaseSpider
from ...domain.product_offer import ProductOffer
from ...domain.search_params import SearchParams
from ...domain.provider_enum import Provider
from ...domain.errors import ScrapingError


class AutozoneSpider(BaseSpider):
    """
    Spider para Autozone Mexico
    
    URL base: https://www.autozone.com.mx
    """
    
    def __init__(self):
        super().__init__(Provider.AUTOZONE)
    
    def build_search_url(self, search_params: SearchParams) -> str:
        """
        Construye URL de busqueda para Autozone
        
        TODO: Verificar formato real de URL de Autozone
        """
        query = search_params.to_query_string()
        encoded_query = quote_plus(query)
        return f"{self.base_url}/search?q={encoded_query}"
    
    async def scrape(self, search_params: SearchParams) -> List[ProductOffer]:
        """
        Realiza scraping en Autozone
        
        TODO: Implementar parseo real
        """
        try:
            url = self.build_search_url(search_params)
            
            # TODO: Implementar scraping real
            products = []
            
            print(f"  Scraping Autozone: {url}")
            return products
            
        except Exception as e:
            raise ScrapingError(
                provider=self.provider.value,
                message=f"Error en scraping: {str(e)}"
            )
