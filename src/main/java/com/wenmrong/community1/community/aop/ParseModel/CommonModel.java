package com.wenmrong.community1.community.aop.ParseModel;

import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.exception.CustomizeException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.wenmrong.community1.community.exception.CustomizeErrorCode.SYS_ERROR;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public class CommonModel implements  ParseModel {


    @Override
    public Object doConvert(Object o) {
        JoinPoint point = (JoinPoint) o;

        Class[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
        Object[] args = point.getArgs();

        Optional<Object> validParam = Arrays.asList(args).stream().filter(item -> item.getClass().equals(QuestionDTO.class)).findFirst();
        if (validParam.isPresent()) {
            QuestionDTO questionDTO = (QuestionDTO) validParam.get();
            return questionDTO.getUser().getId();
        }
        throw new CustomizeException(SYS_ERROR);
    }
}
