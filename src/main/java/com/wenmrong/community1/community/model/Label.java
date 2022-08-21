package com.wenmrong.community1.community.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 标签
 * </p>
 *
 * @author autodidact
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("label")
public class Label extends Model<Label> {


    /**
     * 标签编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名字
     */
    @TableField("label_name")
    private String labelName;

    /**
     * logo(图片)
     */
    @TableField("logo")
    private String logo;

    /**
     * 逻辑删除(0正常,1删除)
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建用户id
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新用户id
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
