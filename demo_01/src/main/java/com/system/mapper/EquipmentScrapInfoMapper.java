package com.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.EquipmentScrapInfo;
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
public interface EquipmentScrapInfoMapper extends BaseMapper<EquipmentScrapInfo> {

    IPage<Map<String, Object>> queryScrapInfos(Page page,
                                               @Param("equipmentName") String equipmentName,
                                               @Param("equipmentType") String equipmentType,
                                               @Param("scrapUser") String scrapUser);
}
