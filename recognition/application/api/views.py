from django.http import HttpResponse
from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

# Create your views here.
from api.configuration.configuration import Configuration
from api.recognition import Recognition

from api.models import Detection
from api.serializer import DetectionSerializer


@api_view(['GET'])
def overview(request):
    api_urls = {
        'extraction': '/recognition/extraction/',
        'recognize_from_picture': '/recognition/picture/<str:picture>/',
        'recognize_from_video': '/recognition/video/<str:video>/',
        'detections': '/detections/'
    }
    return Response(api_urls)


@api_view(['GET'])
def extraction_training(request):
    configuration = Configuration()
    recognition = Recognition(configuration)
    recognition.extract()
    recognition.train()
    return HttpResponse("SUCCESS")


@api_view(['GET'])
def recognize_from_picture(request, picture):
    configuration = Configuration()
    recognition = Recognition(configuration)
    recognition.recognize(picture)
    return HttpResponse("SUCCESS")


@api_view(['GET'])
def recognize_from_video(request, video):
    configuration = Configuration()
    recognition = Recognition(configuration)
    recognition.recognize_from_video(video)
    return HttpResponse("SUCCESS")


@api_view(['GET', ])
def get_all(request):
    if request.method == "GET":
        detections = Detection.objects.all()
        serializer = DetectionSerializer(instance=detections, many=True)
    return Response(data=serializer.data, status=status.HTTP_200_OK)
