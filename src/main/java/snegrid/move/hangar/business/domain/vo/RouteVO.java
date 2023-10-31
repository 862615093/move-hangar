package snegrid.move.hangar.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("机库ID")
    private Long hangarId;

    @ApiModelProperty("航线名称")
    private String routeName;

    @ApiModelProperty("航线文件URL")
    private String routeFileUrl;

    @ApiModelProperty("创建者")
    private Long createBy;

    @ApiModelProperty("创建者")
    @TableField(exist = false)
    private String createByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty("更新者")
    @TableField(exist = false)
    private String updateByName;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
