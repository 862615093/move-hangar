package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 航点表
 * </p>
 *
 * @author wangwei
 * @since 2023-10-30
 */
@Getter
@Setter
@TableName("t_route_point")
@ApiModel(value = "RoutePoint对象", description = "航点表")
public class RoutePoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("关联航线ID")
    private Long routeId;

    @ApiModelProperty("点在线路中的序号从0开始")
    private Integer pointNumber;

    @ApiModelProperty("经度 wgs84 坐标系")
    private Double longitudeWgs84;

    @ApiModelProperty("维度 wgs84坐标系")
    private Double latitudeWgs84;

    @ApiModelProperty("相对高度/米")
    private Float height;
}
