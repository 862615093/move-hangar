package snegrid.move.hangar.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "RouteListDTO对象", description = "航线查询列表入参")
public class RouteListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("航线ID")
    private Long id;

    @ApiModelProperty("航线名称")
    private String routeName;

    @ApiModelProperty("机库ID")
    private Long hangarId;
}
