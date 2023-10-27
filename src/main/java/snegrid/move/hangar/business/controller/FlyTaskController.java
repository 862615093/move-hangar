package snegrid.move.hangar.business.controller;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.base.BaseController;
import snegrid.move.hangar.business.domain.entity.Device;
import snegrid.move.hangar.business.domain.entity.FlyTask;
import snegrid.move.hangar.business.service.IFileService;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.constant.Type;

import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 飞行任务表 前端控制器
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@RestController
@Api(tags = {"飞行管理"})
@AllArgsConstructor
@RequestMapping("/flyTask")
public class FlyTaskController extends BaseController {

    private final IFlyTaskService flyTaskService;

    @ApiOperation("获取任务指令")
    @PostMapping("/getTaskOrder")
    public AjaxResult getTaskOrder() {
        return AjaxResult.success(flyTaskService.getTaskOrder());
    }

    @ApiOperation("新增")
    @PostMapping("/add")
    public AjaxResult add(@Validated(Type.Add.class) @RequestBody FlyTask dto) {
        logger.info("飞行任务新增入参=【{}】", JSON.toJSON(dto));
        return toAjax(flyTaskService.save(dto));
    }
}
