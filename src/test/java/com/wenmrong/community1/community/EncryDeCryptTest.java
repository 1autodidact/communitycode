package com.wenmrong.community1.community;

import com.wenmrong.community1.community.utils.SecretUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EncryDeCryptTest {
    @Test
    public void testEncrypt() throws Exception {
        String encrypt = SecretUtil.encrypt("redis12345");
        String decrypt = SecretUtil.decrypt(encrypt);
        System.out.println("aaa");
    }



}
