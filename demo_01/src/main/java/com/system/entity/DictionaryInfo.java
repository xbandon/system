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
 * @since 2023-03-03
 */
@Getter
@Setter
@TableName("dictionary_info")
public class DictionaryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "keyId", type = IdType.AUTO)
    private Integer keyId;

    /**
     * 字典类型
     */
    @TableField("dicType")
    private String dicType;

    /**
     * 字典类型编码
     */
    @TableField("dicTypeCode")
    private Integer dicTypeCode;

    /**
     * 字典编码
     */
    @TableField("dicCode")
    private Integer dicCode;

    /**
     * 字典内容
     */
    @TableField("dicValue")
    private String dicValue;

    /**
     * 逻辑删除
     */
    @TableField("delFlag")
    private String delFlag;

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
