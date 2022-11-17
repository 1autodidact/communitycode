package com.wenmrong.community1.community.aop.annotation;

import com.wenmrong.community1.community.aop.ParseModel.CommonModel;
import com.wenmrong.community1.community.aop.ParseModel.ParseModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 系统日志自定义注解
 *
 * @author liuyanzhao
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    /**
     * 日志名称
     *
     * @return
     */
    String description() default "";

    String traceId() default  "";

    Class<? extends ParseModel> model() default CommonModel.class;
}