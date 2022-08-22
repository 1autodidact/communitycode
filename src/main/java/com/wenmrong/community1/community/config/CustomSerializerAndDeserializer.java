package com.wenmrong.community1.community.config;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CustomSerializerAndDeserializer implements ObjectSerializer, ObjectDeserializer {
    private static ThreadLocal<DateFormat> formatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 序列化
     *
     * 程序给前端响应时， 就会将数据序列化为二进制流，进行传输
     */
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = serializer.out;

        try {
            String s = String.valueOf(object);
            long timeStamp = Long.parseLong(s);
            out.write(formatter.get().format(timeStamp));
        }catch (Exception e){
            out.write(String.valueOf(object));
        }

    }


    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return null;
    }
}