package com.wenmrong.community1.community.aop.annotation;

import java.lang.annotation.*;

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
