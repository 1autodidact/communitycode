package com.wenmrong.community1.community.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * <p>
 *  用户关注表
 * </p>
 *
 * @author autodidact
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_follow")
public class UserFollow extends Model<UserFollow> {


    @TableField("user_id")
    private Long userId;

    @TableField("follow_id")
    private Long followId;

    @TableId(type = ASSIGN_ID)
    private Long id;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
