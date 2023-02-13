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
    private String dicType;

    /**
     * 字典类型编码
     */
    private Integer dicTypeCode;

    /**
     * 字典编码
     */
    private Integer dicCode;

    /**
     * 字典内容
     */
    private String dicValue;

    /**
     * 创建人
     */
    private Integer insertUser;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新人
     */
    private Integer updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;


}
