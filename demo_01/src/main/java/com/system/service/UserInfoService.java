package com.system.service;

import com.system.entity.LoginUserInfo;
import com.system.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2023-02-10
 */
public interface UserInfoService extends IService<UserInfo> {

    LoginUserInfo login(LoginUserInfo loginUserInfo);

}
