package snegrid.move.hangar.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 指令集合
 *
 * @author zhaojl
 */
@Getter
@AllArgsConstructor
public enum OrderMuster {


    //================机场=================
    /**
     * ：做好飞机起飞前的工作
     */
    DRONE_BOX_PREPARE(2000, "机场准备", 0, 0, "机场流程"),

    DRONE_BOX_STOP(2002, "机场急停", 0, 0, "机场流程"),

    /**
     * 初始化机场
     */
    DRONE_BOX_RESET(2004, "机场复位", 0, 0, "机场流程"),

    /**
     * 初始化机场
     */
    DRONE_BOX_CANCEL_TASK(2003, "取消飞行", 0, 0, "机场流程"),
    /**
     * 飞往紧急降落点后机场复位
     */
    DRONE_BOX_EMERGENCY_BACK(2008, "急停回收", 0, 0, "机场流程"),
    /**
     * 一键飞行
     */
    DRONE_BOX_ONE_CLICK_FLIGHT(4000, "一键飞行", 0, 1, "飞行任务流程"),

    DRONE_BOX_ONE_CLICK_FLIGHT_2(4002, "一键飞行", 0, 1, "飞行任务流程"),

    DRONE_BOX_START_RTK(2023, "开启RTK基站", 0, 0, "机场流程"),

    DRONE_BOX_STOP_RTK(2026, "关闭RTK基站", 0, 0, "机场流程"),

    //===========无人机====================
    /**
     * osdk&msdk
     */
    DRONE_YUNTAI_UP(3000, "无人机云台向上", 0, 1, "摇杆控制"),
    DRONE_YUNTAI_DOWN(3002, "无人机云台向下", 0, 1, "摇杆控制"),
    DRONE_YUNTAI_LEFT(3004, "无人机云台向左", 0, 1, "摇杆控制"),
    DRONE_YUNTAI_RIGHT(3006, "无人机云台向右", 0, 1, "摇杆控制"),
    DRONE_YUNTAI_CENTER(3008, " 无人机云台归中", 0, 1, "摇杆控制"),
    DRONE_YUNTAI_AUTO_FOCUS(3010, "无人机云台自动对焦", 0, 1, "摇杆控制"),
    DRONE_YUNTAI_PHOTOGRAPH(3012, "无人机云台拍照", 0, 1, "摇杆控制"),
    /**
     * 参数范围为 1~30
     */
    DRONE_MAGNIFICATION(3204, "放大倍数", 0, 1, "摇杆控制"),
    DRONE_START_RECORDING_VIDEO(3016, " 视频录制开始", 0, 1, "摇杆控制"),
    DRONE_END_RECORDING_VIDEO(3018, " 视频录制结束", 0, 1, "摇杆控制"),
    DRONE_FLY_FRONT(3100, "无人机向前飞", 0, 1, "摇杆控制"),
    DRONE_FLY_AFTER(3102, " 无人机向后飞", 0, 1, "摇杆控制"),
    DRONE_FLY_LEFT(3104, " 无人机向左飞", 0, 1, "摇杆控制"),
    DRONE_FLY_RIGHT(3106, "无人机向右飞", 0, 1, "摇杆控制"),
    DRONE_FLY_TURN_LEFT(3108, " 无人机向左转", 0, 1, "摇杆控制"),
    DRONE_FLY_TURN_RGIHT(3110, " 无人机向右转", 0, 1, "摇杆控制"),
    DRONE_FLY_DOWN(3112, "无人机向上飞", 0, 1, "摇杆控制"),
    DRONE_FLY_UP(3114, " 无人机向下飞", 0, 1, "摇杆控制"),
    DRONE_SWITCH_MANUAL(3200, " 切换手动", 1, 1, "摇杆控制"),
    DRONE_SWITCH_AUTO(3202, "切换自动", 1, 1, "摇杆控制"),
    /**
     * 仅 msdk 有效
     */
    DRONE_PAGER_START(3212, "喊话器开始", 0, 1, "喊话器控制"),
    /**
     * 仅 msdk 有效，参数范围 0~10
     */
    DRONE_PAGER_VOLUME(3214, "喊话器音量设置", 0, 1, "喊话器控制"),
    /**
     * 以下描述为：仅 msdk 有效
     */
    DRONE_PAGER_STOP(3218, " 喊话器停止", 0, 1, "喊话器控制"),
    DRONE_SWITCH_VISIBLE(3301, "切换可见光", 0, 1, "飞机控制"),
    DRONE_SWITCH_INFRARED(3302, "切换红外", 0, 1, "飞机控制"),
    DRONE_SHUT_PICTURE_IN_PICTURE(3303, "关闭画中画", 0, 1, "飞机控制"),
    DRONE_SWITCH_PICTURE_IN_PICTURE(3304, "切换画中画", 0, 1, "飞机控制"),
    /* DRONE_TX2_OPEN(3330,"开tx2电源",0,1,"机场流程"),
     DRONE_TX2_SHUT(3332,"关tx2电源",0,1,"机场流程"),*/
    DRONE_CLEAR_SDCARD(3900, "清空SD卡", 0, 1, "文件管理"),
    DRONE_FLIE_TRANSMISSION_START(3906, "清理飞行数据", 0, 1, "文件管理"),
    DRONE_FLIE_TRANSMISSION_END(3908, "停止文件传输", 0, 1, "文件管理"),

