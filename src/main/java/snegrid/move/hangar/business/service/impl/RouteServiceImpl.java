package snegrid.move.hangar.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.business.domain.dto.RouteListDTO;
import snegrid.move.hangar.business.domain.dto.RoutePageListDTO;
import snegrid.move.hangar.business.domain.entity.File;
import snegrid.move.hangar.business.domain.entity.Route;
import snegrid.move.hangar.business.domain.vo.RouteVO;
import snegrid.move.hangar.business.mapper.RouteMapper;
import snegrid.move.hangar.business.service.IFileService;
import snegrid.move.hangar.business.service.IRouteService;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.minio.config.MinioConfig;
import snegrid.move.hangar.minio.utils.MinIoUtils;
import snegrid.move.hangar.security.util.SecurityUtils;
import snegrid.move.hangar.utils.common.DateUtils;
import snegrid.move.hangar.utils.common.StringUtils;
import snegrid.move.hangar.utils.fly.MappingUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

import static snegrid.move.hangar.business.constant.AttachmentConstant.ROUTE_TYPE;
import static snegrid.move.hangar.constant.MagicValue.DECIMAL_POINT;
import static snegrid.move.hangar.constant.MagicValue.LEFT_SLASH;
import static snegrid.move.hangar.minio.constant.MinioConstant.BUCKET_NAME;
import static snegrid.move.hangar.minio.constant.MinioConstant.ROUTE_PATH;

/**
 * <p>
 * 航线表 服务实现类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Service
@AllArgsConstructor
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route> implements IRouteService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RouteMapper routeMapper;

    private final MinIoUtils minIoUtil;

    private final IFileService fileService;

    private final MinioConfig minioConfig;

    @Override
    public List<Route> pageList(RoutePageListDTO dto) {
        return routeMapper.selectList(new LambdaQueryWrapper<Route>()
                .like(StringUtils.isNotNull(dto.getRouteName()), Route::getRouteName, dto.getRouteName())
                .eq(Route::getValid, true)
                .orderByDesc(Route::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importKmz(MultipartFile kmz, Long hangarId) {
        //1.TODO 校验航线

        //2.上传航线 & 保存记录
        String filePath = ROUTE_PATH + DateUtils.dateTime() + LEFT_SLASH + kmz.getOriginalFilename();
        try {
            minIoUtil.uploadWithPath(BUCKET_NAME, filePath, kmz);
        } catch (Exception e) {
            logger.error("kmz上传minio异常！", e);
            return AjaxResult.error(e.getMessage());
        }
        Route route = Route.builder().routeName(kmz.getOriginalFilename()).hangarId(hangarId).build();
        if (routeMapper.insert(route) > 0
                && fileService.save(File.builder()
                .relId(route.getId())
                .relType(ROUTE_TYPE)
                .fileName(kmz.getOriginalFilename().substring(0, kmz.getOriginalFilename().lastIndexOf(DECIMAL_POINT)))
                .filePath(BUCKET_NAME + filePath)
                .fileUrl(minioConfig.getUrl() + LEFT_SLASH + filePath)
                .relId(route.getId())
                .fileType(kmz.getOriginalFilename().substring(kmz.getOriginalFilename().lastIndexOf(DECIMAL_POINT) + 1))
                .uploadTime(LocalDateTime.now())
                .uploadUserId(SecurityUtils.getUserId())
                .build())) {
            return AjaxResult.success(route.getId());
        } else {
            return AjaxResult.error("航线记录保存失败！");
        }
    }

    @Override
    public void downRouteKmz(RoutePageListDTO dto, HttpServletResponse response) {
        List<File> fileList = fileService.getAttachmentFileList(dto.getId(), ROUTE_TYPE);
        if (CollectionUtil.isEmpty(fileList)) throw new ServiceException("航线不存在！");
        try {
            File routeFile = fileList.get(0);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(routeFile.getFileName() + "." + routeFile.getFileType(), "utf-8") + "\"");
            response.setContentType("application/octet-stream");
            InputStream input = minIoUtil.download(BUCKET_NAME, routeFile.getFilePath());
            IOUtils.copy(input, response.getOutputStream());
            response.flushBuffer();
            input.close();
        } catch (Exception e) {
            logger.error("航线下载失败！", e);
        }
    }

    @Override
    public Route detail(Long id) {
        return routeMapper.selectById(id);
    }

    @Override
    public int delete(Long id) {
        return this.lambdaUpdate().eq(Route::getId, id).set(Route::getValid, false).update() ? 1 : 0;
    }

    @Override
    public List<RouteVO> routeListForPublic(RouteListDTO dto) {
        List<Route> routeList = routeMapper.selectList(new LambdaQueryWrapper<Route>()
                .like(StringUtils.isNotNull(dto.getRouteName()), Route::getRouteName, dto.getRouteName())
                .eq(StringUtils.isNotNull(dto.getHangarId()), Route::getHangarId, dto.getHangarId())
                .eq(Route::getValid, true)
                .orderByDesc(Route::getCreateTime));
        List<RouteVO> routeVOList = MappingUtil.copyListProperties(routeList, RouteVO::new);
        routeVOList.forEach(routeVO -> routeVO.setRouteFileUrl(fileService.getAttachmentFileList(routeVO.getId(), ROUTE_TYPE).get(0).getFileUrl()));
        return routeVOList;
    }
}
