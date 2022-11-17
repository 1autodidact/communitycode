package com.wenmrong.community1.community.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author autodidact
 * @since 2022-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("nft_order")
public class NftOrder extends Model<NftOrder> {


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    
    @TableField("order_num")
    private String orderNum;

    
    @TableField("pay_order_num")
    private String payOrderNum;

    
    @TableField("order_amount")
    private Double orderAmount;

    
    @TableField("invoice")
    private Integer invoice;

    
    @TableField("invoice_title")
    private String invoiceTitle;

    
    @TableField("order_remark")
    private String orderRemark;

    
    @TableField("address_id")
    private String addressId;

    @TableField("gmt_create")
    private Long gmtCreate;

    @TableField("gmt_modified")
    private Long gmtModified;




}
