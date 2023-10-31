package snegrid.move.hangar.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.business.domain.dto.FlyTaskPicListDTO;
import snegrid.move.hangar.business.domain.dto.RouteListDTO;
import snegrid.move.hangar.business.domain.dto.RoutePageListDTO;
import snegrid.move.hangar.business.domain.entity.Route;
import snegrid.move.hangar.business.domain.entity.RouteUserRel;
import snegrid.move.hangar.business.domain.vo.RouteVO;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 航线表 服务类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
public interface IRouteService extends IService<Route> {

    List<Route> pageList(RoutePageListDTO dto);

    AjaxResult importKmz(MultipartFile kmz, Long hangarId);

    Route detail(Long id);

    int delete(Long id);

    void downRouteKmz(RoutePageListDTO dto, HttpServletResponse response);

    List<RouteVO> routeListForPublic(RouteListDTO dto);

    RouteUserRel getLastSelectRoute(Long userId);

    int setLastSelectRoute(RouteUserRel routeUserRel);

}
