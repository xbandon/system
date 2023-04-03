package com.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.EquipmentChangeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-02-14
 */
public interface EquipmentChangeInfoMapper extends BaseMapper<EquipmentChangeInfo> {

    IPage<Map<String, Object>> queryChangingInfos(Page page,
                                                  @Param("userName") String userName,
                                                  @Param("equipmentName") String equipmentName,
                                                  @Param("approvalStatusCode") Integer approvalStatusCode);

    IPage<Map<String, Object>> queryChangedSucInfos(Page page,
                                                    @Param("applyUser") String applyUser,
                                                    @Param("approvalUser") String approvalUser,
                                                    @Param("equipmentName") String equipmentName,
                                                    @Param("equipmentType") String equipmentType,
                                                    @Param("receiveStatusCode") Integer receiveStatusCode,
                                                    @Param("approvalStatusCode") Integer approvalStatusCode);

    IPage<Map<String, Object>> queryChangedErrInfos(Page page,
                                                    @Param("applyUser") String applyUser,
                                                    @Param("approvalUser") String approvalUser,
                                                    @Param("equipmentName") String equipmentName,
                                                    @Param("approvalStatusCode") Integer approvalStatusCode);

    IPage<Map<String, Object>> queryChangeRecords(Page page,
                                                  @Param("equipmentName") String equipmentName,
                                                  @Param("approvalStatusCode") Integer approvalStatusCode,
                                                  @Param("receiveStatusCode") Integer receiveStatusCode,
                                                  @Param("loginUserCode") Integer loginUserCode);
}
