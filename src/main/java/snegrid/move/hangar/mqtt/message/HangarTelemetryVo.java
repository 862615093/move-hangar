package snegrid.move.hangar.mqtt.message;

import lombok.*;

/**
 * 移动机库 返回页面遥测
 *
 * @author wangwei
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HangarTelemetryVo {

    private int deviceStatus;

    //电池电量
    private int batteryPercent11;

    //电池电量
    private int batteryPercent12;

    //温度
    private int weatherTemperature;

    //风向
    private int weatherWindDirection;

    //湿度
    private int weatherHumidity;

    //风速
    private int weatherWindSpeed;

    //雨量
    private int weatherRainSpeed;

    //移动机库电池电量SOC
    private int moveBatteryPercent;

    //车辆水平角度
    private int vehicleLevelAngle;

    //车辆倾斜角度
    private int vehicleTiltAngle;
}
