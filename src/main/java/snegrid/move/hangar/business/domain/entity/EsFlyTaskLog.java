package snegrid.move.hangar.business.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsFlyTaskLog {

    @ApiModelProperty("飞行任务ID")
    private String flyTaskId;

    @ApiModelProperty("日志来源: 1-机库日志, 2-无人机日志, 3-大疆日志, 4-机库平台日志")
    private Integer logSource;

    @ApiModelProperty("日志编码")
    private Integer logCode;

    @ApiModelProperty("日志等级")
    private Integer logLevel;

    @ApiModelProperty("日志内容")
    private String logContent;

    @ApiModelProperty("创建时间")
    private String logCreateTime;
}
