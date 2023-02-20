package com.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.EquipmentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-02-10
 */
public interface EquipmentInfoMapper extends BaseMapper<EquipmentInfo> {

    IPage<Map<String, Object>> queryEquipmentStock(Page page,
                                                   @Param("equipmentName") String equipmentName);

    int queryEquipmentFreeNum(@Param("equipmentName") String equipmentName);

    List<Map<String, Object>> queryFreeEquipments(@Param("equipmentName") String equipmentName);

    IPage<Map<String, Object>> queryEquipmentInfos(Page page,
                                                   @Param("equipmentName") String equipmentName,
                                                   @Param("equipmentType") String equipmentType,
                                                   @Param("equipmentStatusCode") Integer equipmentStatusCode,
                                                   @Param("userName") String userName);

}
