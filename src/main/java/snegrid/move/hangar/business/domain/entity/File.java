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
 * 附件表
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Getter
@Setter
@Builder
@TableName("t_file")
@ApiModel(value = "File对象", description = "附件表")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @NotNull(message = "id不能为空", groups = {Type.Edit.class})
    private String id;

    @ApiModelProperty("附件所属业务主表id")
    private Long relId;

    @ApiModelProperty("附件所属业务类型")
    private String relType;

    @ApiModelProperty("附件所属业务主表uuid")
    private String relUuid;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty("上传人")
    private Long uploadUserId;
}
