package com.wenmrong.community1.community.utils;

import com.wenmrong.community1.community.config.JasyptProfile;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * Jasypt 加密工具
 *
 * @author likai
 * @Date 2022/2/14 17:59
 */
public class SecretUtil {
    public static final String ALGORITHM = "PBEWithMD5AndDES";


    public static String encrypt(String plainText){
        StandardPBEStringEncryptor standardPBEStringEncryptor = secret();
       return standardPBEStringEncryptor.encrypt(plainText);

    }

    public static String decrypt(String encryptedText){
        StandardPBEStringEncryptor standardPBEStringEncryptor = secret();
        return standardPBEStringEncryptor.decrypt(encryptedText);
    }


    private static StandardPBEStringEncryptor secret() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm(ALGORITHM);
        // 加解密的密钥
        config.setPassword(JasyptProfile.getSecret());
        standardPBEStringEncryptor.setConfig(config);
        return standardPBEStringEncryptor;
    }
}