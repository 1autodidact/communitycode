package com.wenmrong.community1.community.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comment")
public class Comment extends Model<Comment> {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.ID
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("id")
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.PARENT_ID
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("parent_id")
    private Long parentId;

    @TableField("question_id")
    private Long questionId;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.TYPE
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("type")
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.COMMENTATOR
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("commentator")
    private Long commentator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.GMT_CREATE
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Long gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.GMT_MODIFIED
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private Long gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.LIKE_COUNT
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("like_count")
    private Long likeCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.CONTENT
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("content")
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.COMMENT_COUNT
     *
     * @mbg.generated Thu May 28 09:25:06 CST 2020
     */
    @TableField("comment_count")
    private Integer commentCount;

}