package snegrid.move.hangar.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import snegrid.move.hangar.business.domain.entity.File;

import java.util.List;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
public interface IFileService extends IService<File> {

    /**
     * 保存关联附件
     *
     * @return
     */
    public boolean batchSaveAttachmentFile(List<File> list, Long relId, String relType);

    /**
     * 获取关联附件
     *
     * @param relId
     * @param relType
     * @return
     */
    public List<File> getAttachmentFileList(Long relId, String relType);

    /**
     * 获取关联附件
     *
     * @param relUuid
     * @return
     */
    public List<File> getAttachmentFileList(String relUuid);
}
