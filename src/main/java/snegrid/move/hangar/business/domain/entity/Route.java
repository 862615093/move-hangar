package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import snegrid.move.hangar.constant.Type;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 航线表
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Getter
@Setter
@Builder
@TableName("t_route")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Route对象", description = "航线表")
public class Route extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = {Type.Edit.class})
    private Long id;

    @ApiModelProperty("机库ID")
    private Long hangarId;

    @ApiModelProperty("航线名称")
    private String routeName;

    /**********************************************************关联属性************************************************/

    @TableField(exist = false)
    @ApiModelProperty("航线URL")
    private String fileUrl;

    @TableField(exist = false)
    @ApiModelProperty("航线航点信息")
    private List<RoutePoint> routePointList;
}
