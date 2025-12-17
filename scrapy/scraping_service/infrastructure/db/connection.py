"""
Capa de infraestructura - Database Connection
Gestiona pool de conexiones a PostgreSQL
"""
import asyncpg
from typing import Optional
from .config import DatabaseConfig

# Pool global de conexiones
_pool: Optional[asyncpg.Pool] = None


async def init_db() -> None:
    """
    Inicializa el pool de conexiones a PostgreSQL
    Se llama al iniciar la aplicacion
    """
    global _pool
    
    if _pool is not None:
        return
    
    db_url = DatabaseConfig.get_database_url()
    pool_config = DatabaseConfig.get_pool_config()
    
    _pool = await asyncpg.create_pool(
        db_url,
        min_size=pool_config["min_size"],
        max_size=pool_config["max_size"],
        command_timeout=60
    )
    
    print(f"Database pool initialized: {pool_config['min_size']}-{pool_config['max_size']} connections")


async def close_db() -> None:
    """
    Cierra el pool de conexiones
    Se llama al apagar la aplicacion
    """
    global _pool
    
    if _pool is not None:
        await _pool.close()
        _pool = None
        print("Database pool closed")


def get_pool() -> asyncpg.Pool:
    """
    Retorna el pool de conexiones
    
    Returns:
        asyncpg.Pool
    
    Raises:
        RuntimeError: Si el pool no esta inicializado
    """
    if _pool is None:
        raise RuntimeError("Database pool not initialized. Call init_db() first.")
    return _pool
