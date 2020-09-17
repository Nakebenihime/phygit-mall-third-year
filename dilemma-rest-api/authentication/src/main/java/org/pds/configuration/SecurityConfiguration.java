package org.pds.configuration;

import lombok.extern.slf4j.Slf4j;
import org.pds.security.cryptography.AES;
import org.pds.security.cryptography.CustomKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SecurityConfiguration {

    @Autowired
    private CustomKeyStore customKeyStore;

    //Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.
    //Init the keyStore and the encryption/decryption algorithm
    @Bean
    CommandLineRunner initAES() {
        return args -> {
            customKeyStore.init();
            AES.getInstance().init(customKeyStore);
        };
    }
}

