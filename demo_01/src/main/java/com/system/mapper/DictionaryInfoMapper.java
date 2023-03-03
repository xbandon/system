package com.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.DictionaryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-03-03
 */
public interface DictionaryInfoMapper extends BaseMapper<DictionaryInfo> {

    IPage<Map<String, Object>> queryDictionaryInfos(Page<Map<String, Object>> page,
                                                    @Param("dicTypeCode") Integer dicTypeCode,
                                                    @Param("dicValue") String dicValue,
                                                    @Param("delFlag") String delFlag);
}
