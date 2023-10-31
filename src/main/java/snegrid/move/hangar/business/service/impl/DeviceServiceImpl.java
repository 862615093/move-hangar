package snegrid.move.hangar.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snegrid.move.hangar.business.domain.dto.DeviceMatchDTO;
import snegrid.move.hangar.business.domain.dto.DevicePageListDTO;
import snegrid.move.hangar.business.domain.entity.Device;
import snegrid.move.hangar.business.domain.vo.HangerDevice;
import snegrid.move.hangar.business.mapper.DeviceMapper;
import snegrid.move.hangar.business.service.IDeviceService;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.system.service.ISysDeptService;
import snegrid.move.hangar.system.service.ISysDictDataService;
import snegrid.move.hangar.system.service.ISysUserService;
import snegrid.move.hangar.utils.common.StringUtils;
import snegrid.move.hangar.utils.fly.MappingUtil;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author wangwei
 * @since 2023-10-23
 */
@Service
@AllArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final DeviceMapper deviceMapper;

    private final ISysUserService sysUserService;

    private final ISysDeptService sysDeptService;

    private final ISysDictDataService sysDictDataService;

    @Override
    public List<Device> pageList(DevicePageListDTO dto) {
        List<Device> deviceList = deviceMapper.selectList(new LambdaQueryWrapper<Device>()
                .eq(StringUtils.isNotEmpty(dto.getDeviceName()), Device::getDeviceName, dto.getDeviceName())
                .eq(Device::getType, dto.getType())
                .eq(Device::getValid, true)
                .orderByDesc(Device::getCreateTime));
        deviceList.forEach(this::setLinkedData);
        return deviceList;
    }

    private Device setLinkedData(Device device) {
        if (StringUtils.isNotNull(device.getDeviceModel())) {
            device.setDeviceModelName(sysDictDataService.selectDictDataById(device.getDeviceModel()).getDictLabel());
        }
        if (StringUtils.isNotNull(device.getDeviceManagerId())) {
            device.setDeviceManagerName(sysUserService.selectUserById(device.getDeviceManagerId()).getUserName());
        }
        if (StringUtils.isNotNull(device.getDeviceDeptId())) {
            device.setDeviceDeptName(sysDeptService.selectDeptById(device.getDeviceDeptId()).getDeptName());
        }
        return device;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Device dto) {
        if (dto.getType() == 0 || dto.getType() == 2) {
            List<Device> deviceList = deviceMapper.selectList(new LambdaQueryWrapper<Device>().eq(Device::getType, dto.getType()));
            if (deviceList.size() > 0) throw new ServiceException("机库设备只允许录入一台！！！");
        }
        List<Device> deviceList = deviceMapper.selectList(new LambdaQueryWrapper<Device>().eq(Device::getDeviceNumber, dto.getDeviceNumber()));
        if (deviceList.size() > 0) throw new ServiceException("设备号 【" + dto.getDeviceNumber() + "】重复！");
        return deviceMapper.insert(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(Device dto) {
        List<Device> deviceList = deviceMapper.selectList(new LambdaQueryWrapper<Device>().eq(Device::getDeviceNumber, dto.getDeviceNumber()));
        if (deviceList.size() > 0 && !Objects.equals(deviceList.get(0).getDeviceNumber(), dto.getDeviceNumber()))
            throw new ServiceException("设备号 【" + dto.getDeviceNumber() + "】重复！");
        return deviceMapper.updateById(dto);
    }

    @Override
    public Device detail(Long id) {
        return setLinkedData(deviceMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return deviceMapper.deleteById(id);
    }

    @Override
    public int match(DeviceMatchDTO dto) {
        List<Device> deviceList = deviceMapper.selectList(new LambdaQueryWrapper<Device>()
                .eq(Device::getType, 1)
                .eq(Device::getValid, true));
        deviceList.forEach(device -> this.lambdaUpdate().eq(Device::getId, dto.getHangarId()).set(Device::getMatchStatus, 0).update());
        this.lambdaUpdate().eq(Device::getId, dto.getHangarId()).set(Device::getMatchId, dto.getDroneId()).update();
        this.lambdaUpdate().eq(Device::getId, dto.getDroneId()).set(Device::getMatchStatus, dto.getMatchStatus()).update();
        return 1;
    }


    @Override
    public List<HangerDevice> getDeviceListForPublic(HangerDevice dto) {
        List<Device> deviceList = deviceMapper.selectList(new LambdaQueryWrapper<Device>()
                .eq(StringUtils.isNotEmpty(dto.getDeviceName()), Device::getDeviceName, dto.getDeviceName())
                .eq(StringUtils.isNotEmpty(dto.getDeviceNumber()), Device::getDeviceNumber, dto.getDeviceNumber())
                .eq(null != dto.getType(), Device::getType, dto.getType())
                .eq(Device::getValid, true)
                .orderByDesc(Device::getCreateTime));
        return MappingUtil.copyListProperties(deviceList, HangerDevice::new);
    }
}
