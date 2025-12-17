"""
🟩 CAPA DOMINIO - Provider
Enum de proveedores/fuentes de datos
"""
from enum import Enum


class Provider(str, Enum):
    """
    Enum de proveedores de productos
    
    Define las plataformas soportadas para scraping.
    """
    MERCADO_LIBRE = "MERCADO_LIBRE"
    AMAZON = "AMAZON"
    AUTOZONE = "AUTOZONE"
    
    @classmethod
    def get_all(cls):
        """Retorna lista de todos los proveedores"""
        return [provider for provider in cls]
    
    @classmethod
    def from_string(cls, value: str):
        """
        Crea un Provider desde string
        
        Args:
            value: String del provider (case-insensitive)
        
        Returns:
            Provider enum
        
        Raises:
            ValueError: Si el provider no existe
        """
        try:
            return cls[value.upper()]
        except KeyError:
            raise ValueError(f"Provider '{value}' no es válido. Opciones: {[p.value for p in cls]}")
    
    def get_base_url(self) -> str:
        """Retorna la URL base del proveedor"""
        urls = {
            Provider.MERCADO_LIBRE: "https://www.mercadolibre.com.mx",
            Provider.AMAZON: "https://www.amazon.com.mx",
            Provider.AUTOZONE: "https://www.autozone.com.mx"
        }
        return urls[self]
    
    def __str__(self) -> str:
        return self.value
