package org.pds.security.google2fa;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;

public class Authenticator {

    /**
     *
     * @return 20 bytes secret key encoded as base32 string
     */
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    /**
     * @param step - 30 seconds StepSize (ID TOTP)
     * @param key - secret credential key (HEX)
     * @param otp - OTP to validate
     * @param totp implementation of TOTP algorithm
     * @return valid?
     */
    public static boolean validate(final long step, final String key, final String otp, TOTP totp) {
        return totp.getOTP(step, key).equals(otp) || totp.getOTP(step - 1, key).equals(otp);
    }

    /**
     *
     * @return current StepSize based on current time
     */
    public static long getStep() {
        // 30 seconds StepSize (ID TOTP)
        return System.currentTimeMillis() / 30000;
    }

    /**
     *
     * @param secretKey base32 encoded secret key

     * @return hexadecimal character of secretKey
     */
    public static String encodeBase32ToHexString(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        return Hex.encodeHexString(bytes);
    }

}
