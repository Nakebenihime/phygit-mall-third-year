package org.pds.security.cryptography;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Database that can contain keys
 * stored in a file on disk (waiting for better)
 * protected by a password
 */
@Slf4j
@Component
public class CustomKeyStore {

    private KeyStore keyStore;
    private final String file;
    private final String keyStorePassword;
    private final String secretKeyAESPassword;

    public CustomKeyStore(@Value("${server.ssl.key-store}") String file,
                          @Value("${server.ssl.key-store-password}") String keyStorePassword,
                          @Value("${sever.aes.key-password}") String secretKeyAESPassword) {

        this.file = file;
        this.keyStorePassword = keyStorePassword;
        this.secretKeyAESPassword = secretKeyAESPassword;
    }

    /**
     * init the class
     */
    public void init() {

        //creating the keyStore
        try {
            keyStore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            log.error(e.toString());
        }

        //loading the keyStore
        try (InputStream keyStoreData = new FileInputStream(file);) {
            keyStore.load(keyStoreData, keyStorePassword.toCharArray());
        } catch (FileNotFoundException e) {
            log.error("Key store file not found !");
            log.info("Creating a new key store file...");
            try {
                keyStore.load(null, keyStorePassword.toCharArray());
            } catch (IOException | NoSuchAlgorithmException | CertificateException ioException) {
                log.error("Error at creating new key store file : " + e.getMessage());
            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            log.error("Error at loading key : " + e.getMessage());
        }

        //create key if there is no AES secret key
        try {
            if (!keyStore.containsAlias("AES")) {
                log.info("Creating AES secret key");

                //crating a KeyGenerator instance
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

                //initializing the KeyGenerator
                SecureRandom secureRandom = new SecureRandom();
                keyGenerator.init(128, secureRandom);

                //generating a key
                SecretKey secretKey = keyGenerator.generateKey();

                //setting the key in key store
                KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
                KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(secretKeyAESPassword.toCharArray());
                keyStore.setEntry("AES", secretKeyEntry, entryPassword);

                //storing the key store
                try (FileOutputStream keyStoreOutputStream = new FileOutputStream(file)) {
                    keyStore.store(keyStoreOutputStream, keyStorePassword.toCharArray());
                } catch (FileNotFoundException e) {
                    log.error("Key store file not found !");
                } catch (IOException | CertificateException e) {
                    log.error("Error at storing key : " + e.getMessage());
                }
            }
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            log.error(e.toString());
        }
    }

    /**
     * get the secret key use to encrypt Authenticator's keys
     * @return the Key
     */
    public Key getSecretKeyAES() {
        Key key = null;
        try {
            key = keyStore.getKey("AES", secretKeyAESPassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.error(e.toString());
        }
        return key;
    }
}
