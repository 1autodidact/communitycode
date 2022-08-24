package com.wenmrong.community1.community.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Component
public class JasyptProfile {
    public static String secret;

    public static String getSecret() {
        return JasyptProfile.secret;
    }
    @Value("${jasypt.encryptor.password}")
    public void setSecret(String secret) {
        JasyptProfile.secret = secret;
    }
}
