package com.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.EquipmentInfo;
import com.system.entity.UserInfo;
import com.system.mapper.EquipmentApplyInfoMapper;
import com.system.mapper.EquipmentInfoMapper;
import com.system.mapper.UserInfoMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eq")
public class ManageController {
    @Resource
    EquipmentInfoMapper equipmentInfoMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    EquipmentApplyInfoMapper equipmentApplyInfoMapper;

    //region ************************************************** 库存管理 **************************************************

    /**
     * 库存查看
     */
    @RequestMapping(value = "/queryEquipmentStock")
    public Map<String, Object> queryEquipmentStock(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> equipmentStock = equipmentInfoMapper.queryEquipmentStock(page, equipmentName);
            List<Map<String, Object>> list = equipmentStock.getRecords();

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 设备查看 **************************************************

    /**
     * 设备查看
     */
    @RequestMapping(value = "/queryEquipmentInfos")
    public Map<String, Object> queryEquipmentInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");
            String equipmentType = object.getString("equipmentType");
            Integer equipmentStatusCode = object.getInteger("equipmentStatusCode");
            String userName = object.getString("userName");
            String insertTime = object.getString("insertTime");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> equipmentInfo = equipmentInfoMapper.queryEquipmentInfos(page, equipmentName, equipmentType,
                    equipmentStatusCode, userName, insertTime);
            List<Map<String, Object>> list = equipmentInfo.getRecords();
            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 设备新增
     */
    @Transactional
    @RequestMapping(value = "/addEquipmentInfo")
    public Map<String, Object> addEquipmentInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String equipmentName = object.getString("equipmentName");
            String equipmentType = object.getString("equipmentType");

            //获取当前登录人员信息


            //系统时间
            Date sysTime = new Date();

            //设备型号重复检查
            Long isExist = equipmentInfoMapper.selectCount(new QueryWrapper<EquipmentInfo>()
                    .eq("equipmentType", equipmentType));
            if (isExist > 0) {
                resultMap.put("errMsg", "您输入的设备型号已存在！");
            } else {
                //设备信息
                EquipmentInfo equipmentInfo = new EquipmentInfo();
                equipmentInfo.setEquipmentName(equipmentName);
                equipmentInfo.setEquipmentType(equipmentType);
                equipmentInfo.setEquipmentStatusCode(0);
                equipmentInfo.setInsertTime(sysTime);
                equipmentInfo.setUpdateTime(sysTime);

                //设备信息表插入
                equipmentInfoMapper.insert(equipmentInfo);

                resultMap.put("success", true);
            }

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 设备申请 **************************************************

    /**
     * 待审批记录查看
     */
    @RequestMapping(value = "/queryPendingInfos")
    public Map<String, Object> queryPendingInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String userName = object.getString("userName");
            String equipmentName = object.getString("equipmentName");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> pendingInfo = equipmentApplyInfoMapper.queryPendingInfos(page, userName, equipmentName);
            List<Map<String, Object>> list = pendingInfo.getRecords();
            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 审批通过记录查看
     */
    @RequestMapping(value = "/queryApprovedSucInfos")
    public Map<String, Object> queryApprovedSucInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String applyUser = object.getString("applyUser");
            String approvalUser = object.getString("approvalUser");
            String equipmentName = object.getString("equipmentName");
            String equipmentType = object.getString("equipmentType");
            String receiveStatusCode = object.getString("receiveStatusCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> approvedInfo = equipmentApplyInfoMapper.queryApprovedSucInfos(page, applyUser, approvalUser,
                    equipmentName, equipmentType, receiveStatusCode);
            List<Map<String, Object>> list = approvedInfo.getRecords();
            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 审批未通过记录查看
     */
    @RequestMapping(value = "/queryApprovedErrInfos")
    public Map<String, Object> queryApprovedErrInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String applyUser = object.getString("applyUser");
            String approvalUser = object.getString("approvalUser");
            String equipmentName = object.getString("equipmentName");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> approvedInfo = equipmentApplyInfoMapper.queryApprovedErrInfos(page, applyUser, approvalUser,
                    equipmentName);
            List<Map<String, Object>> list = approvedInfo.getRecords();
            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 员工查看 **************************************************

    /**
     * 员工查看
     */
    @RequestMapping(value = "/queryUserInfos")
    public Map<String, Object> queryUserInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String userCode = object.getString("userCode");
            String userName = object.getString("userName");
            Integer onlineStatusCode = object.getInteger("onlineStatusCode");
            Integer accountStatusCode = object.getInteger("accountStatusCode");
            String insertTime = object.getString("insertTime");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> userInfo = userInfoMapper.queryUserInfos(page, userCode, userName, onlineStatusCode,
                    accountStatusCode, insertTime);
            List<Map<String, Object>> list = userInfo.getRecords();
            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 新增员工
     */
    @Transactional
    @RequestMapping(value = "/addUserInfo")
    public Map<String, Object> addUserInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String userName = object.getString("userName");
            String loginName = object.getString("loginName");
            String email = object.getString("email");
            String telephoneNumber = object.getString("telephoneNumber");
            Integer roleCode = object.getInteger("roleCode");

            //获取当前登录人员信息


            //系统时间
            Date sysTime = new Date();

            //登录账户名重复检查
            Long isExist = userInfoMapper.selectCount(new QueryWrapper<UserInfo>()
                    .eq("loginName", loginName));
            if (isExist > 0) {
                resultMap.put("errMsg", "您输入的登录账户名已存在！");
            } else {
                //员工信息
                UserInfo user = new UserInfo();
                user.setUserName(userName);
                user.setLoginName(loginName);
                user.setLoginPassword("12345678");
                user.setEmail(email);
                user.setTelephoneNumber(telephoneNumber);
                user.setRoleCode(roleCode);
                user.setOnlineStatusCode(0);
                user.setAccountStatusCode(0);
                user.setInsertTime(sysTime);
                user.setUpdateTime(sysTime);

                //员工信息表插入
                userInfoMapper.insert(user);

                resultMap.put("success", true);
            }

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }
}
