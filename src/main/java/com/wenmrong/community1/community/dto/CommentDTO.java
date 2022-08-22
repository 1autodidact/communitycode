package com.wenmrong.community1.community.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.wenmrong.community1.community.config.CustomSerializerAndDeserializer;
import com.wenmrong.community1.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    @JSONField(serializeUsing = CustomSerializerAndDeserializer.class)
    private Long gmtCreate;
    @JSONField(serializeUsing = CustomSerializerAndDeserializer.class)
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
