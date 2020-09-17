package org.pds.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.pds.security.cryptography.AES;
import org.pds.security.google2fa.Authenticator;
import org.pds.security.google2fa.TOTP;
import org.pds.security.google2fa.TOTPImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.TimeZone;

@RestController
@Slf4j
public class AuthenticationController {

    private String mockMongoDatabaseEcnryptedSecretKey;

    @GetMapping("/getSecretKeyForDoubleAuthentication")
    public String generateSecretKeyForDoubleAuthentication() {
        String secretKey = Authenticator.generateSecretKey();
        String hexSecretkey = Authenticator.encodeBase32ToHexString(secretKey);
        try {
            mockMongoDatabaseEcnryptedSecretKey = AES.getInstance().encryptionHexString(hexSecretkey);
        } catch (DecoderException e) {
            log.info(e.toString());
        }
        return secretKey;
    }

    @PostMapping("/login")
    public String login(@RequestBody String otp) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
        String key = null;
        TOTP totp = TOTPImpl.getInstance();
        try {
             key = AES.getInstance().decryptionHexString(mockMongoDatabaseEcnryptedSecretKey);
        } catch (DecoderException e) {
            log.info(e.toString());
        }

        String answer = "";
        boolean bool = Authenticator.validate(Authenticator.getStep(), key, otp, totp);
        if (bool) {
            answer = "True";
        } else {
            answer = "False";
        }

        return answer;
    }

}
