"""
Tests para capa de dominio
"""
import pytest
from decimal import Decimal
from ..domain.search_params import SearchParams
from ..domain.product_offer import ProductOffer
from ..domain.provider_enum import Provider
from ..domain.errors import InvalidSearchError


class TestSearchParams:
    """Tests para SearchParams"""
    
    def test_crear_search_params_valido(self):
        params = SearchParams(
            nombre_pieza="bujia NGK",
            marca="Honda",
            modelo="Civic",
            anio=2015
        )
        assert params.nombre_pieza == "bujia ngk"
        assert params.marca == "honda"
    
    def test_search_params_sin_nombre_pieza(self):
        with pytest.raises(ValueError):
            SearchParams(nombre_pieza="")
    
    def test_to_query_string(self):
        params = SearchParams(
            nombre_pieza="bujia",
            marca="Honda",
            anio=2015
        )
        assert "bujia" in params.to_query_string()
        assert "honda" in params.to_query_string()


class TestProductOffer:
    """Tests para ProductOffer"""
    
    def test_crear_producto_valido(self):
        producto = ProductOffer(
            titulo="Bujia NGK",
            precio=Decimal("120.00"),
            url="https://test.com/product",
            fuente=Provider.MERCADO_LIBRE,
            metadata={}
        )
        assert producto.titulo == "Bujia NGK"
        assert producto.precio == Decimal("120.00")
    
    def test_producto_sin_titulo(self):
        with pytest.raises(ValueError):
            ProductOffer(
                titulo="",
                precio=Decimal("100"),
                url="https://test.com",
                fuente=Provider.AMAZON,
                metadata={}
            )


# TODO: Agregar mas tests
