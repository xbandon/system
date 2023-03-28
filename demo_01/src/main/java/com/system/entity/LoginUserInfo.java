package com.system.entity;

import lombok.Data;

@Data
public class LoginUserInfo {
    private Integer userCode;
    private String userName;
    private String loginName;
    private String loginPassword;
    private String email;
    private String telephoneNumber;
    private Integer roleCode;
    private String token;
}
