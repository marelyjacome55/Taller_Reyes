"""
🟨 CAPA APLICACIÓN - CacheService
Gestiona cache de resultados de scraping
"""
from typing import List, Optional
from datetime import datetime, timedelta
import os
from ..domain.search_params import SearchParams
from ..domain.product_offer import ProductOffer
from ..infrastructure.db.cache_repository import CacheRepository
from ..infrastructure.db.results_repository import ResultsRepository


class CacheService:
    """
    Servicio para gestionar cache de búsquedas
    
    Responsabilidades:
    - Verificar si existe cache válido
    - Guardar resultados en cache
    - Invalidar cache expirado
    """
    
    def __init__(
        self,
        cache_repository: CacheRepository,
        results_repository: ResultsRepository
    ):
        self.cache_repository = cache_repository
        self.results_repository = results_repository
        
        # TTL del cache desde env (default 24 horas)
        self.cache_ttl_hours = int(os.getenv("CACHE_TTL_HOURS", "24"))
        self.cache_enabled = os.getenv("CACHE_ENABLED", "true").lower() == "true"
    
    async def get_cached_results(
        self,
        search_params: SearchParams
    ) -> Optional[List[ProductOffer]]:
        """
        Busca resultados en cache
        
        Args:
            search_params: Parámetros de búsqueda
        
        Returns:
            Lista de ProductOffer si existe cache válido, None si no
        """
        if not self.cache_enabled:
            return None
        
        # Generar hash único para esta búsqueda
        cache_key = search_params.to_cache_key()
        
        # Buscar en tabla cache_scrapy
        cache_entry = await self.cache_repository.get_by_hash(cache_key)
        
        if not cache_entry:
            return None
        
        # Verificar si está expirado
        if self._is_expired(cache_entry["expires_at"]):
            # Invalidar cache
            await self.cache_repository.delete_by_hash(cache_key)
            return None
        
        # Reconstruir ProductOffer desde data_json
        products = self._deserialize_products(cache_entry["data_json"])
        return products
    
    async def save_results(
        self,
        search_params: SearchParams,
        products: List[ProductOffer]
    ) -> None:
        """
        Guarda resultados en cache
        
        Args:
            search_params: Parámetros de búsqueda
            products: Lista de productos a cachear
        """
        if not self.cache_enabled or not products:
            return
        
        # Generar hash
        cache_key = search_params.to_cache_key()
        
        # Calcular expires_at
        expires_at = datetime.utcnow() + timedelta(hours=self.cache_ttl_hours)
        
        # Serializar productos
        data_json = self._serialize_products(products)
        
        # Guardar en tabla cache_scrapy
        cache_id = await self.cache_repository.save(
            search_hash=cache_key,
            data_json=data_json,
            expires_at=expires_at
        )
        
        # Guardar productos individuales en resultados_scrapy
        for product in products:
            await self.results_repository.save(
                cache_id=cache_id,
                product=product
            )
        
        print(f"💾 Cache guardado: {cache_key} (expira en {self.cache_ttl_hours}h)")
    
    async def invalidate_all_expired(self) -> int:
        """
        Invalida todos los caches expirados
        
        Returns:
            Número de registros eliminados
        """
        return await self.cache_repository.delete_expired()
    
    def _is_expired(self, expires_at: datetime) -> bool:
        """Verifica si un cache está expirado"""
        return datetime.utcnow() > expires_at
    
    def _serialize_products(self, products: List[ProductOffer]) -> dict:
        """Serializa productos a JSON"""
        return {
            "products": [p.to_dict() for p in products],
            "count": len(products),
            "timestamp": datetime.utcnow().isoformat()
        }
    
    def _deserialize_products(self, data_json: dict) -> List[ProductOffer]:
        """
        Deserializa JSON a ProductOffer
        
        TODO: Implementar reconstrucción completa desde data_json
        """
        # Por ahora retornar lista vacía
        # En implementación real, reconstruir ProductOffer desde data_json["products"]
        return []
