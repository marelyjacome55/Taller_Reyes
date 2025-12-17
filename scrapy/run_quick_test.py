"""
Quick test runner
Ejecuta SearchService con dependencias simuladas (sin DB ni Scrapy real)
"""
import asyncio
from decimal import Decimal

from scraping_service.application.search_service import SearchService
from scraping_service.application.ranking_service import RankingService
from scraping_service.application.cache_service import CacheService
from scraping_service.infrastructure.runners.scrapy_runner import ScrapyRunner
from scraping_service.domain.product_offer import ProductOffer
from scraping_service.domain.provider_enum import Provider


class DummyCacheService:
    async def get_cached_results(self, search_params):
        return None

    async def save_results(self, search_params, products):
        print("[DummyCacheService] save_results called (no-op)")
        return None


class DummyScrapyRunner:
    async def run_spider(self, provider, search_params):
        # Return one fake product per provider
        p = ProductOffer(
            titulo=f"Prueba {provider.value} - {search_params.nombre_pieza}",
            precio=Decimal("99.99") if provider == Provider.MERCADO_LIBRE else Decimal("120.00"),
            url=f"https://example.com/{provider.value.lower()}",
            fuente=provider,
            metadata={"source_test": True}
        )
        return [p]


async def main():
    # Instanciar servicios con doubles
    cache_service = DummyCacheService()
    ranking_service = RankingService()
    scrapy_runner = DummyScrapyRunner()

    search_service = SearchService(
        cache_service=cache_service,
        ranking_service=ranking_service,
        scrapy_runner=scrapy_runner
    )

    # Ejecutar búsqueda de prueba
    results = await search_service.search(
        nombre_pieza="bujia NGK",
        marca="Honda",
        modelo="Civic",
        anio=2015
    )

    print("\nResultados:")
    for r in results:
        print(r.to_dict())


if __name__ == "__main__":
    asyncio.run(main())
