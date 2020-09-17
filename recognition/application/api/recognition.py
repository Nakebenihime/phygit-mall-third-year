import logging
import math
import os
import pickle

import cv2
import imutils
import requests
import numpy as np
from imutils import paths
from sklearn.preprocessing import LabelEncoder
from sklearn.svm import SVC

from api.serializer import DetectionSerializer

logging.basicConfig(format='%(asctime)s - %(levelname)s: %(message)s', level=logging.DEBUG)


class Recognition:

    def __init__(self, _config_):
        self.embeddings = _config_.get_embeddings()
        self.recognizer = _config_.get_trained_model()
        self.label_encoder = _config_.get_label_encoder()

        self.opencv_embedding_model = _config_.get_opencv_embedding_model()
        self.opencv_deep_learning_prototype = _config_.get_opencv_deep_learning_prototype()
        self.opencv_deep_learning_model = _config_.get_opencv_deep_learning_model()

        self.images_path = _config_.get_images_path()
        self.videos_path = _config_.get_videos_path()
        self.dataset = _config_.get_dataset()
        self.confidence_score = _config_.get_confidence_score()

        self.blob_from_image_scalefactor = _config_.get_blob_from_image_scalefactor()
        self.blob_from_image_size = _config_.get_blob_from_image_size()
        self.blob_from_image_mean_r = _config_.get_blob_from_image_mean_r()
        self.blob_from_image_mean_g = _config_.get_blob_from_image_mean_g()
        self.blob_from_image_mean_b = _config_.get_blob_from_image_mean_b()
        self.blob_from_image_swapRB = _config_.get_blob_from_image_swapRB()
        self.blob_from_image_crop = _config_.get_blob_from_image_crop()

        self.blob_from_face_scalefactor = _config_.get_blob_from_face_scalefactor()
        self.blob_from_face_scalefactor_divisor = _config_.get_blob_from_face_scalefactor_divisor()
        self.blob_from_face_size = _config_.get_blob_from_face_size()
        self.blob_from_face_mean_r = _config_.get_blob_from_face_mean_r()
        self.blob_from_face_mean_g = _config_.get_blob_from_face_mean_g()
        self.blob_from_face_mean_b = _config_.get_blob_from_face_mean_b()
        self.blob_from_face_swapRB = _config_.get_blob_from_face_swapRB()
        self.blob_from_face_crop = _config_.get_blob_from_face_crop()

        self.svc_hyper_parameter = _config_.get_svc_c()
        self.svc_kernel = _config_.get_svc_kernel()
        self.svc_probability = _config_.get_svc_probability()

        # Loading of serialized face detector
        logging.info("Loading face detector...")
        self.detector = cv2.dnn.readNetFromCaffe(self.opencv_deep_learning_prototype, self.opencv_deep_learning_model)

        # Loading of serialized face embedding model
        logging.info("Loading face recognizer...")
        self.embedder = cv2.dnn.readNetFromTorch(self.opencv_embedding_model)

    def extract(self):
        # Put paths to images in dataset
        logging.info("Quantifying faces...")
        imagePaths = list(paths.list_images(self.dataset))

        # Initialization of lists of extracted facial embeddings and corresponding people names
        knownEmbeddings = []
        knownNames = []

        # Initialization of the total number of faces processed
        total = 0

        # Loop on images paths
        for (i, imagePath) in enumerate(imagePaths):
            # Extraction of the person name from the image path
            logging.info("processing image {}/{}".format(i + 1, len(imagePaths)))
            # If path is /dataset/tyrion/tyrion1 --> return tyrion
            name = imagePath.split(os.path.sep)[-2]

            # Loading of the image and resizing to make it have a width of 600 pixels (while
            # maintaining the aspect ratio), and then get the image dimensions
            image = cv2.imread(imagePath)
            image = imutils.resize(image, width=600)
            (h, w) = image.shape[:2]

            # Construction of a blob from the image
            imageBlob = self.blob_construction_from_raw_image(image)

            # Apply OpenCV's deep learning-based face detector to localize faces in the input image
            self.detector.setInput(imageBlob)
            detections = self.detector.forward()
            print("Face(s) found : ", len(detections))

            # To make sure that at least one face was found
            if len(detections) > 0:
                # Assumption : each image has only ONE face
                # So find the bounding box with the largest probability
                i = np.argmax(detections[0, 0, :, 2])
                (vec, startX, startY, endX, endY) = self.detection_loop(detections=detections, i=i, w=w, h=h,
                                                                        image=image)

                # Add the name of the person + corresponding face
                # embedding to their respective lists
                knownNames.append(name)
                knownEmbeddings.append(vec.flatten())
                total += 1

        # Dump the facial embeddings + names to disk
        logging.info("serializing {} encodings...".format(total))
        data = {"embeddings": knownEmbeddings, "names": knownNames}
        print(data)
        f = open(self.embeddings, "wb")
        f.write(pickle.dumps(data))
        f.close()
        return knownNames, knownEmbeddings

    def train(self):
        # Loading of the face embeddings created in extract_embeddings.py
        logging.info("loading face embeddings...")
        data = pickle.loads(open(self.embeddings, "rb").read())

        # Encoding of the labels
        logging.info("encoding labels...")
        le = LabelEncoder()
        labels = le.fit_transform(data["names"])

        # Training of the model used to read the 128-d embeddings and
        # then produce face recognition
        logging.info("training model...")
        recognizer = SVC(C=self.svc_hyper_parameter, kernel=self.svc_kernel, probability=self.svc_probability)
        recognizer.fit(data["embeddings"], labels)

        # Store the face recognition model (creates the recognizer.pickle file)
        f = open(self.recognizer, "wb")
        f.write(pickle.dumps(recognizer))
        f.close()

        # Store the label encoder (creates the le.pickle file)
        f = open(self.label_encoder, "wb")
        f.write(pickle.dumps(le))
        f.close()

    def recognize(self, frame):

        # Loading of the actual face recognition model along with the label encoder (the SVM model we trained)
        recognizer = pickle.loads(open(self.recognizer, "rb").read())
        le = pickle.loads(open(self.label_encoder, "rb").read())

        # Loading of the image into memory and resizing to make it have a width of 600 pixels (while
        # maintaining the aspect ratio), and then get the image dimensions
        image = cv2.imread(self.images_path + "/" + frame)
        image = imutils.resize(image, width=600)
        (h, w) = image.shape[:2]

        # Construction of a blob from the image
        imageBlob = self.blob_construction_from_raw_image(image)

        # Applying the OpenCV's deep learning-based face detector to localize
        # faces in the input image
        self.detector.setInput(imageBlob)
        detections = self.detector.forward()

        name = "unknown"
        probability = 1

        # Loop over the detections
        for i in range(0, detections.shape[2]):
            tuple = self.detection_loop(detections=detections, i=i, w=w, h=h, image=image)

            if tuple != None:
                (vec, startX, startY, endX, endY) = tuple

                # Classification to recognize the face
                preds = recognizer.predict_proba(vec)[0]
                j = np.argmax(preds)
                proba = preds[j]

                if proba >= self.confidence_score:
                    name = le.classes_[j]
                    probability = proba
                    # Drawing of the bounding box of the face along with the associated
                    # probability
                    self.draw(startY, startX, endY, endX, name, probability, image)
                    logging.info("The image contains the face of : %s -- > probability : %s", name, probability)
                    self.send_to_api(name)
                else:
                    self.draw(startY, startX, endY, endX, name, probability, image)
                    logging.info("The image contains the face of : %s -- > probability : %s", name, probability)

        # Display the output image
        # cv2.imshow("Image", image)
        # if cv2.waitKey(0) == ord('a'):
        # pass
        return name, probability

    def blob_construction_from_raw_image(self, image):
        blob = cv2.dnn.blobFromImage(
            image=cv2.resize(image, (300, 300)),
            scalefactor=self.blob_from_image_scalefactor,
            size=(self.blob_from_image_size, self.blob_from_image_size),
            mean=(self.blob_from_image_mean_r, self.blob_from_image_mean_g, self.blob_from_image_mean_b),
            swapRB=self.blob_from_image_swapRB,
            crop=self.blob_from_image_crop)
        return blob

    def blob_construction_from_roi_face(self, face):
        blob = cv2.dnn.blobFromImage(
            image=face,
            scalefactor=self.blob_from_face_scalefactor / self.blob_from_face_scalefactor_divisor,
            size=(self.blob_from_face_size, self.blob_from_face_size),
            mean=(self.blob_from_face_mean_r, self.blob_from_face_mean_g, self.blob_from_face_mean_b),
            swapRB=self.blob_from_face_swapRB,
            crop=self.blob_from_face_crop)
        return blob

    def send_to_api(self, name):
        payload = {"user": name, "detector": "advertisement board"}
        try:
            response = requests.post("http://192.168.0.135:8282/api/v1/events/recognized", json=payload)
            if response.status_code == 200:
                logging.info("Payload was successfully sent to API")
            else:
                self.save_into_embedded_db(payload)
        except requests.exceptions.ConnectionError as e:
            response = "Connection Error"
        if response == "Connection Error":
            logging.info("API was unreachable")
            self.save_into_embedded_db(payload)
        return response

    def detection_loop(self, detections, i, w, h, image):
        # Extraction of the confidence (probability) of each detection
        # associated with the prediction
        confidence = detections[0, 0, i, 2]

        # Filter out weak detections
        if confidence > self.confidence_score:
            # (x, y)-coordinates of the bounding box for the face
            box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
            (startX, startY, endX, endY) = box.astype("int")

            # Extraction of the face ROI
            face = image[startY:endY, startX:endX]
            (fH, fW) = face.shape[:2]

            # Ensure the face width and height are sufficiently large
            if fW < 20 or fH < 20:
                return
            # Construction of a blob for the face ROI, passing the blob
            # through our face embedding model to obtain the 128-d
            # quantification of the face
            blob = self.blob_construction_from_roi_face(face)

            self.embedder.setInput(blob)
            vec = self.embedder.forward()
            return vec, startX, startY, endX, endY

    def draw(self, startY, startX, endY, endX, name, probability, image):
        text = "{}: {:.2f}%".format(name, probability * 100)
        y = startY - 10 if startY - 10 > 10 else startY + 10
        cv2.rectangle(image, (startX, startY), (endX, endY), (0, 0, 255), 2)
        cv2.putText(image, text, (startX, y), cv2.FONT_HERSHEY_SIMPLEX, 0.45, (0, 0, 255), 2)

    def save_into_embedded_db(self, payload):
        logging.info("Caching payload in embedded database...")
        serializer = DetectionSerializer(data=payload)
        if serializer.is_valid():
            serializer.create(payload)
            logging.info("Payload was successfully saved")
            return "success"
        logging.info("Payload wasn't saved, error occurred...")
        return "failure"

    def recognize_from_video(self, path_to_video):
        video = cv2.VideoCapture(self.videos_path + "/" + path_to_video)
        total_frames = int(video.get(cv2.CAP_PROP_FRAME_COUNT))
        frames_per_second = video.get(cv2.CAP_PROP_FPS)
        frames_per_second_int = math.modf(frames_per_second)[1]
        i = 0
        frames = []
        while (video.isOpened()):
            ret, frame = video.read()
            if ret == False:
                break
            if i % frames_per_second_int == 0:
                cv2.imwrite(self.images_path + '/frame' + str(i) + '.jpg', frame)
            i = i + 1
        video.release()
        cv2.destroyAllWindows()

        _frame = 0
        for r, d, f in os.walk(self.images_path):
            for frame in f:
                if 'frame' in frame:
                    print("At frame " + str(_frame) + " : ")
                    self.recognize(frame)
                    os.remove(self.images_path + "/" + str(frame))
                    _frame += 1

    def recognize_from_stream(self, stream_url):
        video = cv2.VideoCapture(stream_url)
        i = 0
        while True:
            ret, frame = video.read()
            if ret:
                name = 'frame' + str(i) + '.jpg'
                cv2.imwrite(self.images_path + "/" + name, frame)
                print("At frame " + str(i) + " : ")
                self.recognize(name)
                os.remove(self.images_path + "/" + name)
                i += 1
        video.release()
        cv2.destroyAllWindows()