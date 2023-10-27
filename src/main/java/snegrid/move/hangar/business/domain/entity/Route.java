package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import snegrid.move.hangar.constant.Type;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

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
}
