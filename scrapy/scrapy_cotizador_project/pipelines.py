# Define your item pipelines here
from decimal import Decimal


class ScrapyCotizadorPipeline:
    """
    Pipeline para limpiar y validar items
    """
    
    def process_item(self, item, spider):
        # Limpiar titulo
        if 'titulo' in item:
            item['titulo'] = item['titulo'].strip()
        
        # Validar precio
        if 'precio' in item:
            try:
                item['precio'] = Decimal(str(item['precio']))
            except:
                spider.logger.warning(f"Precio invalido: {item.get('precio')}")
                return None
        
        # Validar URL
        if 'url' not in item or not item['url']:
            spider.logger.warning("Item sin URL, descartado")
            return None
        
        return item
