package snegrid.move.hangar.mqtt.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class McsRoute {

    /**
     * 飞行任务记录ID
     */
    private String historyBizId;

    /**
     * 维度
     */
    private Double elpLatitude;

    /**
     * 经度
     */
    private Double elpLongitude;

    /**
     * 低电量返回参数
     */
    private Integer minBattery;

    /**
     * 任务分类
     * 0：普通光伏巡检模式
     * 2：风机巡检模式
     * 4：线路巡检模式
     */
    private Integer category;

    /**
     * kmz文件列表
     */
    private List<String> kmzFiles;
}