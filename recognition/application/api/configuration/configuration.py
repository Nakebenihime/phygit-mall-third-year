import yaml


class Configuration(object):

    def __init__(self):
        self.configuration_file = "api/application.yml"
        self.configuration_data = None

        f = open(self.configuration_file, 'r')
        self.configuration_data = yaml.load((f.read()), Loader=yaml.FullLoader)
        f.close()

    """ rabbitmq parameters """

    def get_rabbitmq_host(self):
        return self.configuration_data['rabbitmq']['server']

    def get_rabbitmq_port(self):
        return self.configuration_data['rabbitmq']['port']

    def get_rabbitmq_vhost(self):
        return self.configuration_data['rabbitmq']['credentials']['vhost']

    def get_rabbitmq_password(self):
        return self.configuration_data['rabbitmq']['credentials']['password']

    def get_rabbitmq_username(self):
        return self.configuration_data['rabbitmq']['credentials']['username']

    def get_messaging_exchange(self):
        return self.configuration_data['rabbitmq']['messaging']['exchange']

    def get_messaging_queue(self):
        return self.configuration_data['rabbitmq']['messaging']['queue']

    """ output pickle files """

    def get_embeddings(self):
        return self.configuration_data['parameters']['general']['output']['pickle']['embeddings']

    def get_trained_model(self):
        return self.configuration_data['parameters']['general']['output']['pickle']['recognizer']

    def get_label_encoder(self):
        return self.configuration_data['parameters']['general']['output']['pickle']['label_encoder']

    """ input opencv models & prototypes """

    def get_opencv_deep_learning_prototype(self):
        return self.configuration_data['parameters']['general']['input']['opencv']['prototype']

    def get_opencv_deep_learning_model(self):
        return self.configuration_data['parameters']['general']['input']['opencv']['model']

    def get_opencv_embedding_model(self):
        return self.configuration_data['parameters']['general']['input']['opencv']['embedding_model']

    """ general parameters """

    def get_dataset(self):
        return self.configuration_data['parameters']['general']['dataset']

    def get_confidence_score(self):
        return self.configuration_data['parameters']['general']['confidence']

    def get_images_path(self):
        return self.configuration_data['parameters']['general']['images']

    def get_videos_path(self):
        return self.configuration_data['parameters']['general']['videos']

    """ parameters for blob_from_image function """

    def get_blob_from_image_scalefactor(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['scalefactor']

    def get_blob_from_image_size(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['size']

    def get_blob_from_image_mean_r(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['mean']['r']

    def get_blob_from_image_mean_g(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['mean']['g']

    def get_blob_from_image_mean_b(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['mean']['b']

    def get_blob_from_image_swapRB(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['swapRB']

    def get_blob_from_image_crop(self):
        return self.configuration_data['parameters']['construction']['blob_from_image']['crop']

    """ parameters for blob_from_face function """

    def get_blob_from_face_scalefactor(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['scalefactor']

    def get_blob_from_face_scalefactor_divisor(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['divisor']

    def get_blob_from_face_size(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['size']

    def get_blob_from_face_mean_r(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['mean']['r']

    def get_blob_from_face_mean_g(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['mean']['g']

    def get_blob_from_face_mean_b(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['mean']['b']

    def get_blob_from_face_swapRB(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['swapRB']

    def get_blob_from_face_crop(self):
        return self.configuration_data['parameters']['construction']['blob_from_face']['crop']

    """ parameters for svc function """

    def get_svc_c(self):
        return self.configuration_data['parameters']['svc']['c']

    def get_svc_kernel(self):
        return self.configuration_data['parameters']['svc']['kernel']

    def get_svc_probability(self):
        return self.configuration_data['parameters']['svc']['probability']