    /**
     * 演示飞行 起飞
     */
    DEMO_FLY_START_TASK(9999, "演示飞行", 0, 1, "飞行控制"),

    /**
     * 暂停任务
     */
    DRONE_SUSPEND_TASK(4016, "暂停任务", 0, 1, "飞行控制"),

    /**
     * 开始任务
     */
    DRONE_START_TASK(4018, "恢复任务", 0, 1, "飞行控制"),

    /**
     * 结束任务
     */
    DRONE_STOP_TASK(4022, "结束任务", 0, 1, "飞行控制"),

    /**
     * 命令飞机返回
     */
    DRONE_AUTO_RETURN(4004, "自动返航", 0, 1, "飞行控制"),


    DRONE_TURN_ON__THE_BOOM_LIGHTS(3334, "开启机臂灯", 0, 1, "飞机控制"),
    DRONE_TURN_OFF_THE_BOOM_LIGHTS(3336, "关闭机臂灯", 0, 1, "飞机控制"),
    /**
     * 参数为变焦值（范 围为 0-100）
     */
    DRONE_TURN_ON_THE_SEARCHLIGHT(3338, "开启探照灯", 0, 1, "飞机控制"),
    DRONE_TURN_OFF_THE_SEARCHLIGHT(3340, "关闭探照灯", 0, 1, "飞机控制"),
    /**
     * 参数依次为 x1 y1 x2 y2（范围为 0-1 的比例值 且左 上角为 0 点坐标）
     **/
    DRONE_TURN_ON_TEMPERATURE(3342, "开启测温 ", 0, 1, "飞机控制"),
    DRONE_TURN_OFF_TEMPERATURE(3344, "关闭测温", 0, 1, "飞机控制"),
    /**
     * 参数依次为 x y（范围 为 0-1000 的比例值 且左上角 为 0 点坐标
     */
    DRONE_START_RANGING(3346, "开启测距", 0, 1, "飞机控制"),
    DRONE_TURN_OFF_RANGING(3348, "关闭测距", 0, 1, "飞机控制");


    private static final Map<Integer, OrderMuster> ORDER_MAP = new HashMap<Integer, OrderMuster>();

    static {
        for (OrderMuster s : EnumSet.allOf(OrderMuster.class)) {
            ORDER_MAP.put(s.getMsgType(), s);
        }
    }

    public static OrderMuster getByMsgType(int value) {
        return ORDER_MAP.get(value);
    }

    /**
     * 消息类型 code
     */
    private Integer msgType;

    /**
     * 功能
     */
    private String function;

    /**
     * 模式 0：异步 1：同步
     */
    private Integer modelType;

    /**
     * 设备类型  0：机场，1：无人机
     */
    private Integer deviceType;

    /**
     * 指令类别
     */
    private String cmdType;
}
