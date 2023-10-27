package snegrid.move.hangar.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snegrid.move.hangar.config.HangarConfig;
import snegrid.move.hangar.utils.common.StringUtils;

/**
 * 首页
 *
 * @author drone
 */
@RestController
public class SysIndexController {
    /**
     * 系统基础配置
     */
    @Autowired
    private HangarConfig hangarConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", hangarConfig.getName(), hangarConfig.getVersion());
    }
}
