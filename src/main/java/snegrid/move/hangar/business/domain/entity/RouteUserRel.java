package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作航线关联表  t_route_user_rel
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_route_user_rel")
public class RouteUserRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;

    @NotNull(message = "航线ID不能为空")
    @ApiModelProperty("航线ID")
    private String routeId;

    @ApiModelProperty("最近一次选择航线时间")
    @NotNull(message = "最近一次选择航线时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSelectTime;
}
