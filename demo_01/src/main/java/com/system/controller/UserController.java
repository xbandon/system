package com.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.EquipmentApplyInfo;
import com.system.entity.EquipmentChangeInfo;
import com.system.entity.EquipmentInfo;
import com.system.entity.UserInfo;
import com.system.mapper.EquipmentApplyInfoMapper;
import com.system.mapper.EquipmentChangeInfoMapper;
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
@RequestMapping("/user")
public class UserController {
    @Resource
    EquipmentInfoMapper equipmentInfoMapper;
    @Resource
    EquipmentApplyInfoMapper equipmentApplyInfoMapper;
    @Resource
    EquipmentChangeInfoMapper equipmentChangeInfoMapper;
    @Resource
    UserInfoMapper userInfoMapper;

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

    /**
     * 设备名称
     */
    @RequestMapping(value = "/queryEquipmentName")
    public Map<String, Object> queryEquipmentName() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<EquipmentInfo> list = equipmentInfoMapper.selectList(new QueryWrapper<EquipmentInfo>()
                    .select("equipmentName")
                    .groupBy("equipmentName"));

            if (!list.isEmpty()) {
                resultMap.put("list", list);
                resultMap.put("success", true);
            }

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 设备申请
     */
    @Transactional
    @RequestMapping(value = "/applyEquipment")
    public Map<String, Object> applyEquipment(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String equipmentName = object.getString("equipmentName");
            String applyReason = object.getString("applyReason");

            //获取当前登录用户信息


            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(equipmentName) || StringUtils.isEmpty(applyReason)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            EquipmentApplyInfo equipmentApplyInfo = new EquipmentApplyInfo();
            equipmentApplyInfo.setEquipmentName(equipmentName);
            equipmentApplyInfo.setApplyUserCode(3);
            equipmentApplyInfo.setApplyReason(applyReason);
            equipmentApplyInfo.setApplyTime(sysTime);
            equipmentApplyInfo.setApprovalStatusCode(1);

            //设备申请表插入
            equipmentApplyInfoMapper.insert(equipmentApplyInfo);

            resultMap.put("success", true);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    /**
     * 设备更换
     */
    @Transactional
    @RequestMapping(value = "/changeEquipment")
    public Map<String, Object> changeEquipment(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String srcEquipmentName = object.getString("srcEquipmentName");
            String srcEquipmentType = object.getString("srcEquipmentType");
            String equipmentName = object.getString("equipmentName");
            String applyReason = object.getString("applyReason");

            //获取当前登录用户信息


            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(srcEquipmentName) || StringUtils.isEmpty(srcEquipmentType) || StringUtils.isEmpty(equipmentName) || StringUtils.isEmpty(applyReason)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            EquipmentChangeInfo equipmentChangeInfo = new EquipmentChangeInfo();
            equipmentChangeInfo.setSrcEquipmentName(srcEquipmentName);
            equipmentChangeInfo.setSrcEquipmentType(srcEquipmentType);
            equipmentChangeInfo.setEquipmentName(equipmentName);
            equipmentChangeInfo.setApplyUserCode(3);
            equipmentChangeInfo.setApplyReason(applyReason);
            equipmentChangeInfo.setApplyTime(sysTime);
            equipmentChangeInfo.setApprovalStatusCode(1);

            //设备更换表插入
            equipmentChangeInfoMapper.insert(equipmentChangeInfo);

            resultMap.put("success", true);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 设备申请记录 **************************************************

    /**
     * 设备申请记录
     */
    @RequestMapping(value = "/queryApplyRecords")
    public Map<String, Object> queryApplyRecords(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");
            Integer approvalStatusCode = object.getInteger("approvalStatusCode");
            Integer receiveStatusCode = object.getInteger("receiveStatusCode");

            //获取当前登录用户信息
            Integer userCode = object.getInteger("userCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> applyRecords = equipmentApplyInfoMapper.queryApplyRecords(page, equipmentName, approvalStatusCode, receiveStatusCode, userCode);
            List<Map<String, Object>> list = applyRecords.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", applyRecords.getTotal());
                resultMap.put("current", applyRecords.getCurrent());
                resultMap.put("pages", applyRecords.getPages());
            }

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 设备申请接收
     */
    @Transactional
    @RequestMapping(value = "/receiveApplyEquipment")
    public Map<String, Object> receiveApplyEquipment(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String equipmentType = object.getString("equipmentType");

            //获取当前登录用户信息

            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(equipmentType)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            //设备申请表
            EquipmentApplyInfo equipmentApplyInfo = new EquipmentApplyInfo();
            equipmentApplyInfo.setKeyId(keyId);
            equipmentApplyInfo.setReceiveStatusCode(2);
            equipmentApplyInfo.setReceiveTime(sysTime);

            //设备申请表更新
            equipmentApplyInfoMapper.updateById(equipmentApplyInfo);

            //设备信息
            EquipmentInfo equipmentInfo = new EquipmentInfo();
            equipmentInfo.setEquipmentType(equipmentType);
            equipmentInfo.setEquipmentStatusCode(2);
            equipmentInfo.setUserCode(3);

            //设备信息表更新
            equipmentInfoMapper.update(equipmentInfo, new QueryWrapper<EquipmentInfo>()
                    .eq("equipmentType", equipmentType));

            resultMap.put("success", true);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 设备更换记录 **************************************************

    /**
     * 设备更换记录
     */
    @RequestMapping(value = "/queryChangeRecords")
    public Map<String, Object> queryChangeRecords(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");
            Integer approvalStatusCode = object.getInteger("approvalStatusCode");
            Integer receiveStatusCode = object.getInteger("receiveStatusCode");

            //获取当前登录用户信息
            Integer userCode = object.getInteger("userCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> changeRecords = equipmentChangeInfoMapper.queryChangeRecords(page, equipmentName, approvalStatusCode, receiveStatusCode, userCode);
            List<Map<String, Object>> list = changeRecords.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", changeRecords.getTotal());
                resultMap.put("current", changeRecords.getCurrent());
                resultMap.put("pages", changeRecords.getPages());
            }

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 设备更换接收
     */
    @Transactional
    @RequestMapping(value = "/receiveChangeEquipment")
    public Map<String, Object> receiveChangeEquipment(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String equipmentType = object.getString("equipmentType");

            //获取当前登录用户信息

            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(equipmentType)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            //设备申请表
            EquipmentChangeInfo equipmentChangeInfo = new EquipmentChangeInfo();
            equipmentChangeInfo.setKeyId(keyId);
            equipmentChangeInfo.setReceiveStatusCode(2);
            equipmentChangeInfo.setReceiveTime(sysTime);

            //设备申请表更新
            equipmentChangeInfoMapper.updateById(equipmentChangeInfo);

            //设备信息
            EquipmentInfo equipmentInfo = new EquipmentInfo();
            equipmentInfo.setEquipmentType(equipmentType);
            equipmentInfo.setEquipmentStatusCode(2);
            equipmentInfo.setUserCode(3);

            //设备信息表更新
            equipmentInfoMapper.update(equipmentInfo, new QueryWrapper<EquipmentInfo>()
                    .eq("equipmentType", equipmentType));

            resultMap.put("success", true);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 个人中心 **************************************************

    /**
     * 修改个人信息
     */
    @Transactional
    @RequestMapping(value = "/editUserInfo")
    public Map<String, Object> editUserInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String userName = object.getString("userName");
            String loginName = object.getString("loginName");
            String email = object.getString("email");
            String telephoneNumber = object.getString("telephoneNumber");

            //获取当前登录用户信息
            Integer userCode = object.getInteger("userCode");

            //系统时间
            Date sysTime = new Date();

            //个人信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUserCode(userCode);
            userInfo.setUserName(userName);
            userInfo.setLoginName(loginName);
            userInfo.setEmail(email);
            userInfo.setTelephoneNumber(telephoneNumber);
            userInfo.setUpdateUser(userCode);
            userInfo.setUpdateTime(sysTime);

            //员工信息表更新
            userInfoMapper.updateById(userInfo);

            resultMap.put("success", true);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    /**
     * 修改密码
     */
    @Transactional
    @RequestMapping(value = "/editPassword")
    public Map<String, Object> editPassword(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String srcPass = object.getString("srcPass");
            String newPass = object.getString("newPass");

            //获取当前登录用户信息
            Integer userCode = object.getInteger("userCode");

            //系统时间
            Date sysTime = new Date();

            //原密码校验
            Long flg = userInfoMapper.selectCount(new QueryWrapper<UserInfo>()
                    .eq("loginPassword", srcPass)
                    .eq("userCode", userCode));
            if (flg == 0) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "原密码错误！");
            } else {
                //个人信息
                UserInfo userInfo = new UserInfo();
                userInfo.setUserCode(userCode);
                userInfo.setLoginPassword(newPass);

                //员工信息表更新
                userInfoMapper.updateById(userInfo);

                resultMap.put("success", true);
            }

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    //endregion

}
