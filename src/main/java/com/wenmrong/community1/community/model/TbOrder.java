package com.wenmrong.community1.community.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author autodidact
 * @since 2022-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_order")
public class TbOrder extends Model<TbOrder> {


    /**
     * 订单id
     */
    @TableId("order_id")
    private String orderId;

    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @TableField("payment")
    private String payment;

    /**
     * 支付类型，1、在线支付，2、货到付款
     */
    @TableField("payment_type")
    private Integer paymentType;

    /**
     * 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @TableField("post_fee")
    private String postFee;

    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
     */
    @TableField("status")
    private Integer status;

    /**
     * 订单创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 订单更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 付款时间
     */
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    @TableField("consign_time")
    private LocalDateTime consignTime;

    /**
     * 交易完成时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 交易关闭时间
     */
    @TableField("close_time")
    private LocalDateTime closeTime;

    /**
     * 物流名称
     */
    @TableField("shipping_name")
    private String shippingName;

    /**
     * 物流单号
     */
    @TableField("shipping_code")
    private String shippingCode;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 买家留言
     */
    @TableField("buyer_message")
    private String buyerMessage;

    /**
     * 买家昵称
     */
    @TableField("buyer_nick")
    private String buyerNick;

    /**
     * 买家是否已经评价
     */
    @TableField("buyer_rate")
    private Integer buyerRate;


    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
