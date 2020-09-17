package org.pds.authenticator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.pds.security.cryptography.AES;
import org.pds.security.cryptography.CustomKeyStore;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class AESTest {

    private static AES aes;

    private final String hexadecimalKey = "0fcedbe4ee52fcd4e27cf08d0c70c5b16381e366";

    @BeforeClass
    public static void setUp() throws NoSuchAlgorithmException {
        //creating a KeyGenerator instance
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        //initializing the KeyGenerator
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(128, secureRandom);

        //generating a key
        Key secretKey = keyGenerator.generateKey();

        //create the mock
        CustomKeyStore mockedCustomKeyStore = Mockito.mock(CustomKeyStore.class);
        Mockito.when(mockedCustomKeyStore.getSecretKeyAES()).thenReturn(secretKey);

        //init AES instance with the mock
        aes = AES.getInstance();
        aes.init(mockedCustomKeyStore);
    }

    @Test
    public final void whenEncryptWithAES_ThenDecryption_ThenKeyIsCorrect() throws DecoderException {
        //encryption
        String encryptedKey = aes.encryptionHexString(hexadecimalKey);
        //decryption
        String decryptedKey = aes.decryptionHexString(encryptedKey);

        assertEquals("Message is not the same before and after encryption/decryption", decryptedKey, hexadecimalKey);
    }

    @Test
    public final void whenEncryptWithAES_WithoutDecryption_ThenKeyIsNotCorrect() throws DecoderException {
        //encryption
        String encryptedKey = aes.encryptionHexString(hexadecimalKey);

        assertNotEquals("Message and encrytion of message must not be equals", encryptedKey, hexadecimalKey);
    }

}
