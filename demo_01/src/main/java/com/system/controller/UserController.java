package com.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.mapper.EquipmentInfoMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    EquipmentInfoMapper equipmentInfoMapper;

    //region ************************************************** 设备查看 **************************************************

    /**
     * 设备查看
     */
    @RequestMapping(value = "/queryUserEquipments")
    public Map<String, Object> queryUserEquipments(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");

            //获取当前登录用户信息
            Integer userCode = object.getInteger("userCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> equipmentInfos = equipmentInfoMapper.queryUserEquipments(page, equipmentName, userCode);
            List<Map<String, Object>> list = equipmentInfos.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", equipmentInfos.getTotal());
                resultMap.put("current", equipmentInfos.getCurrent());
                resultMap.put("pages", equipmentInfos.getPages());
            }

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    //endregion
}
