package snegrid.move.hangar.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "FlyTaskPicListDTO对象", description = "获取指定飞行任务航拍图入参")
public class FlyTaskPicListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("飞行任务ID")
    @NotBlank(message = "飞行任务ID不能为空")
    private String flyTaskId;
}
