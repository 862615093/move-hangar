package snegrid.move.hangar.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "FlyTaskDTO对象", description = "开始飞行任务入参")
public class FlyTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("航线ID")
    @NotNull(message = "航线ID不能为空")
    private Long routeId;
}
