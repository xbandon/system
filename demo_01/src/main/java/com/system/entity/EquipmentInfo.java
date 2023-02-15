package com.system.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("equipment_info")
public class EquipmentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @TableId(value = "equipmentCode", type = IdType.AUTO)
    private Integer equipmentCode;

    /**
     * 设备型号
     */
    @TableField("equipmentType")
    private String equipmentType;

    /**
     * 设备名称
     */
    @TableField("equipmentName")
    private String equipmentName;

    /**
     * 设备状态码
     */
    @TableField("equipmentStatusCode")
    private Integer equipmentStatusCode;

    /**
     * 使用人编号
     */
    @TableField(value = "userCode",updateStrategy = FieldStrategy.IGNORED)
    private Integer userCode;

    /**
     * 入库人
     */
    @TableField("insertUser")
    private Integer insertUser;

    /**
     * 入库时间
     */
    @TableField("insertTime")
    private Date insertTime;

    /**
     * 更新人
     */
    @TableField("updateUser")
    private Integer updateUser;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;


}
