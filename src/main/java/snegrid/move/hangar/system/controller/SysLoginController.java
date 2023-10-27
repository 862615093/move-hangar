package snegrid.move.hangar.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.constant.MagicValue;
import snegrid.move.hangar.mqtt.factory.MqttStrategyServiceFactory;
import snegrid.move.hangar.mqtt.factory.Parameter;
import snegrid.move.hangar.security.handle.SysLoginService;
import snegrid.move.hangar.security.handle.SysPermissionService;
import snegrid.move.hangar.security.model.LoginBody;
import snegrid.move.hangar.security.util.SecurityUtils;
import snegrid.move.hangar.system.domain.entity.SysMenu;
import snegrid.move.hangar.system.domain.entity.SysUser;
import snegrid.move.hangar.system.service.ISysMenuService;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author drone
 */
@Api(tags = {"用户管理"})
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) throws Exception {
        // 登录入口
        return loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation("获取路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
