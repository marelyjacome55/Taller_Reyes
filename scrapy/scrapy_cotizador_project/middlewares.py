# Define here the models for your spider middleware
from scrapy import signals
import random


class ScrapyCotizadorSpiderMiddleware:
    """Spider middleware"""
    
    @classmethod
    def from_crawler(cls, crawler):
        s = cls()
        crawler.signals.connect(s.spider_opened, signal=signals.spider_opened)
        return s

    def process_spider_input(self, response, spider):
        return None

    def process_spider_output(self, response, result, spider):
        for i in result:
            yield i

    def spider_opened(self, spider):
        spider.logger.info("Spider opened: %s" % spider.name)


class ScrapyCotizadorDownloaderMiddleware:
    """Downloader middleware"""

    @classmethod
    def from_crawler(cls, crawler):
        s = cls()
        crawler.signals.connect(s.spider_opened, signal=signals.spider_opened)
        return s

    def process_request(self, request, spider):
        # Rotar User-Agent
        user_agents = spider.settings.get('USER_AGENT_LIST')
        if user_agents:
            request.headers['User-Agent'] = random.choice(user_agents)
        return None

    def spider_opened(self, spider):
        spider.logger.info("Spider opened: %s" % spider.name)
