"""
Capa de infraestructura - Base Spider
Clase base abstracta para todos los spiders
"""
from abc import ABC, abstractmethod
from typing import List
from ...domain.product_offer import ProductOffer
from ...domain.search_params import SearchParams
from ...domain.provider_enum import Provider


class BaseSpider(ABC):
    """
    Clase base abstracta para spiders de tiendas
    
    Define la interfaz comun que todos los spiders deben implementar
    """
    
    def __init__(self, provider: Provider):
        self.provider = provider
        self.base_url = provider.get_base_url()
    
    @abstractmethod
    async def scrape(self, search_params: SearchParams) -> List[ProductOffer]:
        """
        Metodo principal de scraping
        
        Args:
            search_params: Parametros de busqueda
        
        Returns:
            Lista de ProductOffer encontrados
        
        Raises:
            ScrapingError: Si hay error en el scraping
        """
        pass
    
    @abstractmethod
    def build_search_url(self, search_params: SearchParams) -> str:
        """
        Construye la URL de busqueda para este proveedor
        
        Args:
            search_params: Parametros de busqueda
        
        Returns:
            URL completa para realizar la busqueda
        """
        pass
    
    def get_provider_name(self) -> str:
        """Retorna el nombre del proveedor"""
        return self.provider.value
    
    def __str__(self) -> str:
        return f"{self.__class__.__name__} ({self.provider.value})"
