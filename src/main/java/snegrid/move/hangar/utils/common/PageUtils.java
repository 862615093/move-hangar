package snegrid.move.hangar.utils.common;

import com.github.pagehelper.PageHelper;
import snegrid.move.hangar.base.page.PageDomain;
import snegrid.move.hangar.base.page.TableSupport;

/**
 * 分页工具类
 *
 * @author drone
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
}
