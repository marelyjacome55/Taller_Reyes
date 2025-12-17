# Proyecto Scrapy Clasico - Settings
# https://docs.scrapy.org/en/latest/topics/settings.html

BOT_NAME = "scrapy_cotizador_project"

SPIDER_MODULES = ["scrapy_cotizador_project.spiders"]
NEWSPIDER_MODULE = "scrapy_cotizador_project.spiders"

# Obey robots.txt rules
ROBOTSTXT_OBEY = False  # Cambiar a True en produccion si es necesario

# Configure maximum concurrent requests
CONCURRENT_REQUESTS = 8

# Configure a delay for requests for the same website
DOWNLOAD_DELAY = 2

# Disable cookies (enabled by default)
COOKIES_ENABLED = False

# Override the default request headers
DEFAULT_REQUEST_HEADERS = {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Accept-Language": "es-MX,es;q=0.9,en;q=0.8",
}

# Enable or disable spider middlewares
SPIDER_MIDDLEWARES = {
    "scrapy_cotizador_project.middlewares.ScrapyCotizadorSpiderMiddleware": 543,
}

# Enable or disable downloader middlewares
DOWNLOADER_MIDDLEWARES = {
    "scrapy_cotizador_project.middlewares.ScrapyCotizadorDownloaderMiddleware": 543,
}

# Configure item pipelines
ITEM_PIPELINES = {
    "scrapy_cotizador_project.pipelines.ScrapyCotizadorPipeline": 300,
}

# Enable and configure HTTP caching
HTTPCACHE_ENABLED = True
HTTPCACHE_EXPIRATION_SECS = 3600
HTTPCACHE_DIR = "httpcache"

# Set settings whose default value is deprecated
REQUEST_FINGERPRINTER_IMPLEMENTATION = "2.7"
TWISTED_REACTOR = "twisted.internet.asyncioreactor.AsyncioSelectorReactor"
FEED_EXPORT_ENCODING = "utf-8"

# User-Agent rotation (anti-bot)
USER_AGENT_LIST = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36",
]

# Retry settings
RETRY_TIMES = 3
RETRY_HTTP_CODES = [500, 502, 503, 504, 522, 524, 408, 429]

# AutoThrottle settings
AUTOTHROTTLE_ENABLED = True
AUTOTHROTTLE_START_DELAY = 1
AUTOTHROTTLE_MAX_DELAY = 10
AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0

# Logging
LOG_LEVEL = "INFO"
