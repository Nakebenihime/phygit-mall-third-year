from api.configuration.configuration import Configuration
from api.recognition import Recognition


# ..\manage.py runscript recognize_from_stream_command --script-args udp://....

def run(stream):
    configuration = Configuration()
    recognition = Recognition(configuration)
    recognition.recognize_from_stream(stream)