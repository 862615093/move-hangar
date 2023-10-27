package snegrid.move.hangar.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snegrid.move.hangar.business.domain.entity.File;
import snegrid.move.hangar.business.mapper.FileMapper;
import snegrid.move.hangar.business.service.IFileService;
import snegrid.move.hangar.constant.MagicValue;
import snegrid.move.hangar.minio.config.MinioConfig;

import java.time.LocalDateTime;
import java.util.List;

import static snegrid.move.hangar.constant.MagicValue.LEFT_SLASH;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Service
@AllArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private final FileMapper fileMapper;

    private final MinioConfig minioConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveAttachmentFile(List<File> list, Long relId, String relType) {
        if (ObjUtil.isNull(list) || CollectionUtil.isEmpty(list)) return true;
        return list.stream().noneMatch(file -> {
            File build = File.builder()
                    .relId(relId)
                    .relType(relType)
                    .fileName(file.getFileName())
                    .filePath(file.getFilePath())
                    .fileUrl(minioConfig.getUrl() + LEFT_SLASH + file.getFilePath())
                    .fileType(file.getFileType())
                    .uploadUserId(file.getUploadUserId())
                    .uploadTime(LocalDateTime.now())
                    .build();
            return !this.save(build);
        });
    }

    @Override
    public List<File> getAttachmentFileList(Long relId, String relType) {
        return fileMapper.selectList(new LambdaQueryWrapper<File>().eq(File::getRelId, relId).eq(File::getRelType, relType));
    }

    @Override
    public List<File> getAttachmentFileList(String relUuid) {
        return fileMapper.selectList(new LambdaQueryWrapper<File>().eq(File::getRelUuid, relUuid));
    }
}
