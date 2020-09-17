from rest_framework import serializers

from api.models import Detection


class DetectionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Detection
        fields = '__all__'
