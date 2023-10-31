package snegrid.move.hangar.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.domain.entity.RouteUserRel;
import snegrid.move.hangar.business.mapper.RouteUserRelMapper;
import snegrid.move.hangar.business.service.IRouteUserRelService;

/**
 * <p>
 * 用户航线操作关联表 服务实现类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-31
 */
@Service
public class RouteUserRelServiceImpl extends ServiceImpl<RouteUserRelMapper, RouteUserRel> implements IRouteUserRelService {

}
