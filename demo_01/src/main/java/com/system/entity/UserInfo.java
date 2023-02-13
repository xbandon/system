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
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工编号
     */
    @TableId(value = "userCode", type = IdType.AUTO)
    private Integer userCode;

    /**
     * 员工姓名
     */
    @TableField("userName")
    private String userName;

    /**
     * 登录账户名
     */
    @TableField("loginName")
    private String loginName;

    /**
     * 登录密码
     */
    @TableField("loginPassword")
    private String loginPassword;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 电话
     */
    @TableField("telephoneNumber")
    private String telephoneNumber;

    /**
     * 角色码
     */
    @TableField("roleCode")
    private Integer roleCode;

    /**
     * 登录状态码
     */
    @TableField("onlineStatusCode")
    private Integer onlineStatusCode;

    /**
     * 账户状态码
     */
    @TableField("accountStatusCode")
    private Integer accountStatusCode;

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
