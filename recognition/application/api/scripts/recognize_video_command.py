from api.configuration.configuration import Configuration
from api.recognition import Recognition


# ..\manage.py runscript recognize_video_command --script-args video.mp4

def run(video):
    configuration = Configuration()
    recognition = Recognition(configuration)
    recognition.recognize_from_video(video)
