from django.apps import AppConfig

from api.configuration.configuration import Configuration
from api.rabbimq.amqpconsumer import AMQPConsumer


class ApiConfig(AppConfig):
    name = 'api'

    def ready(self):
        configuration = Configuration()
        consumer = AMQPConsumer(configuration)
        consumer.daemon = True
        print("AMQP Daemon started")
        consumer.start()

