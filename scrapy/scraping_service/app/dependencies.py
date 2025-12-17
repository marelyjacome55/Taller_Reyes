"""
🟦 CAPA PRESENTACIÓN - Dependencies
Inyección de dependencias para FastAPI
"""
from ..application.search_service import SearchService
from ..application.ranking_service import RankingService
from ..application.cache_service import CacheService
from ..infrastructure.db.cache_repository import CacheRepository
from ..infrastructure.db.results_repository import ResultsRepository
from ..infrastructure.runners.scrapy_runner import ScrapyRunner

"""
SearchService → el servicio principal que orquesta todo el proceso de buscar piezas.
RankingService → ordena por precio, filtra, etc. (lógica de negocio).
CacheService → administra lectura/escritura de caché (si ya se buscó algo, no volver a scrapear).
CacheRepository / ResultsRepository → acceden a la base de datos PostgreSQL/Supabase (infraestructura).
ScrapyRunner → ejecuta los spiders y obtiene resultados frescos.
"""

""" 
Crea la clase que habla con la tabla cache_scrapy en PostgreSQL.
Guarda búsquedas previas
Lee registros si ya existen
Evita que el scraper se ejecute cada vez 
"""
def get_cache_repository() -> CacheRepository:
    """Factory para CacheRepository"""
    return CacheRepository()

"""Encargado de la tabla resultados_scrapy, donde se guardan los resultados limpios por pieza.
Es un repositorio diferente porque:
cache_scrapy guarda JSON crudo
resultados_scrapy guarda precios procesados para UI"""

def get_results_repository() -> ResultsRepository:
    """Factory para ResultsRepository"""
    return ResultsRepository()

"""
Crea el objeto que:
Ejecuta spiders
Controla sus procesos
Devuelve los resultados a SearchService
Es infraestructura pura (scrapy, spiders, procesos).
"""
def get_scrapy_runner() -> ScrapyRunner:
    """Factory para ScrapyRunner"""
    return ScrapyRunner()

"""
Crea el servicio de lógica de caché.
Este servicio:
Genera search_key desde SearchParams
Verifica si hay un resultado previo
Expira resultados cuando corresponde
Guarda nuevas entradas
Usa los repositorios como dependencia (inyección).
"""
def get_cache_service(
    cache_repo: CacheRepository = None,
    results_repo: ResultsRepository = None
) -> CacheService:
    """Factory para CacheService"""
    if cache_repo is None:
        cache_repo = get_cache_repository()
    if results_repo is None:
        results_repo = get_results_repository()
    return CacheService(cache_repo, results_repo)

"""
Crea el servicio que:
Ordena productos por precio
Filtra duplicados
Aplica límite (por ejemplo top 3)
Aplica reglas de negocio
"""
def get_ranking_service() -> RankingService:
    """Factory para RankingService"""
    return RankingService()

"""
Este método:
Construye el motor completo de scraping
Une:
CacheService
RankingService
ScrapyRunner
Y crea la instancia que usará cada endpoint:
SearchService(...)
SearchService es el cerebro principal:
Revisa caché
Si no hay datos → ejecuta spiders
Procesa y ordena datos
Guarda nuevos resultados
Devuelve la lista al controlador
Los endpoints del router lo reciben así:
search_service: SearchService = Depends(get_search_service)
"""
def get_search_service() -> SearchService:
    """
    Factory para SearchService (dependencia principal)
    Instancia todos los servicios necesarios
    """
    cache_service = get_cache_service()
    ranking_service = get_ranking_service()
    scrapy_runner = get_scrapy_runner()
    
    return SearchService(
        cache_service=cache_service,
        ranking_service=ranking_service,
        scrapy_runner=scrapy_runner
    )
