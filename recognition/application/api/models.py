from django.db import models


# Create your models here.

class Detection(models.Model):
    name = models.CharField(max_length=10)
    detector = models.CharField(max_length=30)
    created = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return "{} - {}".format(self.name, self.detector, self.created)
