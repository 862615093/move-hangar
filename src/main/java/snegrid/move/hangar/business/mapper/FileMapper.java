package snegrid.move.hangar.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import snegrid.move.hangar.business.domain.entity.File;

/**
 * <p>
 * 附件表 Mapper 接口
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
