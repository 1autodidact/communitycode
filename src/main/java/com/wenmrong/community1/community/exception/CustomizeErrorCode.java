package com.wenmrong.community1.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    //构造方法可以拿到对应的参数
    QUESTION_NOT_FOUND(2001,"Your question is null"),
    TARGET_PARAM_NOT_FOUND(2002,"Please select questions or answers answer "),
    NO_LOGIN(2003,"Not log in"),
    SYS_ERROR(2004,"Server overload,please try again"),
    TYPE_PARAM_WRONG(2005,"Comment type error"),
    COMMENT_NOT_FOUND(2006,"Comment not found");
    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message){
        this.message = message;
        this.code = code;
   }


}
