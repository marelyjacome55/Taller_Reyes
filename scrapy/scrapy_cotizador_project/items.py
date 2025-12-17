# Define here the models for your scraped items
import scrapy


class ProductItem(scrapy.Item):
    """
    Item de Scrapy para productos
    """
    titulo = scrapy.Field()
    precio = scrapy.Field()
    url = scrapy.Field()
    fuente = scrapy.Field()
    imagen_url = scrapy.Field()
    descripcion = scrapy.Field()
    metadata = scrapy.Field()
    disponibilidad = scrapy.Field()
    fecha_scraping = scrapy.Field()
