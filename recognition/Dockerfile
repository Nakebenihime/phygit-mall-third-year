FROM python:3.7
MAINTAINER mario-dacosta@hotmail.fr

# set environment variables
ENV PYTHONUNBUFFERED 1
ENV PYTHONDONTWRITEBYTECODE 1

# create root directory
RUN mkdir /application

# set work directory
WORKDIR /application
# add directories
ADD ./application /application/

RUN ls -l

# install dependencies
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

EXPOSE 8585

RUN chmod +x /application/entrypoint.sh
ENTRYPOINT ["/application/entrypoint.sh"]

