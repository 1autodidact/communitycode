package com.wenmrong.community1.community.model;

import lombok.Data;

@Data
public class User {
    //id是自增的
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}


