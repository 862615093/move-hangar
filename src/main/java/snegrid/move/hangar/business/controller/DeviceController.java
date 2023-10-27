package snegrid.move.hangar.business.controller;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.base.BaseController;
import snegrid.move.hangar.base.page.TableDataInfo;
import snegrid.move.hangar.business.domain.dto.DevicePageListDTO;
import snegrid.move.hangar.business.domain.entity.Device;
import snegrid.move.hangar.business.service.IDeviceService;
import snegrid.move.hangar.constant.Type;

import java.util.List;

/**
 * <p>
 * 设备表 前端控制器
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@RestController
@Api(tags = {"设备管理"})
@AllArgsConstructor
@RequestMapping("/device")
public class DeviceController extends BaseController {

    private final IDeviceService deviceService;

    @ApiOperation("列表")
    @GetMapping("/page/list")
    public TableDataInfo pageList(DevicePageListDTO dto) {
        logger.info("设备列表入参【{}】", JSON.toJSON(dto));
        startPage();
        List<Device> list = deviceService.pageList(dto);
        return getDataTable(list);
    }

    @ApiOperation("新增")
    @PostMapping("/add")
    public AjaxResult add(@Validated(Type.Add.class) @RequestBody Device dto) {
        logger.info("设备新增入参=【{}】", JSON.toJSON(dto));
        return toAjax(deviceService.add(dto));
    }

    @ApiOperation("修改")
    @PostMapping("/edit")
    public AjaxResult edit(@Validated(Type.Edit.class) @RequestBody Device dto) {
        logger.info("设备修改入参=【{}】", JSON.toJSON(dto));
        return toAjax(deviceService.edit(dto));
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable("id") Long id) {
        return AjaxResult.success(deviceService.detail(id));
    }

    @ApiOperation("物理删除")
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        return toAjax(deviceService.delete(id));
    }

    @ApiOperation("匹配无人机")
    @PostMapping("/match/{id}/{droneId}")
    public AjaxResult match(@PathVariable("id") Long id, @PathVariable("droneId") Long droneId) {
        return toAjax(deviceService.match(id, droneId));
    }
}
