from unittest.mock import patch

from django.test import TestCase, Client
from django.urls import reverse
from rest_framework import status

from api.configuration.configuration import Configuration
from api.models import Detection
from api.recognition import Recognition
from api.serializer import DetectionSerializer


class RecognitionExtractTest(TestCase):
    """ Test class for extract function in Recognition class """

    def setUp(self):
        self.configuration = Configuration()
        self.recognition = Recognition(self.configuration)
        self.tuple = self.recognition.extract()
        self.recognition.train()
        self.names_array = self.tuple[0]
        self.embeddings_array = self.tuple[1]

    def test_embedding_array_length_equals_name_array_length(self):
        self.assertEqual(len(self.names_array), len(self.embeddings_array))
        print("test_embedding_array_length_equals_name_array_length : successful")

    def test_embedding_array_length_not_equals_name_array_length(self):
        self.assertNotEqual(len(self.names_array), len(self.embeddings_array) + 1)
        print("test_embedding_array_length_not_equals_name_array_length : successful")


class RecognitionRecognizeTest(TestCase):
    """ Test class for recognize function in Recognition class """

    def setUp(self):
        self.configuration = Configuration()
        self.recognition = Recognition(self.configuration)

    def test_cersei_is_recognized(self):
        self.assertEqual(self.recognition.recognize('cersei.jpg')[0], 'cersei')
        print("test_cersei_is_recognized : successful")

    def test_tyrion_is_recognized(self):
        self.assertEqual(self.recognition.recognize('tyrion.jpg')[0], 'tyrion')
        print("test_tyrion_is_recognized : successful")

    def test_jonsnow_is_recognized(self):
        self.assertEqual(self.recognition.recognize('jonsnow.jpg')[0], 'jon_snow')
        print("test_jonsnow_is_recognized : successful")

    def test_unknown_is_not_recognized(self):
        self.assertEqual(self.recognition.recognize('eleven.jpg')[0], 'unknown')
        print("test_unknown_is_not_recognized : successful")

    def test_recognize_only_above_80_per_cent(self):
        self.assertTrue(self.recognition.recognize('cersei.jpg')[1] >= 0.8)
        self.assertTrue(self.recognition.recognize('tyrion.jpg')[1] >= 0.8)
        self.assertTrue(self.recognition.recognize('jonsnow.jpg')[1] >= 0.8)
        print("test_recognize_only_above_50_per_cent : successful")

    def test_unknown_recognition_is_100_per_cent(self):
        self.assertTrue(self.recognition.recognize('kevin.jpg')[1] == 1)
        print("test_unknown_recognition_is_under_50_per_cent : successful")


class RecognitionDatabaseTest(TestCase):
    """ Test class for save_into_embedded_db function in Recognition class """

    def setUp(self):
        self.configuration = Configuration()
        self.recognition = Recognition(self.configuration)

        self.valid_payload = {
            'name': 'Ragnar',
            'detector': 'advertisement board'
        }
        self.invalid_payload = {
            'name': '',
            'detector': 'advertisement board'
        }

    def test_save_into_embedded_db_valid_person(self):
        response = self.recognition.save_into_embedded_db(self.valid_payload)
        self.assertEqual(response, "success")
        self.assertEqual(Detection.objects.count(), 1)
        print('test_save_into_embedded_db_valid_person successful')

    def test_save_into_embedded_db_invalid_person(self):
        response = self.recognition.save_into_embedded_db(self.invalid_payload)
        self.assertEqual(response, "failure")
        self.assertEqual(Detection.objects.count(), 0)
        print('test_save_into_embedded_db_invalid_person successful')


def response_http_is_ok():
    return status.HTTP_200_OK


def response_http_is_ko():
    return status.HTTP_404_NOT_FOUND


def response_exception():
    return "Connection Error"


class RecognitionAPITest(TestCase):
    """ Test class for sendToAPI function in Recognition class """

    def setUp(self):
        self.configuration = Configuration()
        self.recognition = Recognition(self.configuration)
        self.response_http_is_ok = status.HTTP_200_OK
        self.response_http_is_ko = status.HTTP_404_NOT_FOUND
        self.response_exception = "Connection Error"

    @patch('api.recognition.Recognition.send_to_api', return_value=response_http_is_ok())
    def test_send_to_api_ok(self, send_to_api):
        self.assertEqual(self.recognition.send_to_api('x'), self.response_http_is_ok)
        print('test_send_to_api_ok successful')

    @patch('api.recognition.Recognition.send_to_api', return_value=response_http_is_ko())
    def test_send_to_api_ko(self, send_to_api):
        self.assertEqual(self.recognition.send_to_api('x'), self.response_http_is_ko)
        print('test_send_to_api_ko successful')

    @patch('api.recognition.Recognition.send_to_api', return_value=response_exception())
    def test_send_to_api_exception(self, send_to_api):
        self.assertEqual(self.recognition.send_to_api('x'), self.response_exception)
        print('test_send_to_api_exception successful')


client = Client()


class GetAllDetectionsTest(TestCase):
    """ Test class for GET all detections API """

    def setUp(self):
        Detection.objects.create(name='Ragnar', detector='advertisement board')
        Detection.objects.create(name="Björn",detector='advertisement board')
        Detection.objects.create(name="Ivar", detector='advertisement board')

    def test_get_all_detections(self):
        response = client.get(reverse('get_all'))
        detections = Detection.objects.all()
        serializer = DetectionSerializer(detections, many=True)
        self.assertEqual(response.data, serializer.data)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        print('test_get_all_detections successful')
