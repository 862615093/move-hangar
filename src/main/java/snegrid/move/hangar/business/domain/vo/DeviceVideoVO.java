package snegrid.move.hangar.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DeviceVideoVO对象", description = "设备视频出参")
public class DeviceVideoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("机库视频集合")
    private List<String> hangarVideoList;

    @ApiModelProperty("无人机视频集合")
    private List<String> droneVideoList;
}
