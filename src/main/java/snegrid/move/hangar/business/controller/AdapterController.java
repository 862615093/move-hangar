package snegrid.move.hangar.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.base.BaseController;
import snegrid.move.hangar.business.domain.dto.FlyTaskDTO;
import snegrid.move.hangar.business.domain.dto.FlyTaskPicListDTO;
import snegrid.move.hangar.business.domain.dto.RouteListDTO;
import snegrid.move.hangar.business.domain.vo.HangerDevice;
import snegrid.move.hangar.business.service.IDeviceService;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.business.service.IRouteService;
import snegrid.move.hangar.security.handle.SysLoginService;
import snegrid.move.hangar.security.model.LoginBody;
import snegrid.move.hangar.utils.common.StringUtils;

/**
 * <p>
 * 适配层 控制器
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@RestController
@Api(tags = {"适配层管理"}, hidden = true)
@AllArgsConstructor
@RequestMapping("/adapter")
public class AdapterController extends BaseController {

    private final SysLoginService loginService;

    private final IDeviceService deviceService;

    private final IFlyTaskService flyTaskService;

    private final IRouteService routeService;

    @ApiOperation("登录开放接口")
    @PostMapping("/api/user/login")
    public AjaxResult login(@RequestBody String sendParam) throws Exception {
        if (StringUtils.isEmpty(sendParam)) return AjaxResult.error("请传递参数");
        JSONObject jsonObject = JSONObject.parseObject(sendParam);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername(username);
        loginBody.setPassword(password);
        // 生成令牌
        return loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
    }

    @ApiOperation("设备查询开放接口")
    @GetMapping("/device/hangerDevice/deviceList")
    public AjaxResult deviceListForPublic(HangerDevice dto) {
        logger.info("设备查询开放接口入参=【{}】", JSON.toJSON(dto));
        return AjaxResult.success(deviceService.getDeviceListForPublic(dto));
    }

    @ApiOperation("开始飞行任务开放接口")
    @PostMapping("/task/hangerTask/add")
    public AjaxResult startTaskForPublic(@Validated @RequestBody FlyTaskDTO dto) {
        logger.info("开始飞行任务开放接口入参=【{}】", JSON.toJSON(dto));
        return flyTaskService.startTaskForPublic(dto);
    }

    @ApiOperation("航线列表开放接口")
    @GetMapping("/plan/route/getRoutes")
    public AjaxResult routeListForPublic(@Validated RouteListDTO dto) {
        logger.info("航线列表开放接口入参【{}】", JSON.toJSON(dto));
        return AjaxResult.success(routeService.routeListForPublic(dto));
    }

    @ApiOperation("导入kmz航线开放接口")
    @PostMapping("/plan/route/importRouteKmz")
    public AjaxResult importRouteKmz(@RequestPart("file") MultipartFile kmz, Long hangarId) {
        logger.info("航线列表开放接口入参 hangarId =【{}】", JSON.toJSON(hangarId));
        return routeService.importKmz(kmz, hangarId);
    }

    @ApiOperation("获取指定飞行任务航拍图开放接口")
    @GetMapping("/fly/flyHistory/getFlyHistoryPicInfoByHistoryId")
    public AjaxResult flyTaskPicListForPublic(@Validated FlyTaskPicListDTO dto) {
        logger.info("获取指定飞行任务航拍图开放接口入参【{}】", JSON.toJSON(dto));
        return AjaxResult.success(flyTaskService.flyTaskPicListForPublic(dto));
    }
}
