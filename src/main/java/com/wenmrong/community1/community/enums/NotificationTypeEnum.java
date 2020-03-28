package com.wenmrong.community1.community.enums;

public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "reply question"),
    REPLY_COMMENT(2, "reply comment"),
    THUMB_COMMENT(3, "thumb comment");
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(int type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }

}
