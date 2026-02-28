package com.farmconnect.krishisetu.CommonUtility.Util;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

import org.apache.commons.codec.digest.DigestUtils;

@NoArgsConstructor
@Component
public class TokenUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateRawToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}


