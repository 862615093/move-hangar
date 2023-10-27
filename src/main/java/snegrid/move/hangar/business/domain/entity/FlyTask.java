package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import snegrid.move.hangar.constant.Type;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 飞行任务表
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_fly_task")
@ApiModel(value = "FlyTask对象", description = "飞行任务表")
public class FlyTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @NotNull(message = "id不能为空", groups = {Type.Edit.class})
    private String id;

    @ApiModelProperty("机库ID")
    private Long hangarId;

    @ApiModelProperty("无人机ID")
    private Long droneId;

    @ApiModelProperty("航线ID")
    private Long routeId;

    @ApiModelProperty("任务开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskStartTime;

    @ApiModelProperty("任务结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskEndTime;

    @ApiModelProperty("飞行开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime flyStartTime;

    @ApiModelProperty("飞行结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime flyEndTime;

    @ApiModelProperty("任务是否中断（0:否 1:是）")
    private Boolean isInterrupt;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("创建者")
    @TableField(exist = false)
    private String createByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty("更新者")
    @TableField(exist = false)
    private String updateByName;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否有效", hidden = true)
    @JsonIgnore
    private Boolean valid;
}
