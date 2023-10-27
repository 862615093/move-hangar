package snegrid.move.hangar.mqtt.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.*;

/**
 * 移动机库遥测
 *
 * @author wangwei
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HangarTelemetryMessage {

    /**
     * 机场状态  -1; //离线
     * NeedReset = 0; //未知
     * Standby = 10; //待命
     * Preparing = 20; //准备中
     * Connecting = 30; //建立连接中
     * WaitingFly = 40; //等待起飞
     * ShowDroneTakeoffFail = 42;
     * // 显示-等待起飞超时（重试起飞-发送飞机起飞指令；任务结束-回收流程；倒计时结束-回收流程）
     * WaitingRTL = 50; //等待返航
     * WaitingLand = 60; //等待降落
     * Recycling = 70; //回收中
     * Resetting = 100; //复位中
     */
    @JSONField(name = "40025")
    private int deviceStatus;

    //电池电量
    @JSONField(name = "40028")
    private int batteryPercent11;

    //电池电量
    @JSONField(name = "40029")
    private int batteryPercent12;

    //温度
    @JSONField(name = "40036")
    private int weatherTemperature;

    //风向
    @JSONField(name = "40037")
    private int weatherWindDirection;

    //湿度
    @JSONField(name = "40038")
    private int weatherHumidity;

    //风速
    @JSONField(name = "40039")
    private int weatherWindSpeed;

    //雨量
    @JSONField(name = "40040")
    private int weatherRainSpeed;

    //移动机库电池电量SOC
    @JSONField(name = "40041")
    private int moveBatteryPercent;

    //车辆水平角度
    @JSONField(name = "40042")
    private int vehicleLevelAngle;

    //车辆倾斜角度
    @JSONField(name = "40043")
    private int vehicleTiltAngle;

    //机场电池温度
    private int droneBoxTemperature1;

    //电控柜温度
    private int droneBoxTemperature2;

    //电气柜温度
    private int droneBoxTemperature3;

    //机场潮湿度
    private int droneBoxHumidity1;

    //电控柜潮湿度
    private int droneBoxHumidity2;

    //电气柜潮湿度
    private int droneBoxHumidity3;

    //机场是否故障
    private int isDroneBoxError;

    //机场是否运行中
    private int isDroneBoxRunning;
}
