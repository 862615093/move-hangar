package snegrid.move.hangar.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import snegrid.move.hangar.business.domain.entity.RouteUserRel;

/**
 * <p>
 * 用户航线操作关联表 Mapper 接口
 * </p>
 *
 * @author wangwei
 * @since 2023-10-31
 */
@Mapper
public interface RouteUserRelMapper extends BaseMapper<RouteUserRel> {

}
