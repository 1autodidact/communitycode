package com.wenmrong.community1.community.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.wenmrong.community1.community.config.CustomSerializerAndDeserializer;
import com.wenmrong.community1.community.model.UserLike;
import com.wenmrong.community1.community.model.User;
import lombok.Data;

import java.util.List;

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
    private String userId;
    @JSONField(serializeUsing = CustomSerializerAndDeserializer.class)
    private Long gmtCreate;
    @JSONField(serializeUsing = CustomSerializerAndDeserializer.class)
    private Long gmtModified;
    private UserDto user;
    private TagDTO tagDTO;
    private Object file;
    private String html;
    private boolean isLike = false;
    private boolean isFollow = false;
    private List<Integer> labelIds;
    private UserLike userUserLikeInfo;

}
