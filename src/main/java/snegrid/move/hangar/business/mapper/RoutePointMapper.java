package snegrid.move.hangar.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import snegrid.move.hangar.business.domain.entity.RoutePoint;

/**
 * <p>
 * 航点表 Mapper 接口
 * </p>
 *
 * @author wangwei
 * @since 2023-10-30
 */
@Mapper
public interface RoutePointMapper extends BaseMapper<RoutePoint> {

}
