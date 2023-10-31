package snegrid.move.hangar.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import snegrid.move.hangar.business.domain.dto.DeviceMatchDTO;
import snegrid.move.hangar.business.domain.dto.DevicePageListDTO;
import snegrid.move.hangar.business.domain.entity.Device;
import snegrid.move.hangar.business.domain.vo.HangerDevice;

import java.util.List;

/**
 * <p>
 * 设备表 服务类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
public interface IDeviceService extends IService<Device> {

    List<Device> pageList(DevicePageListDTO dto);

    int add(Device dto);

    int edit(Device dto);

    Device detail(Long id);

    int delete(Long id);

    int match(DeviceMatchDTO dto);

    List<HangerDevice> getDeviceListForPublic(HangerDevice dto);
}
