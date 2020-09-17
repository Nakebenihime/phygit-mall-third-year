#!/bin/bash

python manage.py runscript extraction_training_command &&
python manage.py makemigrations &&
python manage.py migrate &&
python manage.py runserver 0.0.0.0:8000 --noreload