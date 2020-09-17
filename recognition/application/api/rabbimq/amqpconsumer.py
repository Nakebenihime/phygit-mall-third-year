import logging
from threading import Thread

import pika as pika


class AMQPConsumer(Thread):
    logging.getLogger("pika").setLevel(logging.WARNING)

    def __init__(self, _config_):
        Thread.__init__(self)
        self.credentials = pika.credentials.PlainCredentials(
            _config_.get_rabbitmq_username(),
            _config_.get_rabbitmq_password())
        self.parameters = pika.ConnectionParameters(
            host=_config_.get_rabbitmq_host(),
            port=_config_.get_rabbitmq_port(),
            virtual_host=_config_.get_rabbitmq_vhost(),
            credentials=self.credentials)
        self.connection = pika.BlockingConnection(parameters=self.parameters)
        self.channel = self.connection.channel()
        self.rmq = _config_.get_messaging_queue()
        self.channel.basic_consume(queue=self.rmq, on_message_callback=self.callback, auto_ack=True)

    def callback(self, ch, method, properties, body):
        print(" [x] Received %r" % str(body))

    def run(self):
        while True:
            self.channel = self.connection.channel()
            self.channel.basic_consume(queue=self.rmq, on_message_callback=self.callback, auto_ack=True)
            self.channel.start_consuming()

    def start_consuming(self):
        logging.info("Starts consuming on message bus")
        self.channel.start_consuming()
