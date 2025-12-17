"""
Capa de infraestructura - Spider Factory
Factory para crear instancias de spiders
"""
from typing import Dict
from ...domain.provider_enum import Provider
from .base_spider import BaseSpider
from .mercado_libre_spider import MercadoLibreSpider
from .amazon_spider import AmazonSpider
from .autozone_spider import AutozoneSpider


class SpiderFactory:
    """
    Factory pattern para crear spiders segun el proveedor
    """
    
    _spiders: Dict[Provider, type] = {
        Provider.MERCADO_LIBRE: MercadoLibreSpider,
        Provider.AMAZON: AmazonSpider,
        Provider.AUTOZONE: AutozoneSpider,
    }
    
    @classmethod
    def create_spider(cls, provider: Provider) -> BaseSpider:
        """
        Crea una instancia de spider para el proveedor dado
        
        Args:
            provider: Enum Provider
        
        Returns:
            Instancia de BaseSpider
        
        Raises:
            ValueError: Si el proveedor no esta soportado
        """
        spider_class = cls._spiders.get(provider)
        
        if spider_class is None:
            raise ValueError(f"No hay spider implementado para: {provider.value}")
        
        return spider_class()
    
    @classmethod
    def get_supported_providers(cls) -> list:
        """Retorna lista de proveedores soportados"""
        return list(cls._spiders.keys())
