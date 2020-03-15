package com.wenmrong.community1.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private Long outerid;
    private String notifierName;
    private String outerTitle;
    private String typeName;
    private Integer type;
}
