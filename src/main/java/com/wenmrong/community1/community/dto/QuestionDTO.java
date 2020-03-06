package com.wenmrong.community1.community.dto;

import com.wenmrong.community1.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private User user;


}
