package com.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2023-02-10
 */
@Getter
@Setter
@TableName("equipment_apply_info")
public class EquipmentApplyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "keyId", type = IdType.AUTO)
    private Integer keyId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备型号
     */
    private String equipmentType;

    /**
     * 申请人编号
     */
    private Integer applyUserCode;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 审批状态码
     */
    private Integer approvalStatusCode;

    /**
     * 审批人
     */
    private Integer approvalUserCode;

    /**
     * 审批未通过备注
     */
    private String approvalLog;

    /**
     * 审批时间
     */
    private Date approvalTime;

    /**
     * 接收状态码
     */
    private Integer receiveStatusCode;

    /**
     * 接收时间
     */
    private Date receiveTime;


}
