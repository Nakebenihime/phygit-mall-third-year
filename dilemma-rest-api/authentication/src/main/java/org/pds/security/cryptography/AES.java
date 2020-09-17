package org.pds.security.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


/**
 * Class can be used to both encrypt and decrypt data
 * AES => encryption algorithm
 * ECB => block cypher mode of operation
 * PKCS5Padding => padding method : PKCS#5 (RFC 5652)
 */
@Slf4j
public class AES {

    private static final AES INSTANCE = new AES();

    private Cipher cipherEncryptMode;
    private Cipher cipherDecryptMode;

    /**
     * private constructor (singleton)
     */
    private AES() {}

    /**
     * init the class
     */
    public void init(CustomKeyStore customKeyStore) {
        try {
            //get the secret key from the KeyStore
            Key key = customKeyStore.getSecretKeyAES();

            //Initializing a Cipher instance is an expensive operation
            //Therefore, we init all Cypher instances here to reuse it after.
            cipherEncryptMode = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherEncryptMode.init(Cipher.ENCRYPT_MODE, key);

            cipherDecryptMode = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherDecryptMode.init(Cipher.DECRYPT_MODE, key);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Encrypt a hexadecimal String into another hexadecimal String with AES, CBC and PKCS#5
     * @param hexEncodedData Hexadecimal string to encrypt
     * @return A String containing lower-case hexadecimal characters encrypted
     * @throws DecoderException Thrown if an odd number or illegal of characters is supplied
     */
    public final String encryptionHexString(String hexEncodedData) throws DecoderException {

        byte[] data = Hex.decodeHex(hexEncodedData);
        byte[] cipherData = null;
        try {
            cipherData = cipherEncryptMode.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return  Hex.encodeHexString(cipherData);
    }

    /**
     * Decrypt a hexadecimal String into another hexadecimal String with AES, CBC and PKCS#5
     * @param hexEncodedData Hexadecimal string to encrypt
     * @return A String containing lower-case hexadecimal characters encrypted
     * @throws DecoderException Thrown if an odd number or illegal of characters is supplied
     */
    public final String decryptionHexString(String hexEncodedData) throws DecoderException {

        byte[] data = Hex.decodeHex(hexEncodedData);
        byte[] cipherData = null;
        try {
            cipherData = cipherDecryptMode.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return  Hex.encodeHexString(cipherData);
    }

    public static AES getInstance() {
        return INSTANCE;
    }

}
