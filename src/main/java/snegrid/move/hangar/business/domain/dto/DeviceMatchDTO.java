package snegrid.move.hangar.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DeviceMatchDTO对象", description = "设备匹配入参")
public class DeviceMatchDTO {

    @ApiModelProperty("机库ID")
    @NotNull(message = "机库ID不能为空")
    private Long hangarId;

    @ApiModelProperty("无人机ID")
    @NotNull(message = "无人机ID不能为空")
    private Long droneId;

    @ApiModelProperty("是否匹配：0-解除匹配，1-匹配")
    @NotNull(message = "无人机ID不能为空")
    private Integer matchStatus;
}
