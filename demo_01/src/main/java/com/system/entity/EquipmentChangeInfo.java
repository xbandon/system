package com.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2023-02-14
 */
@Getter
@Setter
@TableName("equipment_change_info")
public class EquipmentChangeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "keyId", type = IdType.AUTO)
    private Integer keyId;

    /**
     * 原设备名称
     */
    @TableField("srcEquipmentName")
    private String srcEquipmentName;

    /**
     * 更换设备名称
     */
    @TableField("equipmentName")
    private String equipmentName;

    /**
     * 原设备型号
     */
    @TableField("srcEquipmentType")
    private String srcEquipmentType;

    /**
     * 更换设备型号
     */
    @TableField("equipmentType")
    private String equipmentType;

    /**
     * 申请人编号
     */
    @TableField("applyUserCode")
    private Integer applyUserCode;

    /**
     * 申请原因
     */
    @TableField("applyReason")
    private String applyReason;

    /**
     * 申请时间
     */
    @TableField("applyTime")
    private Date applyTime;

    /**
     * 审批状态码
     */
    @TableField("approvalStatusCode")
    private Integer approvalStatusCode;

    /**
     * 审批人
     */
    @TableField("approvalUserCode")
    private Integer approvalUserCode;

    /**
     * 审批未通过备注
     */
    @TableField("approvalLog")
    private String approvalLog;

    /**
     * 审批时间
     */
    @TableField("approvalTime")
    private Date approvalTime;

    /**
     * 接收状态码
     */
    @TableField("receiveStatusCode")
    private Integer receiveStatusCode;

    /**
     * 接收时间
     */
    @TableField("receiveTime")
    private Date receiveTime;


}
