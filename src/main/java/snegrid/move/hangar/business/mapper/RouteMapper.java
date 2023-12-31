package snegrid.move.hangar.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import snegrid.move.hangar.business.domain.entity.Route;
import snegrid.move.hangar.business.domain.entity.RouteUserRel;

/**
 * <p>
 * 航线表 Mapper 接口
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Mapper
public interface RouteMapper extends BaseMapper<Route> {

}
