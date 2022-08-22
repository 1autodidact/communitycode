package com.wenmrong.community1.community.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.sun.mail.imap.protocol.ID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * <p>
 * 
 * </p>
 *
 * @author autodidact
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("comment_relation")
public class CommentRelation extends Model<CommentRelation> {


    @TableId(type = ASSIGN_ID)
    private Long  id;

    @TableField("comment_id")
    private Long commentId;

    @TableField("parentId")
    private Long parentId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
