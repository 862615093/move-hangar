package snegrid.move.hangar.mqtt.message;

import lombok.Data;
import lombok.ToString;

/**
 * 无人机 页面遥测
 *
 * @author wangwei
 */
@Data
@ToString
public class DroneTelemetryVo {

    private Boolean uavConnect;

    private String uavSerialNumber;

    //飞行总时长，总体飞行时长，单位：秒。飞行器断电后不会清零。
    private Double uavAircraftTotalFlightDuration;

    //飞行总距离，总体飞行距离，单位：米。飞行器断电后不会清零。
    private Double uavAircraftTotalFlightDistance;

    //飞行总次数，总体飞行次数，飞行器断电后不会清零。
    private Integer uavAircraftTotalFlightTimes;

    //飞行时间  自飞行器电机起转以来累计的飞行时间，单位：0.1秒。该数值在电池重新上电后才会清零。
    private Integer uavFlightTimeInSeconds;

    //预估剩余飞行时间单位：秒。
    //预估剩余飞行时间是飞行器在剩余10%的电池电量的情况下返航所需的时间，其中包括飞行器降落着陆的时间。
    private Integer uavRemainingFlightTime;

    //预估返航需要的时间，单位：秒。
    //飞行器从当前位置返回返航点所需的估计时间。
    private Integer uavTimeNeededToGoHome;

    //低电量返航时的电池剩余电量的百分比。
    //当电池剩余电量百分比小于这个数值的时候，会提示低电量返航。
    private Integer uavBatteryPercentNeededToGoHome;

    //以返航点为圆心的最大安全飞行半径，单位：米。
    //飞行器可以安全返回返航点的最大飞行半径，该值是通过飞行器的高度、跟返航点的距离、电池剩余电量等信息综合计算得出。
    //如果飞行器飞行的距离超过这个半径，飞行器将可能在返航途中降落着陆。
    //如果飞行器正在使用模拟器，则该值将为0。
    private Double uavMaxRadiusCanFlyAndGoHome;

    //起飞海拔高度，飞行器起飞时的海拔高度，单位为米。
    private Double uavTakeoffLocationAltitude;

    //飞行日志的索引
    private Integer uavFlightLogIndex;

    //飞行模式
    private String uavFlightMode;

    //仿地飞行模式
    private Boolean uavTerrainFollowMode;

    //region 状态
    //电机是否工作
    private Boolean uavAreMotorsOn;

    //是否飞行中
    private Boolean uavIsFlying;

    //是否已达到最大飞行高度
    private Boolean uavIsNearHeightLimit;

    //当前电池是否处于低电量状态。
    private Boolean uavIsLowBatteryWarning;

    //当前电池是否处于严重低电量状态。
    private Boolean uavIsSeriousLowBatteryWarning;

    //位置数据（GPS模块的地理位置）
    private Boolean uavLocationLatitude;
    private Boolean uavLocationLongitude;
    private Boolean uavLocationAltitude;

    //姿态数据
    private Boolean uavAttitudePitch;
    private Double uavAttitudeRoll;
    private Double uavAttitudeYaw;

    //飞行速度
    private Double uavVelocityX;
    private Double uavVelocityY;
    private Double uavVelocityZ;

    //是否失控中
    private Boolean uavIsFailSafe;

    //失控行为。
    private String uavFailsafeAction;

    //是否返航点已设置
    private Boolean uavIsHomeLocationSet;

    //返航时的相对高度，单位：米。
    private Integer uavGoHomeHeight;

    //返航状态
    private String uavGoHomeStatus;

    //返航点位置
    private Double uavHomeLocationLatitude;
    private Double uavHomeLocationLongitude;

    //降落点位置
    private Double uavLandLocationLatitude;
    private Double uavLandLocationLongitude;

    //region 传感器
    //GPS卫星个数，通常在高空飞行能达到12颗星以上，时段良好时能达到18~19颗。
    private Integer uavGpsSatelliteCount;

    //GPS信号等级
    private String uavGpsSignalLevel;

    //指南针个数
    private Integer uavCompassCount;

    //指南针朝向，单位：度。正北为0度，正东为90度，范围：[-180,180]。
    private Double uavCompassHeading;

    //指南针数据是否错误，
    //当飞行器在强干扰或磁场区域使用时，可能出现指南针数据错误。
    //需要调用KeyStartCompassCalibration进行指南针校准。
    private Boolean uavCompassHasError;

    //IMU个数
    private Integer uavImuCount;

    //风速等级
    private String uavWindWarning;

    //当前风速，单位：dm/s。
    private Integer uavWindSpeed;

    //当前风向，采用世界坐标系。
    private String uavWindDirection;

    /********************************************* 遥控器 ********************************************************/
    private Boolean rcConnect;

    //型号
    private String rcType;

    //序列号
    private String rcSerialNumber;

    //档位模式
    private String rcFlightMode;

    //控制模式
    private String rcControlMode;

    private Boolean rcBatteryEnabled;
    private Integer rcBatteryPower;
    private Integer rcBatteryPercent;

    private Boolean rcSecondBatteryEnabled;
    private Integer rcSecondBatteryPower;
    private Integer rcSecondBatteryPercent;

    /******************************************************** 电池 *************************************************/

    //region 电池1（左）componentIndex 0
    private Boolean batteryLeftConnect;
    private String batteryLeftSerialNumber;

    //满电容量，单位：mAh。
    // 随着电池的持续使用，充满电时电池的容量会随着时间而减小。
    private Integer batteryLeftFullChargeCapacity;

