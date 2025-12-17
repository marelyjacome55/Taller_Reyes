"""
Tests para capa de infraestructura
"""
import pytest
from ..infrastructure.scraping.spider_factory import SpiderFactory
from ..domain.provider_enum import Provider


class TestSpiderFactory:
    """Tests para SpiderFactory"""
    
    def test_crear_spider_mercado_libre(self):
        spider = SpiderFactory.create_spider(Provider.MERCADO_LIBRE)
        assert spider is not None
        assert spider.provider == Provider.MERCADO_LIBRE
    
    def test_crear_spider_amazon(self):
        spider = SpiderFactory.create_spider(Provider.AMAZON)
        assert spider is not None
    
    def test_obtener_proveedores_soportados(self):
        providers = SpiderFactory.get_supported_providers()
        assert len(providers) == 3


# TODO: Agregar mas tests
