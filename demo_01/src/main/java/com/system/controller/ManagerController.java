package com.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.system.entity.*;
import com.system.mapper.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/mg")
public class ManagerController {
    @Resource
    EquipmentInfoMapper equipmentInfoMapper;
    @Resource
    UserInfoMapper userInfoMapper;
    @Resource
    EquipmentApplyInfoMapper equipmentApplyInfoMapper;
    @Resource
    EquipmentChangeInfoMapper equipmentChangeInfoMapper;
    @Resource
    EquipmentScrapInfoMapper equipmentScrapInfoMapper;
    @Resource
    DictionaryInfoMapper dictionaryInfoMapper;

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

            if (!list.isEmpty()) {
                resultMap.put("total", equipmentStock.getTotal());
                resultMap.put("current", equipmentStock.getCurrent());
                resultMap.put("pages", equipmentStock.getPages());
            }

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

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> equipmentInfos = equipmentInfoMapper.queryEquipmentInfos(page, equipmentName, equipmentType,
                    equipmentStatusCode, userName);
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
                resultMap.put("error", false);
                resultMap.put("errMsg", "您输入的设备型号已存在！");
            } else {
                //设备信息
                EquipmentInfo equipmentInfo = new EquipmentInfo();
                equipmentInfo.setEquipmentName(equipmentName);
                equipmentInfo.setEquipmentType(equipmentType);
                equipmentInfo.setEquipmentStatusCode(0);
                //equipmentInfo.setInsertUser();
                equipmentInfo.setInsertTime(sysTime);
                //equipmentInfo.setUpdateUser();
                equipmentInfo.setUpdateTime(sysTime);

                //设备信息表插入
                equipmentInfoMapper.insert(equipmentInfo);

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

    /**
     * 设备删除
     */
    @Transactional
    @RequestMapping(value = "/deleteEquipmentInfo")
    public Map<String, Object> deleteEquipmentInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            JSONArray list = object.getJSONArray("list");
            List<Integer> idList = new ArrayList<>();

            //判断设备是否报废
            for (Object o : list) {
                Map map = (Map) o;
                Integer keyId = (Integer) map.get("keyId");
                Integer equipmentStatusCode = (Integer) map.get("equipmentStatusCode");
                if (equipmentStatusCode != 3) {
                    idList.clear();
                    resultMap.put("error", false);
                    resultMap.put("errMsg", "仅报废设备可以删除！");
                    break;
                } else {
                    idList.add(keyId);
                }
            }

            if (idList.size() > 0) {
                for (Integer keyId : idList) {
                    //设备信息表删除
                    equipmentInfoMapper.deleteById(keyId);
                    resultMap.put("success", true);
                }
            }

        } catch (
                Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 设备申请 **************************************************

    /**
     * 待审批记录查看
     */
    @RequestMapping(value = "/queryApprovingInfos")
    public Map<String, Object> queryApprovingInfos(@RequestBody String json) {
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
            IPage<Map<String, Object>> approvingInfo = equipmentApplyInfoMapper.queryApprovingInfos(page, userName, equipmentName);
            List<Map<String, Object>> list = approvingInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", approvingInfo.getTotal());
                resultMap.put("current", approvingInfo.getCurrent());
                resultMap.put("pages", approvingInfo.getPages());
            }

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
            Integer receiveStatusCode = object.getInteger("receiveStatusCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> approvedSucInfo = equipmentApplyInfoMapper.queryApprovedSucInfos(page, applyUser, approvalUser,
                    equipmentName, equipmentType, receiveStatusCode);
            List<Map<String, Object>> list = approvedSucInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", approvedSucInfo.getTotal());
                resultMap.put("current", approvedSucInfo.getCurrent());
                resultMap.put("pages", approvedSucInfo.getPages());
            }

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
            IPage<Map<String, Object>> approvedErrInfo = equipmentApplyInfoMapper.queryApprovedErrInfos(page, applyUser, approvalUser,
                    equipmentName);
            List<Map<String, Object>> list = approvedErrInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", approvedErrInfo.getTotal());
                resultMap.put("current", approvedErrInfo.getCurrent());
                resultMap.put("pages", approvedErrInfo.getPages());
            }

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 设备更换 **************************************************

    /**
     * 待审批记录查看
     */
    @RequestMapping(value = "/queryChangingInfos")
    public Map<String, Object> queryChangingInfos(@RequestBody String json) {
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
            IPage<Map<String, Object>> changingInfo = equipmentChangeInfoMapper.queryChangingInfos(page, userName,
                    equipmentName);
            List<Map<String, Object>> list = changingInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", changingInfo.getTotal());
                resultMap.put("current", changingInfo.getCurrent());
                resultMap.put("pages", changingInfo.getPages());
            }

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
    @RequestMapping(value = "/queryChangedSucInfos")
    public Map<String, Object> queryChangedSucInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String applyUser = object.getString("applyUser");
            String approvalUser = object.getString("approvalUser");
            String equipmentName = object.getString("equipmentName");
            String equipmentType = object.getString("equipmentType");
            Integer receiveStatusCode = object.getInteger("receiveStatusCode");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> changedSucInfo = equipmentChangeInfoMapper.queryChangedSucInfos(page, applyUser, approvalUser,
                    equipmentName, equipmentType, receiveStatusCode);
            List<Map<String, Object>> list = changedSucInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", changedSucInfo.getTotal());
                resultMap.put("current", changedSucInfo.getCurrent());
                resultMap.put("pages", changedSucInfo.getPages());
            }

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
    @RequestMapping(value = "/queryChangedErrInfos")
    public Map<String, Object> queryChangedErrInfos(@RequestBody String json) {
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
            IPage<Map<String, Object>> changedErrInfo = equipmentChangeInfoMapper.queryChangedErrInfos(page, applyUser, approvalUser,
                    equipmentName);
            List<Map<String, Object>> list = changedErrInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", changedErrInfo.getTotal());
                resultMap.put("current", changedErrInfo.getCurrent());
                resultMap.put("pages", changedErrInfo.getPages());
            }

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }
    //endregion

    //region ************************************************** 设备审批 **************************************************

    /**
     * 空闲设备查看
     */
    @RequestMapping(value = "/queryFreeEquipments")
    public Map<String, Object> queryFreeEquipments(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String equipmentName = object.getString("equipmentName");

            if (StringUtils.isEmpty(equipmentName)) {
                resultMap.put("errMsg", "参数错误！");
            }

            int freeNum = equipmentInfoMapper.queryEquipmentFreeNum(equipmentName);
            if (freeNum > 0) {
                List<Map<String, Object>> list = equipmentInfoMapper.queryFreeEquipments(equipmentName);
                resultMap.put("list", list);
            }

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 设备申请审批
     */
    @Transactional
    @RequestMapping(value = "/approveApplications")
    public Map<String, Object> approveApplications(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String equipmentType = object.getString("equipmentType");
            Integer approvalStatusCode = object.getInteger("approvalStatusCode");
            String approvalLog = object.getString("approvalLog");

            //获取当前登录人员信息


            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(keyId) || StringUtils.isEmpty(equipmentType) || StringUtils.isEmpty(approvalStatusCode)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            EquipmentApplyInfo equipmentApplyInfo = new EquipmentApplyInfo();
            equipmentApplyInfo.setKeyId(keyId);

            if (approvalStatusCode == 1) {
                //审批通过
                equipmentApplyInfo.setEquipmentType(equipmentType);
                equipmentApplyInfo.setApprovalStatusCode(1);
                //equipmentApplyInfo.setApprovalUserCode();
                equipmentApplyInfo.setApprovalTime(sysTime);
                equipmentApplyInfo.setReceiveStatusCode(1);

                EquipmentInfo equipmentInfo = new EquipmentInfo();
                equipmentInfo.setEquipmentType(equipmentType);
                equipmentInfo.setEquipmentStatusCode(1);
                //equipmentInfo.setUpdateUser();
                equipmentInfo.setUpdateTime(sysTime);

                //设备申请表更新
                equipmentApplyInfoMapper.updateById(equipmentApplyInfo);

                //设备信息表更新
                equipmentInfoMapper.update(equipmentInfo, new QueryWrapper<EquipmentInfo>()
                        .eq("equipmentType", equipmentType));
            } else {
                //审批未通过
                equipmentApplyInfo.setApprovalStatusCode(2);
                //equipmentApplyInfo.setApprovalUserCode();
                equipmentApplyInfo.setApprovalLog(approvalLog);
                equipmentApplyInfo.setApprovalTime(sysTime);

                //设备申请表更新
                equipmentApplyInfoMapper.updateById(equipmentApplyInfo);
            }
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
     * 设备更换审批
     */
    @Transactional
    @RequestMapping(value = "/approveChangeApplications")
    public Map<String, Object> approveChangeApplications(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String srcEquipmentName = object.getString("srcEquipmentName");
            String srcEquipmentType = object.getString("srcEquipmentType");
            String equipmentType = object.getString("equipmentType");
            String applyReason = object.getString("applyReason");
            Integer approvalStatusCode = object.getInteger("approvalStatusCode");
            String approvalLog = object.getString("approvalLog");

            //获取当前登录人员信息


            //系统时间
            Date sysTime = new Date();

            if (StringUtils.isEmpty(keyId) || StringUtils.isEmpty(srcEquipmentName) || StringUtils.isEmpty(srcEquipmentType) ||
                    StringUtils.isEmpty(equipmentType) || StringUtils.isEmpty(applyReason) || StringUtils.isEmpty(approvalStatusCode)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            EquipmentChangeInfo equipmentChangeInfo = new EquipmentChangeInfo();
            equipmentChangeInfo.setKeyId(keyId);

            if (approvalStatusCode == 1) {
                //审批通过
                equipmentChangeInfo.setEquipmentType(equipmentType);
                equipmentChangeInfo.setApprovalStatusCode(1);
                //equipmentChangeInfo.setApprovalUserCode();
                equipmentChangeInfo.setApprovalTime(sysTime);
                equipmentChangeInfo.setReceiveStatusCode(1);

                EquipmentInfo srcEquipmentInfo = new EquipmentInfo();
                srcEquipmentInfo.setEquipmentType(srcEquipmentType);
                srcEquipmentInfo.setEquipmentStatusCode(3);
                srcEquipmentInfo.setUserCode(null);
                //equipmentInfo.setUpdateUser();
                srcEquipmentInfo.setUpdateTime(sysTime);

                EquipmentInfo equipmentInfo = new EquipmentInfo();
                equipmentInfo.setEquipmentType(equipmentType);
                equipmentInfo.setEquipmentStatusCode(1);
                //equipmentInfo.setUpdateUser();
                equipmentInfo.setUpdateTime(sysTime);

                EquipmentScrapInfo equipmentScrapInfo = new EquipmentScrapInfo();
                equipmentScrapInfo.setEquipmentName(srcEquipmentName);
                equipmentScrapInfo.setEquipmentType(srcEquipmentType);
                //equipmentScrapInfo.setScrapUserCode();
                equipmentScrapInfo.setScrapTime(sysTime);
                equipmentScrapInfo.setScrapLog(applyReason);

                //设备更换表更新
                equipmentChangeInfoMapper.updateById(equipmentChangeInfo);

                //设备信息表更新
                equipmentInfoMapper.update(srcEquipmentInfo, new QueryWrapper<EquipmentInfo>()
                        .eq("equipmentType", srcEquipmentType));
                equipmentInfoMapper.update(equipmentInfo, new QueryWrapper<EquipmentInfo>()
                        .eq("equipmentType", equipmentType));

                //设备报废表插入
                equipmentScrapInfoMapper.insert(equipmentScrapInfo);
            } else {
                //审批未通过
                equipmentChangeInfo.setApprovalStatusCode(2);
                //equipmentChangeInfo.setApprovalUserCode();
                equipmentChangeInfo.setApprovalLog(approvalLog);
                equipmentChangeInfo.setApprovalTime(sysTime);

                //设备更换表更新
                equipmentChangeInfoMapper.updateById(equipmentChangeInfo);
            }
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

    //region ************************************************** 报废记录 **************************************************

    /**
     * 报废记录查看
     */
    @RequestMapping(value = "/queryScrapInfos")
    public Map<String, Object> queryScrapInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");                          // 每页显示数据量
            Integer nextPage = object.getInteger("page");                          // 页数
            String equipmentName = object.getString("equipmentName");
            String equipmentType = object.getString("equipmentType");
            String scrapUser = object.getString("scrapUser");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> scrapInfos = equipmentScrapInfoMapper.queryScrapInfos(page, equipmentName, equipmentType,
                    scrapUser);
            List<Map<String, Object>> list = scrapInfos.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", scrapInfos.getTotal());
                resultMap.put("current", scrapInfos.getCurrent());
                resultMap.put("pages", scrapInfos.getPages());
            }

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
            String userName = object.getString("userName");
            Integer roleCode = object.getInteger("roleCode");
            Integer onlineStatusCode = object.getInteger("onlineStatusCode");
            Integer accountStatusCode = object.getInteger("accountStatusCode");
            String entryTime = object.getString("entryTime");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> userInfo = userInfoMapper.queryUserInfos(page, userName, roleCode, onlineStatusCode,
                    accountStatusCode, entryTime);
            List<Map<String, Object>> list = userInfo.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", userInfo.getTotal());
                resultMap.put("current", userInfo.getCurrent());
                resultMap.put("pages", userInfo.getPages());
            }

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
                resultMap.put("error", false);
                resultMap.put("errMsg", "您输入的账户名已存在！");
            } else {
                //员工信息
                UserInfo user = new UserInfo();
                user.setUserName(userName);
                user.setLoginName(loginName);
                user.setLoginPassword("123456");
                user.setEmail(email);
                user.setTelephoneNumber(telephoneNumber);
                user.setRoleCode(roleCode);
                user.setOnlineStatusCode(1);
                user.setAccountStatusCode(0);
                user.setEntryTime(sysTime);
                //user.setUpdateUser();
                user.setUpdateTime(sysTime);

                //员工信息表插入
                userInfoMapper.insert(user);

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

    /**
     * 删除员工
     */
    @Transactional
    @RequestMapping(value = "/deleteUserInfo")
    public Map<String, Object> deleteUserInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            JSONArray list = object.getJSONArray("list");
            List<Integer> idList = new ArrayList<>();

            //判断员工是否离职
            for (Object o : list) {
                Map map = (Map) o;
                Integer userCode = (Integer) map.get("userCode");
                Integer accountStatusCode = (Integer) map.get("accountStatusCode");
                if (accountStatusCode != 1) {
                    idList.clear();
                    resultMap.put("error", false);
                    resultMap.put("errMsg", "仅离职员工可以删除！");
                    break;
                } else {
                    idList.add(userCode);
                }
            }

            if (idList.size() > 0) {
                for (Integer userCode : idList) {
                    //员工信息表删除
                    userInfoMapper.deleteById(userCode);
                    resultMap.put("success", true);
                }
            }

        } catch (Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    /**
     * 员工授权
     */
    @Transactional
    @RequestMapping(value = "/editRole")
    public Map<String, Object> editRole(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer userCode = object.getInteger("userCode");
            Integer roleCode = object.getInteger("roleCode");

            //获取当前登录人员信息


            //系统时间
            Date sysTime = new Date();

            //员工信息
            UserInfo user = new UserInfo();
            user.setUserCode(userCode);
            user.setRoleCode(roleCode);
            //user.setUpdateUser();
            user.setUpdateTime(sysTime);

            //员工信息表更新
            userInfoMapper.updateById(user);
            resultMap.put("success", true);

        } catch (
                Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    /**
     * 密码重置
     */
    @Transactional
    @RequestMapping(value = "/resetPassword")
    public Map<String, Object> resetPassword(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer userCode = object.getInteger("userCode");

            //获取当前登录人员信息


            //系统时间
            Date sysTime = new Date();

            //员工信息
            UserInfo user = new UserInfo();
            user.setUserCode(userCode);
            user.setLoginPassword("123456");
            //user.setUpdateUser();
            user.setUpdateTime(sysTime);

            //员工信息表更新
            userInfoMapper.updateById(user);
            resultMap.put("success", true);

        } catch (
                Exception e) {
            //事务手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统繁忙，请稍后再试！");
        }
        return resultMap;
    }

    //endregion

    //region ************************************************** 字典维护 **************************************************

    /**
     * 字典查询
     */
    @RequestMapping(value = "/queryDictionaryInfos")
    public Map<String, Object> queryDictionaryInfos(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer pageSize = object.getInteger("rows");       //每页显示数据量
            Integer nextPage = object.getInteger("page");       //页数
            Integer dicTypeCode = object.getInteger("dicTypeCode");
            String dicValue = object.getString("dicValue");
            String delFlag = object.getString("delFlag");

            if (StringUtils.isEmpty(pageSize) || StringUtils.isEmpty(nextPage)) {
                resultMap.put("errMsg", "参数错误！");
            }

            Page<Map<String, Object>> page = new Page<>(nextPage, pageSize);
            IPage<Map<String, Object>> dictionaryInfos = dictionaryInfoMapper.queryDictionaryInfos(page, dicTypeCode, dicValue, delFlag);
            List<Map<String, Object>> list = dictionaryInfos.getRecords();

            if (!list.isEmpty()) {
                resultMap.put("total", dictionaryInfos.getTotal());
                resultMap.put("current", dictionaryInfos.getCurrent());
                resultMap.put("pages", dictionaryInfos.getPages());
            }

            resultMap.put("list", list);
            resultMap.put("success", true);

        } catch (Exception e) {
            resultMap.put("error", "系统异常！");
        }
        return resultMap;
    }

    /**
     * 新增字典
     */
    @Transactional
    @RequestMapping(value = "/addDictionaryInfo")
    public Map<String, Object> addDictionaryInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer dicTypeCode = object.getInteger("dicTypeCode");
            String dicValue = object.getString("dicValue");

            //获取当前登录人员信息

            //系统时间
            Date sysTime = new Date();

            //字典重复检查
            Long isExist = dictionaryInfoMapper.selectCount(new QueryWrapper<DictionaryInfo>()
                    .eq("dicTypeCode", dicTypeCode)
                    .eq("dicValue", dicValue));
            if (isExist > 0) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "您输入的字典已存在！");
            } else {
                //获取当前字典类型最大字典编码
                DictionaryInfo dictionaryInfo = dictionaryInfoMapper.selectOne(new QueryWrapper<DictionaryInfo>()
                        .eq("dicTypeCode", dicTypeCode)
                        .select("MAX(dicCode) AS dicCode", "dicType"));

                Integer dicCode = 0;
                if (dictionaryInfo != null) {
                    dicCode = dictionaryInfo.getDicCode() + 1;
                }
                String dicType = dictionaryInfo.getDicType();

                //字典信息
                DictionaryInfo dictionaryInfo1 = new DictionaryInfo();
                dictionaryInfo1.setDicType(dicType);
                dictionaryInfo1.setDicTypeCode(dicTypeCode);
                dictionaryInfo1.setDicCode(dicCode);
                dictionaryInfo1.setDicValue(dicValue);
                dictionaryInfo1.setDelFlag("0");
                //dictionaryInfo1.setInsertUser();
                dictionaryInfo1.setInsertTime(sysTime);
                //dictionaryInfo1.setUpdateUser();
                dictionaryInfo1.setUpdateTime(sysTime);

                //字典表插入
                dictionaryInfoMapper.insert(dictionaryInfo1);

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

    /**
     * 修改字典
     */
    @Transactional
    @RequestMapping(value = "/editDictionaryInfo")
    public Map<String, Object> editDictionaryInfo(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            Integer dicTypeCode = object.getInteger("dicTypeCode");
            String dicValue = object.getString("dicValue");

            //获取当前登录人员信息

            //系统时间
            Date sysTime = new Date();

            //字典重复检查
            Long isExist = dictionaryInfoMapper.selectCount(new QueryWrapper<DictionaryInfo>()
                    .eq("dicTypeCode", dicTypeCode)
                    .eq("dicValue", dicValue)
                    .ne("keyId", keyId));
            if (isExist > 0) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "您输入的字典内容已存在！");
            } else {
                DictionaryInfo dictionaryInfo = new DictionaryInfo();
                dictionaryInfo.setKeyId(keyId);
                dictionaryInfo.setDicValue(dicValue);
//            dictionaryInfo.setUpdateUser();
                dictionaryInfo.setUpdateTime(sysTime);

                //字典表更新
                dictionaryInfoMapper.updateById(dictionaryInfo);
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

    /**
     * 字典启用/停用
     */
    @Transactional
    @RequestMapping(value = "/editDictionaryDelFlag")
    public Map<String, Object> editDictionaryDelFlag(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            Integer keyId = object.getInteger("keyId");
            String delFlag = object.getString("delFlag");

            //获取当前登录人员信息

            //系统时间
            Date sysTime = new Date();

            if ("0".equals(delFlag) || "1".equals(delFlag)) {
                DictionaryInfo dictionaryInfo = new DictionaryInfo();
                dictionaryInfo.setKeyId(keyId);
                dictionaryInfo.setDelFlag(delFlag);
//                dictionaryInfo.setUpdateUser();
                dictionaryInfo.setUpdateTime(sysTime);

                //字典表更新
                dictionaryInfoMapper.updateById(dictionaryInfo);
                resultMap.put("success", true);
            } else {
                resultMap.put("error", false);
                resultMap.put("errMsg", "非法参数！");
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
