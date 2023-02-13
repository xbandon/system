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
    @TableField("userCode")
    private Integer userCode;

    /**
     * 创建人
     */
    @TableField("insertUser")
    private Integer insertUser;

    /**
     * 创建时间
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
