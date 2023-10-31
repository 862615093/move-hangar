package snegrid.move.hangar.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.domain.entity.RoutePoint;
import snegrid.move.hangar.business.mapper.RoutePointMapper;
import snegrid.move.hangar.business.service.IRoutePointService;

/**
 * <p>
 * 航点表 服务实现类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-30
 */
@Service
@AllArgsConstructor
public class RoutePointServiceImpl extends ServiceImpl<RoutePointMapper, RoutePoint> implements IRoutePointService {

}
