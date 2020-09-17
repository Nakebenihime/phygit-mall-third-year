from django.urls import path

from api import views

urlpatterns = [
    path('', views.overview, name='api_overview'),
    path('recognition/extraction/', views.extraction_training, name='extraction_training'),
    path('recognition/picture/<str:picture>/', views.recognize_from_picture, name='recognize_from_picture'),
    path('recognition/video/<str:video>/', views.recognize_from_video, name='recognize_from_video'),
    path('detections/', views.get_all, name='get_all')
]
