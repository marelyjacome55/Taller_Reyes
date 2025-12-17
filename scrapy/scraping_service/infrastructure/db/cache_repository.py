"""
Capa de infraestructura - Cache Repository
CRUD sobre tabla cache_scrapy
"""
from typing import Optional, Dict, Any
from datetime import datetime
import uuid
from .connection import get_pool


class CacheRepository:
    """
    Repositorio para tabla cache_scrapy
    
    Tabla:
        cache_scrapy (
            id UUID PRIMARY KEY,
            search_hash TEXT UNIQUE,
            data_json JSONB,
            created_at TIMESTAMP,
            expires_at TIMESTAMP
        )
    """
    
    async def get_by_hash(self, search_hash: str) -> Optional[Dict[str, Any]]:
        """
        Busca entrada de cache por hash
        
        Args:
            search_hash: Hash SHA256 de los parametros de busqueda
        
        Returns:
            Dict con datos de cache o None si no existe
        """
        pool = get_pool()
        
        query = """
            SELECT id, search_hash, data_json, created_at, expires_at
            FROM cache_scrapy
            WHERE search_hash = $1
        """
        
        async with pool.acquire() as conn:
            row = await conn.fetchrow(query, search_hash)
            
            if row is None:
                return None
            
            return {
                "id": row["id"],
                "search_hash": row["search_hash"],
                "data_json": row["data_json"],
                "created_at": row["created_at"],
                "expires_at": row["expires_at"]
            }
    
    async def save(
        self,
        search_hash: str,
        data_json: dict,
        expires_at: datetime
    ) -> str:
        """
        Guarda o actualiza entrada de cache
        
        Args:
            search_hash: Hash unico de busqueda
            data_json: Datos serializados
            expires_at: Fecha de expiracion
        
        Returns:
            ID del registro (UUID)
        """
        pool = get_pool()
        cache_id = str(uuid.uuid4())
        
        query = """
            INSERT INTO cache_scrapy (id, search_hash, data_json, created_at, expires_at)
            VALUES ($1, $2, $3, $4, $5)
            ON CONFLICT (search_hash) 
            DO UPDATE SET 
                data_json = EXCLUDED.data_json,
                created_at = EXCLUDED.created_at,
                expires_at = EXCLUDED.expires_at
            RETURNING id
        """
        
        async with pool.acquire() as conn:
            result = await conn.fetchval(
                query,
                cache_id,
                search_hash,
                data_json,
                datetime.utcnow(),
                expires_at
            )
            return str(result)
    
    async def delete_by_hash(self, search_hash: str) -> bool:
        """
        Elimina entrada de cache por hash
        
        Returns:
            True si se elimino, False si no existia
        """
        pool = get_pool()
        
        query = "DELETE FROM cache_scrapy WHERE search_hash = $1"
        
        async with pool.acquire() as conn:
            result = await conn.execute(query, search_hash)
            return result != "DELETE 0"
    
    async def delete_expired(self) -> int:
        """
        Elimina todas las entradas expiradas
        
        Returns:
            Numero de registros eliminados
        """
        pool = get_pool()
        
        query = "DELETE FROM cache_scrapy WHERE expires_at < $1"
        
        async with pool.acquire() as conn:
            result = await conn.execute(query, datetime.utcnow())
            # Extraer numero de DELETE 5
            return int(result.split()[-1])