    //获取电池剩余电量百分比。
    // 如果你需要获取所有电池的总电量百分比，
    // 可以通过设置不同的 componentIndex 获取到所有的电池剩余电量和电池容量进行计算获取。
    private Integer batteryLeftChargeRemainingInPercent;

    //电池温度，单位：摄氏度。
    // 范围区间：[-128, 127]
    private Double batteryLeftBatteryTemperature;

    //电池的电压，单位：mV。
    private Integer batteryLeftVoltage;

    //实时电流消耗，单位：mA。
    // 负值表示电池正在放电，正值表示正在充电。
    private Integer batteryLeftCurrent;

    //总放电次数。
    private Integer batteryLeftNumberOfDischarges;

    //region 电池2（右）componentIndex 1
    private Boolean batteryRightConnect;
    private String batteryRightSerialNumber;

    //满电容量，单位：mAh。
    //随着电池的持续使用，充满电时电池的容量会随着时间而减小。
    private Integer batteryRightFullChargeCapacity;

    //获取电池剩余电量百分比。
    //如果你需要获取所有电池的总电量百分比，
    //可以通过设置不同的 componentIndex 获取到所有的电池剩余电量和电池容量进行计算获取。
    private Integer batteryRightChargeRemainingInPercent;

    //电池温度，单位：摄氏度。
    //范围区间：[-128, 127]
    private Double batteryRightBatteryTemperature;

    //电池的电压，单位：mV。
    private Integer batteryRightVoltage;

    //实时电流消耗，单位：mA。
    //负值表示电池正在放电，正值表示正在充电。
    private Integer batteryRightCurrent;

    //总放电次数。
    private Integer batteryRightNumberOfDischarges;

    /******************************************************** 图传 ************************************************/

    private Boolean airLinkTypeConnect;

    private String airLinkType;

    //图传信号质量，单位：百分比。
    // 如果获取到的信号质量小于40%，则表示当前信号质量差；
    // 如果获取到的信号质量在40%和60%之间，则表示当前信号质量普通；
    // 如果获取到的信号质量大于60%，则表示当前的信号质量好。
    private Integer airLinkSignalQuality;

    /******************************************************** 相机 *************************************************/

    private Boolean cameraConnection;

    //是否正在拍照
    private Boolean cameraIsShootingPhoto;

    //相机类型
    private String cameraType;

    //相机模式
    //包括拍照和录像模式。
    private String cameraMode;

    //当前镜头（视频源）
    private String cameraVideoStreamSource;

    //变焦倍率（缩放镜头）
    //建议设置的最小精度为0.1。
    private Double cameraZoomRatios;

    //变焦倍率（红外镜头）
    //支持1x、2x、4x、8x等变焦倍率。
    private Double cameraThermalZoomRatios;

    /******************************************************** 云台 **************************************************/

    private Boolean gimbalConnection;

    //云台模式
    private String gimbalMode;

    //姿态 - 俯仰值
    private Double gimbalAttitudePitch;

    //姿态 - 偏航值（北东地坐标系）
    private Double gimbalAttitudeYaw;

    //姿态 - 横滚值
    private Double gimbalAttitudeRoll;

    //姿态 - 偏航值（相对于飞行器机头朝向的偏航角度）
    private Double gimbalYawRelativeToAircraftHeading;

    /********************************************************** RTK *************************************************/

    //RTK模块与飞控系统的连接状态。默认为true，只有当飞机上的RTK模块发生故障的时候才会变成false
    private Boolean rtkConnection;

    private Boolean rtkEnabled;

    //RTK模块是否收敛
    private Boolean rtkHealthy;

    //RTK错误信息
    private String rtkError;

    //RTK模块数据源
    //QX_NETWORK_SERVICE	千寻网络RTK数据源。
    //NTRIP_NETWORK_SERVICE	NTRIP网络RTK数据源。如需使用CMCC网络RTK请设置为此类型。
    //CUSTOM_NETWORK_SERVICE	自定义网络RTK数据源。
    //BASE_STATION	基站数据源。
    private String rtkStationSource;

    //RTK模块的定向及定位状态
    //NONE	没有RTK定位。可能原因如下：视野中的卫星数量不足、锁定卫星的时间不足或者飞行器和地面基站之间的通信链路丢失。
    //SINGLE_POINT	Single Point状态。
    //FLOAT	Float状态。
    //FIXED_POINT	Fixed Point状态，此状态最精准。
    private String rtkPositioningSolution;

    //RTK位置
    private Double rtkLocationLatitude;
    private Double rtkLocationLongitude;
    private Double rtkLocationAltitude;

    //RTK&GPS融合后位置
    private Double rtkRealLocationLatitude;
    private Double rtkRealLocationLongitude;
    private Double rtkRealLocationAltitude;

    //RTK基站位置
    private Double rtkBaseStationLocationLatitude;
    private Double rtkBaseStationLocationLongitude;
    private Double rtkBaseStationLocationAltitude;

    //IDLE 空闲
    //SCANNING 扫描汇总
    //CONNECTING 连接中
    //CONNECTED 已连接
    //DISCONNECTED 未连接
    //UNKNOWN 未知
    private String rtkStationConnectState;

    /****************************************************** 航线任务 ***************************************************/

    //任务执行状态
    private String missionExecuteState;

    //航线ID
    private Integer missionWayLineID;

    //航线任务文件名
    private String missionFileName;

    //当前执行的航点序号
    private Integer missionCurrentWaypointIndex;

    //航线执行中断原因
    private String missionExecutingInterruptReason;
}
