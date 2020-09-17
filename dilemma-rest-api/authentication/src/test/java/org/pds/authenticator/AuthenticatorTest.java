package org.pds.authenticator;

import org.apache.commons.codec.binary.Base32;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pds.security.google2fa.Authenticator;
import org.pds.security.google2fa.TOTP;
import org.pds.security.google2fa.TOTPImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class AuthenticatorTest {

    private final Logger log = LoggerFactory.getLogger(AuthenticatorTest.class);
    private final String secretKey = Authenticator.generateSecretKey();
    private final String sampleSecretKey = "B7HNXZHOKL6NJYT46CGQY4GFWFRYDY3G";
    private final Base32 base32 = new Base32();
    private final TOTP totp = TOTPImpl.getInstance();
    private final Calendar calendar = Calendar.getInstance();

    @BeforeClass
    public static void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Test
    public final void testGenerateSecretKey_IsEncodedInBase32() {
        assertTrue("Secret key must be encoded in base32", base32.isInAlphabet(secretKey));
    }

    @Test
    public final void testGenerateSecretKey_NumberOfByte_MustBe20() {
        //In base32, 8 char => 5 bytes so 32 chars => 20 bytes
        int numberOfByte = 20;
        assertEquals("Secret key must have 20 bytes", secretKey.length(), numberOfByte*8/5);
    }


    @Test
    public final void testGetTOTPCode_WithCorrectSecretKey_GeneratedCorrectTOTPCode() {
        // tested for the given time May 8th, 2020 at 21:37:00
        log.debug("Calendar.TimeZone : {} ", calendar.getTimeZone());
        calendar.set(2020, Calendar.MAY, 8, 21, 37, 0);
        long step = calendar.getTimeInMillis() / 30000;
        String hexKey = Authenticator.encodeBase32ToHexString(sampleSecretKey);
        assertTrue("TOTP algorithm is not correct", Authenticator.validate(step, hexKey, "331522", totp));
    }

    @Test
    public final void testGetTOTPCode_WithWrongSecretKey_GeneratedWrongTOTPCode() {
        // tested for the given time May 8th, 2020 at 21:37:00
        calendar.set(2020, Calendar.MAY, 8, 21, 37, 0);
        long step = calendar.getTimeInMillis() / 30000;
        String wrongSecretKey = sampleSecretKey.replace('7', '5');
        String hexKey = Authenticator.encodeBase32ToHexString(wrongSecretKey);
        assertFalse("TOTP algorithm is not correct", Authenticator.validate(step, hexKey, "331522", totp));
    }

}
