"""
🟩 CAPA DOMINIO - ProductOffer
Entidad que representa una oferta de producto
"""
from dataclasses import dataclass
from typing import Optional, Dict, Any
from decimal import Decimal
from datetime import datetime
from .provider_enum import Provider


@dataclass
class ProductOffer:
    """
    Representa una oferta de producto encontrada en un proveedor
    
    Esta es la entidad principal del dominio de scraping.
    Contiene toda la información relevante de un producto.
    """
    titulo: str
    precio: Decimal
    url: str
    fuente: Provider
    metadata: Dict[str, Any]
    
    # Campos opcionales
    imagen_url: Optional[str] = None
    descripcion: Optional[str] = None
    disponibilidad: bool = True
    fecha_scraping: Optional[datetime] = None
    
    def __post_init__(self):
        """Validaciones y normalizaciones"""
        if not self.titulo or self.titulo.strip() == "":
            raise ValueError("titulo es obligatorio")
        
        if self.precio < 0:
            raise ValueError("precio no puede ser negativo")
        
        if not self.url or not self.url.startswith("http"):
            raise ValueError("url debe ser válida")
        
        # Normalizar título
        self.titulo = self.titulo.strip()
        
        # Asignar fecha si no existe
        if self.fecha_scraping is None:
            self.fecha_scraping = datetime.utcnow()
    
    def get_shipping_info(self) -> Optional[str]:
        """Extrae información de envío desde metadata"""
        return self.metadata.get("shipping")
    
    def get_rating(self) -> Optional[float]:
        """Extrae calificación del vendedor/producto desde metadata"""
        rating = self.metadata.get("rating")
        if rating:
            try:
                return float(rating)
            except (ValueError, TypeError):
                return None
        return None
    
    def get_vendidos(self) -> Optional[int]:
        """Extrae número de ventas desde metadata"""
        vendidos = self.metadata.get("vendidos")
        if vendidos:
            try:
                return int(vendidos)
            except (ValueError, TypeError):
                return None
        return None
    
    def to_dict(self) -> dict:
        """Convierte a diccionario para serialización"""
        return {
            "titulo": self.titulo,
            "precio": float(self.precio),
            "url": self.url,
            "fuente": self.fuente.value,
            "metadata": self.metadata,
            "imagen_url": self.imagen_url,
            "descripcion": self.descripcion,
            "disponibilidad": self.disponibilidad,
            "fecha_scraping": self.fecha_scraping.isoformat() if self.fecha_scraping else None
        }
    
    def __str__(self) -> str:
        return f"{self.titulo} - ${self.precio} ({self.fuente.value})"
    
    def __eq__(self, other) -> bool:
        """Dos productos son iguales si tienen la misma URL"""
        if not isinstance(other, ProductOffer):
            return False
        return self.url == other.url
    
    def __hash__(self) -> int:
        """Hash basado en URL para usar en sets"""
        return hash(self.url)
