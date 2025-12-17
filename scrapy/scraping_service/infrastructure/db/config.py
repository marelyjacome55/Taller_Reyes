"""
Capa de infraestructura - Database Configuration
Lee configuracion de variables de entorno
"""
import os
from typing import Dict, Any
from dotenv import load_dotenv

load_dotenv()


class DatabaseConfig:
    """
    Configuracion de PostgreSQL/Supabase
    """
    
    @staticmethod
    def get_connection_params() -> Dict[str, Any]:
        """
        Retorna parametros de conexion desde .env
        
        Returns:
            Dict con host, port, database, user, password
        """
        return {
            "host": os.getenv("DB_HOST", "localhost"),
            "port": int(os.getenv("DB_PORT", "5432")),
            "database": os.getenv("DB_NAME", "taller_reyes"),
            "user": os.getenv("DB_USER", "postgres"),
            "password": os.getenv("DB_PASSWORD", ""),
        }
    
    @staticmethod
    def get_database_url() -> str:
        """
        Retorna URL de conexion completa
        
        Returns:
            String: postgresql://user:pass@host:port/database
        """
        url = os.getenv("DATABASE_URL")
        if url:
            return url
        
        params = DatabaseConfig.get_connection_params()
        return (
            f"postgresql://{params['user']}:{params['password']}"
            f"@{params['host']}:{params['port']}/{params['database']}"
        )
    
    @staticmethod
    def get_pool_config() -> Dict[str, int]:
        """
        Retorna configuracion del pool de conexiones
        """
        return {
            "min_size": int(os.getenv("DB_POOL_MIN_SIZE", "5")),
            "max_size": int(os.getenv("DB_POOL_MAX_SIZE", "20")),
        }
