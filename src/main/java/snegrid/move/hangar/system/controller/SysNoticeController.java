package snegrid.move.hangar.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.base.BaseController;
import snegrid.move.hangar.base.page.TableDataInfo;
import snegrid.move.hangar.system.domain.entity.SysNotice;
import snegrid.move.hangar.system.service.ISysNoticeService;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author drone
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return AjaxResult.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
//    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
//    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
//    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
