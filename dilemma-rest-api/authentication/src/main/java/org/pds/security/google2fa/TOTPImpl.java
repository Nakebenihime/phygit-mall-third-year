package org.pds.security.google2fa;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Implementation of TOTP - RFC 6283
 *
 * @author thoeger
 * @contact horsten.hoeger@taimos.de
 */
public final class TOTPImpl implements TOTP {

    private TOTPImpl() {
        // private utility class constructor
    }

    private static final TOTPImpl INSTANCE = new TOTPImpl();

    /**
     * singleton pattern
     */
    public static TOTPImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public String getOTP(final long step, final String key) {
        StringBuilder steps = new StringBuilder(Long.toHexString(step).toUpperCase());
        while (steps.length() < 16) {
            steps.insert(0, "0");
        }

        // Get the HEX in a Byte[]
        final byte[] msg = hexStr2Bytes(steps.toString());
        final byte[] k = hexStr2Bytes(key);

        final byte[] hash_sha1 = hmac(k, msg);

        // put selected bytes into result int
        final int offset = hash_sha1[hash_sha1.length - 1] & 0xf;
        final int binary = ((hash_sha1[offset] & 0x7f) << 24) | ((hash_sha1[offset + 1] & 0xff) << 16) | ((hash_sha1[offset + 2] & 0xff) << 8) | (hash_sha1[offset + 3] & 0xff);
        final int otp = binary % 1000000;

        StringBuilder result = new StringBuilder(Integer.toString(otp));
        while (result.length() < 6) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    /**
     * This method converts HEX string to Byte[]
     *
     * @param hex the HEX string
     *
     * @return A byte array
     */
    private byte[] hexStr2Bytes(final String hex) {
        // Adding one byte to get the right conversion
        // values starting with "0" can be converted
        final byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        final byte[] ret = new byte[bArray.length - 1];

        // Copy all the REAL bytes, not the "first"
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a Hashed Message Authentication Code with the crypto hash
     * algorithm as a parameter.
     * @param keyBytes the bytes to use for the HMAC key
     * @param text the message or text to be authenticated.
     */
    private byte[] hmac(final byte[] keyBytes, final byte[] text) {
        try {
            //the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
            final Mac hmac = Mac.getInstance("HmacSHA1");
            final SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (final GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

}