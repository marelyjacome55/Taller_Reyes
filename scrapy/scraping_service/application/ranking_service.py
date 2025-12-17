"""
🟨 CAPA APLICACIÓN - RankingService
Ordena y filtra resultados de scraping
"""
from typing import List
from decimal import Decimal
from ..domain.product_offer import ProductOffer


class RankingService:
    """
    Servicio para rankear y filtrar resultados
    
    Responsabilidades:
    - Ordenar por precio (menor a mayor)
    - Eliminar duplicados
    - Aplicar filtros de calidad
    """
    
    def rank_by_price(
        self,
        products: List[ProductOffer],
        ascending: bool = True
    ) -> List[ProductOffer]:
        """
        Ordena productos por precio
        
        Args:
            products: Lista de productos
            ascending: True para menor a mayor (default), False para mayor a menor
        
        Returns:
            Lista ordenada sin duplicados
        """
        if not products:
            return []
        
        # 1. Eliminar duplicados (basado en __eq__ de ProductOffer que usa URL)
        unique_products = list(set(products))
        
        # 2. Ordenar por precio
        sorted_products = sorted(
            unique_products,
            key=lambda p: p.precio,
            reverse=not ascending
        )
        
        return sorted_products
    
    def filter_by_price_range(
        self,
        products: List[ProductOffer],
        min_price: Decimal = None,
        max_price: Decimal = None
    ) -> List[ProductOffer]:
        """
        Filtra productos por rango de precio
        
        Args:
            products: Lista de productos
            min_price: Precio mínimo (opcional)
            max_price: Precio máximo (opcional)
        
        Returns:
            Lista filtrada
        """
        filtered = products
        
        if min_price is not None:
            filtered = [p for p in filtered if p.precio >= min_price]
        
        if max_price is not None:
            filtered = [p for p in filtered if p.precio <= max_price]
        
        return filtered
    
    def filter_by_availability(
        self,
        products: List[ProductOffer],
        only_available: bool = True
    ) -> List[ProductOffer]:
        """
        Filtra productos por disponibilidad
        
        Args:
            products: Lista de productos
            only_available: Si True, solo retorna disponibles
        
        Returns:
            Lista filtrada
        """
        if only_available:
            return [p for p in products if p.disponibilidad]
        return products
    
    def filter_by_provider(
        self,
        products: List[ProductOffer],
        providers: List[str]
    ) -> List[ProductOffer]:
        """
        Filtra productos por proveedor
        
        Args:
            products: Lista de productos
            providers: Lista de nombres de proveedores (ej: ['MERCADO_LIBRE'])
        
        Returns:
            Lista filtrada
        """
        if not providers:
            return products
        
        return [p for p in products if p.fuente.value in providers]
    
    def get_top_n(
        self,
        products: List[ProductOffer],
        n: int = 10
    ) -> List[ProductOffer]:
        """
        Retorna los primeros N productos
        
        Args:
            products: Lista de productos
            n: Número de productos a retornar
        
        Returns:
            Lista con máximo N productos
        """
        return products[:n]
