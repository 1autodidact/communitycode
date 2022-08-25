package com.wenmrong.community1.community;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public class TestConvert {
    @Test
    public void testConvertTime() throws ParseException {
       SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = "1661146635210";
        String s = String.valueOf(1661146635210l);
        long l = Long.parseLong(s);
        String format = smf.format(l);
        System.out.println("aaa");


    }
}
