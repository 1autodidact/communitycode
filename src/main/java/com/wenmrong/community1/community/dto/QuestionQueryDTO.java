package com.wenmrong.community1.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private String sort;
    private Integer page;
    private Integer size;
    private Long time;


}
