"""
Capa de infraestructura - Results Repository
CRUD sobre tabla resultados_scrapy
"""
from typing import List, Dict, Any
import uuid
from datetime import datetime
from .connection import get_pool
from ...domain.product_offer import ProductOffer


class ResultsRepository:
    """
    Repositorio para tabla resultados_scrapy
    
    Tabla:
        resultados_scrapy (
            id UUID PRIMARY KEY,
            cache_id UUID REFERENCES cache_scrapy(id),
            titulo TEXT,
            precio DECIMAL,
            url TEXT,
            fuente TEXT,
            metadata JSONB,
            created_at TIMESTAMP
        )
    """
    
    async def save(self, cache_id: str, product: ProductOffer) -> str:
        """
        Guarda un producto en la tabla
        
        Args:
            cache_id: ID del cache padre
            product: ProductOffer a guardar
        
        Returns:
            ID del registro insertado
        """
        pool = get_pool()
        result_id = str(uuid.uuid4())
        
        query = """
            INSERT INTO resultados_scrapy 
            (id, cache_id, titulo, precio, url, fuente, metadata, created_at)
            VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
            RETURNING id
        """
        
        async with pool.acquire() as conn:
            result = await conn.fetchval(
                query,
                result_id,
                cache_id,
                product.titulo,
                float(product.precio),
                product.url,
                product.fuente.value,
                product.metadata,
                datetime.utcnow()
            )
            return str(result)
    
    async def get_by_cache_id(self, cache_id: str) -> List[Dict[str, Any]]:
        """
        Obtiene todos los productos de un cache
        
        Args:
            cache_id: ID del cache
        
        Returns:
            Lista de productos
        """
        pool = get_pool()
        
        query = """
            SELECT id, titulo, precio, url, fuente, metadata, created_at
            FROM resultados_scrapy
            WHERE cache_id = $1
            ORDER BY precio ASC
        """
        
        async with pool.acquire() as conn:
            rows = await conn.fetch(query, cache_id)
            
            return [
                {
                    "id": row["id"],
                    "titulo": row["titulo"],
                    "precio": row["precio"],
                    "url": row["url"],
                    "fuente": row["fuente"],
                    "metadata": row["metadata"],
                    "created_at": row["created_at"]
                }
                for row in rows
            ]
    
    async def delete_by_cache_id(self, cache_id: str) -> int:
        """
        Elimina todos los productos de un cache
        
        Returns:
            Numero de registros eliminados
        """
        pool = get_pool()
        
        query = "DELETE FROM resultados_scrapy WHERE cache_id = $1"
        
        async with pool.acquire() as conn:
            result = await conn.execute(query, cache_id)
            return int(result.split()[-1])
