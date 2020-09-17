package org.pds.configuration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.pds.repository")
public class Config extends AbstractElasticsearchConfiguration {
    @Value("${data.elasticsearch.host}")
    private String host;
    @Value("${data.elasticsearch.port}")
    private int port;
    @Value("${data.elasticsearch.username}")
    private String userName;
    @Value("${data.elasticsearch.password}")
    private String password ;
    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        RestHighLevelClient client = new RestHighLevelClient(builder);

        return client;
    }
    @Bean
    @Override
    public EntityMapper entityMapper() {

        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(
                elasticsearchMappingContext(), new DefaultConversionService());
        entityMapper.setConversions(elasticsearchCustomConversions());

        return entityMapper;
    }

    public static class CustomEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        public CustomEntityMapper() {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.registerModule(new CustomGeoModule());
            objectMapper.registerModule(new JavaTimeModule());
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }

        @Override
        public Map<String, Object> mapObject(Object o) {
            return null;
        }

        @Override
        public <T> T readObject(Map<String, Object> map, Class<T> aClass) {
            return null;
        }
    }


}
