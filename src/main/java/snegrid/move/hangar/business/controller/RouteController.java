package snegrid.move.hangar.business.controller;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.base.BaseController;
import snegrid.move.hangar.base.page.TableDataInfo;
import snegrid.move.hangar.business.domain.dto.RoutePageListDTO;
import snegrid.move.hangar.business.domain.entity.Route;
import snegrid.move.hangar.business.service.IRouteService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 航线表 前端控制器
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@RestController
@Api(tags = {"航线管理"})
@AllArgsConstructor
@RequestMapping("/route")
public class RouteController extends BaseController {

    private final IRouteService routeService;

    @ApiOperation("列表")
    @GetMapping("/page/list")
    public TableDataInfo pageList(RoutePageListDTO dto) {
        logger.info("航线列表入参【{}】", JSON.toJSON(dto));
        startPage();
        List<Route> list = routeService.pageList(dto);
        return getDataTable(list);
    }

    @ApiOperation("导入kmz航线")
    @PostMapping("/importRouteKmz")
    public AjaxResult importRouteKmz(@RequestPart("file") MultipartFile kmz, Long hangarId) {
        return routeService.importKmz(kmz, hangarId);
    }

    @ApiOperation("下载kmz航线")
    @PostMapping("/downRouteKmz")
    public void downRouteKmz(@Validated @RequestBody RoutePageListDTO dto, HttpServletResponse response) {
        routeService.downRouteKmz(dto, response);
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable("id") Long id) {
        return AjaxResult.success(routeService.detail(id));
    }

    @ApiOperation("逻辑删除")
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        return toAjax(routeService.delete(id));
    }
}
