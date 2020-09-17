from api.configuration.configuration import Configuration
from api.recognition import Recognition


# ..\manage.py runscript recognize_command --script-args jonsnow.jpg

def run(picture):
    configuration = Configuration()
    recognition = Recognition(configuration)
    recognition.recognize(picture)
