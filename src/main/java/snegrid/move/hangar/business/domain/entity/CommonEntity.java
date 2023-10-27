package snegrid.move.hangar.business.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公共实体参数
 *
 * @author wangwei
 */
@Data
public class CommonEntity {

    @ApiModelProperty("创建者")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty("创建者")
    @TableField(exist = false)
    private String createByName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty("更新者")
    @TableField(exist = false)
    private String updateByName;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否有效", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Boolean valid;
}