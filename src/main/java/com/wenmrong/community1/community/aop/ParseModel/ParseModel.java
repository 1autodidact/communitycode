package com.wenmrong.community1.community.aop.ParseModel;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
public interface ParseModel<Param> {

    Param doConvert(Param param);
}
