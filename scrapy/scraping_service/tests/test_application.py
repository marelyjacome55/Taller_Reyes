"""
Tests para capa de aplicacion
"""
import pytest
from decimal import Decimal
from ..application.ranking_service import RankingService
from ..domain.product_offer import ProductOffer
from ..domain.provider_enum import Provider


class TestRankingService:
    """Tests para RankingService"""
    
    def setup_method(self):
        self.ranking_service = RankingService()
    
    def test_rank_by_price(self):
        productos = [
            ProductOffer(
                titulo="Producto A",
                precio=Decimal("150"),
                url="https://test.com/a",
                fuente=Provider.MERCADO_LIBRE,
                metadata={}
            ),
            ProductOffer(
                titulo="Producto B",
                precio=Decimal("100"),
                url="https://test.com/b",
                fuente=Provider.AMAZON,
                metadata={}
            ),
        ]
        
        ranked = self.ranking_service.rank_by_price(productos)
        
        assert len(ranked) == 2
        assert ranked[0].precio == Decimal("100")
        assert ranked[1].precio == Decimal("150")
    
    def test_eliminar_duplicados(self):
        producto = ProductOffer(
            titulo="Producto",
            precio=Decimal("100"),
            url="https://test.com/same",
            fuente=Provider.MERCADO_LIBRE,
            metadata={}
        )
        
        productos = [producto, producto]  # Duplicado
        ranked = self.ranking_service.rank_by_price(productos)
        
        assert len(ranked) == 1


# TODO: Agregar mas tests
