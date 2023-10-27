package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import snegrid.move.hangar.constant.Type;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Getter
@Setter
@TableName("t_device")
@ApiModel(value = "Device对象", description = "设备表")
public class Device extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = {Type.Edit.class})
    private Long id;

    @ApiModelProperty("设备配对ID")
    private Long matchId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备类型: 0-固定机场,1-无人机,2-移动机场")
    private Integer type;

    @ApiModelProperty("设备号")
    private String deviceNumber;

    @ApiModelProperty("设备型号")
    private String deviceModel;

    @ApiModelProperty("设备管理员ID")
    private Long deviceManagerId;

    @TableField(exist = false)
    @ApiModelProperty("设备管理员名称")
    private String deviceManagerName;

    @ApiModelProperty("设备所属单位ID")
    private Long deviceDeptId;

    @TableField(exist = false)
    @ApiModelProperty("设备所属单位名称")
    private String deviceDeptName;

    @ApiModelProperty("设备的 ip 地址")
    private String ip;

    @ApiModelProperty("实时视频 地址 1")
    private String liveVideoUrlOne;

    @ApiModelProperty("实时视频 地址 2")
    private String liveVideoUrlTwo;

    @ApiModelProperty("实时视频 地址 3")
    private String liveVideoUrlThree;

    @ApiModelProperty("实时视频 地址 4")
    private String liveVideoUrlFour;
}
