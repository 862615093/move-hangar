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
@ApiModel(value = "DevicePageListDTO对象", description = "设备查询列表入参")
public class DevicePageListDTO extends BaseEntity {

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型")
    private Integer type;
}
