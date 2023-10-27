package snegrid.move.hangar.business.domain.vo;

import lombok.*;

/**
 * 【请填写功能名称】
 *
 * @author zhaojinglong
 * @date 2023-03-27
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HangerDevice {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型，0：机场，1：无人机
     */
    private Integer type;

    /**
     * 设备号，唯一
     */
    private String deviceNumber;

    /**
     * 型号(字典表)
     */
    private Long modelNumber;

    /**
     * 型号名称
     */
    private String modelNumberName;

    /**
     * 设备的 ip 地址
     */
    private String ip;

    /**
     * 实时视频 地址 1
     */
    private String liveVideoUrlOne;

    /**
     * 实时视频 地址 2
     */
    private String liveVideoUrlTwo;

    /**
     * 实时视频 地址 3
     */
    private String liveVideoUrlThree;

    /**
     * 实时视频 地址 4
     */
    private String liveVideoUrlFour;

    /**
     * http 文件 地址
     */
    private String httpFileRootUrl;

    /**
     * 配对的设备id
     */
    private Long matchDeviceId;
}
