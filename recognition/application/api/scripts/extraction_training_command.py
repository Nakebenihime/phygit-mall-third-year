import logging
import os

from api.configuration.configuration import Configuration
from api.recognition import Recognition

# ..\manage.py runscript extraction_training_command


def run():
    configuration = Configuration()

    logging.info("OUTPUT PICKLE FILES")
    logging.info(configuration.get_embeddings())
    logging.info(configuration.get_trained_model())
    logging.info(configuration.get_label_encoder())

    logging.info("OPENCV MODELS & PROTOTYPES")
    logging.info(configuration.get_opencv_deep_learning_prototype())
    logging.info(configuration.get_opencv_deep_learning_model())
    logging.info(configuration.get_opencv_embedding_model())

    logging.info("GENERAL PARAMETERS")
    logging.info(configuration.get_dataset())
    logging.info(configuration.get_confidence_score())

    logging.info("CONSTRUCTION BLOB FROM IMAGE PARAMETERS")
    logging.info(configuration.get_blob_from_image_scalefactor())
    logging.info(configuration.get_blob_from_image_size())
    logging.info(configuration.get_blob_from_image_mean_r())
    logging.info(configuration.get_blob_from_image_mean_g())
    logging.info(configuration.get_blob_from_image_mean_b())
    logging.info(configuration.get_blob_from_image_swapRB())
    logging.info(configuration.get_blob_from_image_crop())

    logging.info("CONSTRUCTION BLOB FROM FACE PARAMETERS")
    logging.info(configuration.get_blob_from_face_scalefactor())
    logging.info(configuration.get_blob_from_face_size())
    logging.info(configuration.get_blob_from_face_mean_r())
    logging.info(configuration.get_blob_from_face_mean_g())
    logging.info(configuration.get_blob_from_face_mean_b())
    logging.info(configuration.get_blob_from_face_swapRB())
    logging.info(configuration.get_blob_from_face_crop())

    logging.info("SVC PARAMETERS")
    logging.info(configuration.get_svc_c())
    logging.info(configuration.get_svc_kernel())
    logging.info(configuration.get_svc_probability())

    logging.info("EXTRACTION AND TRAINING STARTED ...")
    recognition = Recognition(configuration)

    if os.path.exists("data/output/embeddings.pickle") is False:
        logging.info(" **** EXTRACTION STARTED **** ")
        recognition.extract()
        logging.info(" **** TRAINING STARTED **** ")
        recognition.train()
