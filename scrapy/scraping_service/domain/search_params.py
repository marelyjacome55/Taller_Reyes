"""
🟩 CAPA DOMINIO - SearchParams
Entidad que representa los parámetros de búsqueda
"""
from dataclasses import dataclass
from typing import Optional
from datetime import datetime
import hashlib
import json


@dataclass
class SearchParams:
    """
    Parámetros de búsqueda de refacciones
    
    Representa la intención de búsqueda del usuario.
    Esta clase es inmutable (frozen) para garantizar consistencia.
    """
    nombre_pieza: str
    marca: Optional[str] = None
    modelo: Optional[str] = None
    anio: Optional[int] = None
    version: Optional[str] = None
    mecanica: Optional[str] = None
    
    def __post_init__(self):
        """Validaciones post-inicialización"""
        if not self.nombre_pieza or self.nombre_pieza.strip() == "":
            raise ValueError("nombre_pieza es obligatorio")
        
        # Normalizar strings
        self.nombre_pieza = self.nombre_pieza.strip().lower()
        if self.marca:
            self.marca = self.marca.strip().lower()
        if self.modelo:
            self.modelo = self.modelo.strip().lower()
        if self.version:
            self.version = self.version.strip().lower()
        if self.mecanica:
            self.mecanica = self.mecanica.strip().lower()
    
    def to_query_string(self) -> str:
        """
        Convierte los parámetros a un string de búsqueda
        Usado para construir queries en spiders
        
        Returns:
            String: "bujia ngk honda civic 2015"
        """
        parts = [self.nombre_pieza]
        
        if self.marca:
            parts.append(self.marca)
        if self.modelo:
            parts.append(self.modelo)
        if self.anio:
            parts.append(str(self.anio))
        if self.version:
            parts.append(self.version)
        if self.mecanica:
            parts.append(self.mecanica)
        
        return " ".join(parts)
    
    def to_cache_key(self) -> str:
        """
        Genera un hash único para usar como key de cache
        
        Returns:
            String hash SHA256
        """
        params_dict = {
            "nombre_pieza": self.nombre_pieza,
            "marca": self.marca,
            "modelo": self.modelo,
            "anio": self.anio,
            "version": self.version,
            "mecanica": self.mecanica
        }
        
        # Ordenar keys para garantizar mismo hash con mismos parámetros
        params_json = json.dumps(params_dict, sort_keys=True)
        return hashlib.sha256(params_json.encode()).hexdigest()
    
    def to_dict(self) -> dict:
        """Convierte a diccionario"""
        return {
            "nombre_pieza": self.nombre_pieza,
            "marca": self.marca,
            "modelo": self.modelo,
            "anio": self.anio,
            "version": self.version,
            "mecanica": self.mecanica
        }
    
    def __str__(self) -> str:
        return self.to_query_string()
