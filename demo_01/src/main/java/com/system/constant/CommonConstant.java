package com.system.constant;

/**
 * @author xrx
 * @since 2023/3/29
 * description TODO
 */
public class CommonConstant {
    /**
     * 设备状态
     */
    // 在库
    public static final Integer EQUIPMENT_FREE = 0;
    // 派送中
    public static final Integer EQUIPMENT_SENDING = 1;
    // 使用中
    public static final Integer EQUIPMENT_USING = 2;
    // 报废
    public static final Integer EQUIPMENT_SCRAPED = 3;

    /**
     * 审批状态
     */
    // 未审批
    public static final Integer APPLICATION_APPROVING = 1;
    // 审批通过
    public static final Integer APPLICATION_APPROVED_SUCCESS = 2;
    // 审批未通过
    public static final Integer APPLICATION_APPROVED_ERROR = 3;

    /**
     * 接收状态
     */
    // 未接收
    public static final Integer EQUIPMENT_UNRECEIVED = 1;
    // 已接收
    public static final Integer EQUIPMENT_RECEIVED = 2;
}
