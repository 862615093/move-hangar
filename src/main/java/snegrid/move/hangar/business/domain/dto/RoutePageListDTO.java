package snegrid.move.hangar.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import snegrid.move.hangar.base.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "RoutePageListDTO对象", description = "航线查询列表入参")
public class RoutePageListDTO extends BaseEntity {

    @ApiModelProperty("航线ID")
    private Long id;

    @ApiModelProperty("航线名称")
    private String routeName;

    @ApiModelProperty("机库ID")
    private Long hangarId;
}
