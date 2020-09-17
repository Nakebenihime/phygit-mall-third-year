package org.pds.authenticator;

import org.junit.Test;
import org.pds.security.google2fa.Authenticator;
import org.pds.security.google2fa.TOTP;
import org.pds.security.google2fa.TOTPImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TOTPTest {

    TOTP totp = TOTPImpl.getInstance();
    String sampleSecretKey = "B7HNXZHOKL6NJYT46CGQY4GFWFRYDY3G";
    String hexKey = Authenticator.encodeBase32ToHexString(sampleSecretKey);

    @Test
    public final void testGetOTP_ResultIs6Digits() {
        String otp = totp.getOTP(Authenticator.getStep() ,hexKey);
        try {
            Integer.parseInt(otp);
        } catch (NumberFormatException e) {
            fail("TOTP one-time password must be a digit number");
        }

        assertEquals("TOTP one-time password must be composed of only 6 digits !", otp.length(), 6);
    }
}
