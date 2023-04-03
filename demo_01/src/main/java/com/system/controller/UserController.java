package com.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.constant.CommonConstant;
import com.system.constant.ResponseConstant;
import com.system.entity.EquipmentApplyInfo;
import com.system.entity.EquipmentChangeInfo;
import com.system.entity.EquipmentInfo;
import com.system.entity.UserInfo;
import com.system.mapper.EquipmentApplyInfoMapper;
import com.system.mapper.EquipmentChangeInfoMapper;
import com.system.mapper.EquipmentInfoMapper;
import com.system.mapper.UserInfoMapper;
import com.system.wrapper.Wrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static Logger logger = LogManager.getLogger(ManagerController.class);

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
    public Wrapper queryUserEquipments(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> equipmentInfos = equipmentInfoMapper.queryUserEquipments(page, equipmentName, loginUserCode);
            List<Map<String, Object>> list = equipmentInfos.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", equipmentInfos.getTotal());
                resultMap.put("current", equipmentInfos.getCurrent());
                resultMap.put("pages", equipmentInfos.getPages());
            }

            resultMap.put("list", list);

        } catch (Exception e) {
            logger.error("系统异常", e);
        }
        return Wrapper.success(resultMap);
    }

    /**
     * 设备名称
     */
    @RequestMapping(value = "/queryEquipmentName")
    public Wrapper queryEquipmentName() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<EquipmentInfo> list = equipmentInfoMapper.selectList(new QueryWrapper<EquipmentInfo>()
                    .select("equipmentName")
                    .groupBy("equipmentName"));

            if (!list.isEmpty()) {
                resultMap.put("list", list);
            }

        } catch (Exception e) {
            logger.error("系统异常", e);
        }
        return Wrapper.success(resultMap);
    }

    /**
     * 设备申请
     */
    @Transactional
    @RequestMapping(value = "/applyEquipment")
    public Wrapper applyEquipment(@RequestBody String json) {
        try {
            JSONObject object = JSON.parseObject(json);
            String equipmentName = object.getString("equipmentName");
            String applyReason = object.getString("applyReason");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(equipmentName) || StringUtils.isEmpty(applyReason)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            EquipmentApplyInfo equipmentApplyInfo = new EquipmentApplyInfo();
            equipmentApplyInfo.setEquipmentName(equipmentName);
            equipmentApplyInfo.setApplyUserCode(loginUserCode);
            equipmentApplyInfo.setApplyReason(applyReason);
            equipmentApplyInfo.setApplyTime(sysTime);
            equipmentApplyInfo.setApprovalStatusCode(CommonConstant.APPLICATION_APPROVING);

            //设备申请表插入
            equipmentApplyInfoMapper.insert(equipmentApplyInfo);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("系统异常", e);
            return Wrapper.error();
        }
        return Wrapper.success();
    }

    /**
     * 设备更换
     */
    @Transactional
    @RequestMapping(value = "/changeEquipment")
    public Wrapper changeEquipment(@RequestBody String json) {
        try {
            JSONObject object = JSON.parseObject(json);
            String srcEquipmentName = object.getString("srcEquipmentName");
            String srcEquipmentType = object.getString("srcEquipmentType");
            String equipmentName = object.getString("equipmentName");
            String applyReason = object.getString("applyReason");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(srcEquipmentName) || StringUtils.isEmpty(srcEquipmentType) || StringUtils.isEmpty(equipmentName) || StringUtils.isEmpty(applyReason)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            EquipmentChangeInfo equipmentChangeInfo = new EquipmentChangeInfo();
            equipmentChangeInfo.setSrcEquipmentName(srcEquipmentName);
            equipmentChangeInfo.setSrcEquipmentType(srcEquipmentType);
            equipmentChangeInfo.setEquipmentName(equipmentName);
            equipmentChangeInfo.setApplyUserCode(loginUserCode);
            equipmentChangeInfo.setApplyReason(applyReason);
            equipmentChangeInfo.setApplyTime(sysTime);
            equipmentChangeInfo.setApprovalStatusCode(CommonConstant.APPLICATION_APPROVING);

            //设备更换表插入
            equipmentChangeInfoMapper.insert(equipmentChangeInfo);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("系统异常", e);
            return Wrapper.error();
        }
        return Wrapper.success();
    }

    //endregion

    //region ************************************************** 设备申请记录 **************************************************

    /**
     * 设备申请记录
     */
    @RequestMapping(value = "/queryApplyRecords")
    public Wrapper queryApplyRecords(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");
            Integer approvalStatusCode = object.getInteger("approvalStatusCode");
            Integer receiveStatusCode = object.getInteger("receiveStatusCode");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> applyRecords = equipmentApplyInfoMapper.queryApplyRecords(page, equipmentName, approvalStatusCode, receiveStatusCode, loginUserCode);
            List<Map<String, Object>> list = applyRecords.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", applyRecords.getTotal());
                resultMap.put("current", applyRecords.getCurrent());
                resultMap.put("pages", applyRecords.getPages());
            }

            resultMap.put("list", list);

        } catch (Exception e) {
            logger.error("系统异常", e);
        }
        return Wrapper.success(resultMap);
    }

    /**
     * 设备申请接收
     */
    @Transactional
    @RequestMapping(value = "/receiveApplyEquipment")
    public Wrapper receiveApplyEquipment(@RequestBody String json) {
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String equipmentType = object.getString("equipmentType");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(equipmentType)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            //设备申请表
            EquipmentApplyInfo equipmentApplyInfo = new EquipmentApplyInfo();
            equipmentApplyInfo.setKeyId(keyId);
            equipmentApplyInfo.setReceiveStatusCode(CommonConstant.EQUIPMENT_RECEIVED);
            equipmentApplyInfo.setReceiveTime(sysTime);

            //设备申请表更新
            equipmentApplyInfoMapper.updateById(equipmentApplyInfo);

            //设备信息
            EquipmentInfo equipmentInfo = new EquipmentInfo();
            equipmentInfo.setEquipmentType(equipmentType);
            equipmentInfo.setEquipmentStatusCode(CommonConstant.EQUIPMENT_USING);
            equipmentInfo.setUserCode(loginUserCode);

            //设备信息表更新
            equipmentInfoMapper.update(equipmentInfo, new QueryWrapper<EquipmentInfo>()
                    .eq("equipmentType", equipmentType));

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("系统异常", e);
            return Wrapper.error();
        }
        return Wrapper.success();
    }

    //endregion

    //region ************************************************** 设备更换记录 **************************************************

    /**
     * 设备更换记录
     */
    @RequestMapping(value = "/queryChangeRecords")
    public Wrapper queryChangeRecords(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");
            Integer approvalStatusCode = object.getInteger("approvalStatusCode");
            Integer receiveStatusCode = object.getInteger("receiveStatusCode");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> changeRecords = equipmentChangeInfoMapper.queryChangeRecords(page, equipmentName, approvalStatusCode, receiveStatusCode, loginUserCode);
            List<Map<String, Object>> list = changeRecords.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", changeRecords.getTotal());
                resultMap.put("current", changeRecords.getCurrent());
                resultMap.put("pages", changeRecords.getPages());
            }

            resultMap.put("list", list);

        } catch (Exception e) {
            logger.error("系统异常", e);
        }
        return Wrapper.success(resultMap);
    }

    /**
     * 设备更换接收
     */
    @Transactional
    @RequestMapping(value = "/receiveChangeEquipment")
    public Wrapper receiveChangeEquipment(@RequestBody String json) {
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String equipmentType = object.getString("equipmentType");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(equipmentType)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            //设备申请表
            EquipmentChangeInfo equipmentChangeInfo = new EquipmentChangeInfo();
            equipmentChangeInfo.setKeyId(keyId);
            equipmentChangeInfo.setReceiveStatusCode(CommonConstant.EQUIPMENT_RECEIVED);
            equipmentChangeInfo.setReceiveTime(sysTime);

            //设备申请表更新
            equipmentChangeInfoMapper.updateById(equipmentChangeInfo);

            //设备信息
            EquipmentInfo equipmentInfo = new EquipmentInfo();
            equipmentInfo.setEquipmentType(equipmentType);
            equipmentInfo.setEquipmentStatusCode(CommonConstant.EQUIPMENT_USING);
            equipmentInfo.setUserCode(loginUserCode);

            //设备信息表更新
            equipmentInfoMapper.update(equipmentInfo, new QueryWrapper<EquipmentInfo>()
                    .eq("equipmentType", equipmentType));

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("系统异常", e);
            return Wrapper.error();
        }
        return Wrapper.success();
    }

    //endregion

    //region ************************************************** 个人信息 **************************************************

    /**
     * 修改个人信息
     */
    @Transactional
    @RequestMapping(value = "/editUserInfo")
    public Wrapper editUserInfo(@RequestBody String json) {
        try {
            JSONObject object = JSON.parseObject(json);
            String userName = object.getString("userName");
            String loginName = object.getString("loginName");
            String email = object.getString("email");
            String telephoneNumber = object.getString("telephoneNumber");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            //系统时间
            Date sysTime = new Date();

            //个人信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUserCode(loginUserCode);
            userInfo.setUserName(userName);
            userInfo.setLoginName(loginName);
            userInfo.setEmail(email);
            userInfo.setTelephoneNumber(telephoneNumber);
            userInfo.setUpdateUser(loginUserCode);
            userInfo.setUpdateTime(sysTime);

            //员工信息表更新
            userInfoMapper.updateById(userInfo);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("系统异常", e);
            return Wrapper.error();
        }
        return Wrapper.success();
    }

    /**
     * 修改密码
     */
    @Transactional
    @RequestMapping(value = "/editPassword")
    public Wrapper editPassword(@RequestBody String json) {
        try {
            JSONObject object = JSON.parseObject(json);
            String srcPass = object.getString("srcPass");
            String newPass = object.getString("newPass");

            //获取当前登录用户信息
            Integer loginUserCode = object.getInteger("loginUserCode");

            //系统时间
            Date sysTime = new Date();

            //原密码检查
            Long flg = userInfoMapper.selectCount(new QueryWrapper<UserInfo>()
                    .eq("loginPassword", srcPass)
                    .eq("userCode", loginUserCode));
            if (flg == 0) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "原密码错误");
            }

            //个人信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUserCode(loginUserCode);
            userInfo.setLoginPassword(newPass);

            //员工信息表更新
            userInfoMapper.updateById(userInfo);

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("系统异常", e);
            return Wrapper.error();
        }
        return Wrapper.success();
    }

    //endregion

}
