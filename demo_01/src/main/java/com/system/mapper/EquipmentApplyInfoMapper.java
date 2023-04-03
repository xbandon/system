package com.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.EquipmentApplyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-02-10
 */
public interface EquipmentApplyInfoMapper extends BaseMapper<EquipmentApplyInfo> {

    IPage<Map<String, Object>> queryApprovingInfos(Page page,
                                                   @Param("userName") String userName,
                                                   @Param("equipmentName") String equipmentName,
                                                   @Param("approvalStatusCode") Integer approvalStatusCode);

    IPage<Map<String, Object>> queryApprovedSucInfos(Page page,
                                                     @Param("applyUser") String applyUser,
                                                     @Param("approvalUser") String approvalUser,
                                                     @Param("equipmentName") String equipmentName,
                                                     @Param("equipmentType") String equipmentType,
                                                     @Param("receiveStatusCode") Integer receiveStatusCode,
                                                     @Param("approvalStatusCode") Integer approvalStatusCode);

    IPage<Map<String, Object>> queryApprovedErrInfos(Page page,
                                                     @Param("applyUser") String applyUser,
                                                     @Param("approvalUser") String approvalUser,
                                                     @Param("equipmentName") String equipmentName,
                                                     @Param("approvalStatusCode") Integer approvalStatusCode);


    IPage<Map<String, Object>> queryApplyRecords(Page page,
                                                 @Param("equipmentName") String equipmentName,
                                                 @Param("approvalStatusCode") Integer approvalStatusCode,
                                                 @Param("receiveStatusCode") Integer receiveStatusCode,
                                                 @Param("loginUserCode") Integer loginUserCode);
}
