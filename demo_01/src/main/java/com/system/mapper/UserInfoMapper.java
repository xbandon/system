package com.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-02-10
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    IPage<Map<String, Object>> queryUserInfos(Page page,
                                              @Param("userName") String userName,
                                              @Param("roleCode") Integer roleCode,
                                              @Param("accountStatusCode") Integer accountStatusCode,
                                              @Param("entryTime") String entryTime);

}
