package com.wenmrong.community1.community.aop.annotation;

import com.wenmrong.community1.community.aop.ParseModel.CommonModel;
import com.wenmrong.community1.community.aop.ParseModel.ParseModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface Environment {

    String environment() default "";

}
