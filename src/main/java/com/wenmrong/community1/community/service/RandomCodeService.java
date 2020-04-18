package com.wenmrong.community1.community.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomCodeService {
    public String createActiveCode() {
        Random random = new Random();
        int num = random.nextInt(999999);
        String numString = String.valueOf(num);
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(numString);
        for (int i = 0; i < 6 - numString.length(); i++) {
            stringBuffer.append("0");
        }
        return stringBuffer.toString();
    }
}