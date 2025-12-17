"""
🟩 CAPA DOMINIO - Errors
Excepciones personalizadas del dominio
"""


class ScrapyDomainError(Exception):
    """Excepción base para errores de dominio"""
    pass


class InvalidSearchError(ScrapyDomainError):
    """Error cuando los parámetros de búsqueda son inválidos"""
    def __init__(self, message: str = "Parámetros de búsqueda inválidos"):
        self.message = message
        super().__init__(self.message)


class NoResultsFoundError(ScrapyDomainError):
    """Error cuando no se encuentran resultados"""
    def __init__(self, message: str = "No se encontraron resultados"):
        self.message = message
        super().__init__(self.message)


class ScrapingError(ScrapyDomainError):
    """Error durante el proceso de scraping"""
    def __init__(self, provider: str, message: str = "Error en scraping"):
        self.provider = provider
        self.message = f"[{provider}] {message}"
        super().__init__(self.message)


class DatabaseError(ScrapyDomainError):
    """Error en operaciones de base de datos"""
    def __init__(self, message: str = "Error de base de datos"):
        self.message = message
        super().__init__(self.message)


class CacheError(ScrapyDomainError):
    """Error en operaciones de cache"""
    def __init__(self, message: str = "Error de cache"):
        self.message = message
        super().__init__(self.message)
