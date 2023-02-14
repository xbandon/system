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
@TableName("equipment_scrap_info")
public class EquipmentScrapInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "keyId", type = IdType.AUTO)
    private Integer keyId;

    /**
     * 设备名称
     */
    @TableField("equipmentName")
    private String equipmentName;

    /**
     * 设备型号
     */
    @TableField("equipmentType")
    private String equipmentType;

    /**
     * 报废人编号
     */
    @TableField("scrapUserCode")
    private Integer scrapUserCode;

    /**
     * 报废时间
     */
    @TableField("scrapTime")
    private Date scrapTime;

    /**
     * 报废记录
     */
    @TableField("scrapLog")
    private String scrapLog;


}
