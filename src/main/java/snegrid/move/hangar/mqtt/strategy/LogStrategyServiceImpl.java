package snegrid.move.hangar.mqtt.strategy;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.domain.entity.EsFlyTaskLog;
import snegrid.move.hangar.business.service.IElasticService;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.business.service.L1CacheService;
import snegrid.move.hangar.mqtt.factory.Parameter;
import snegrid.move.hangar.mqtt.message.CommonReceiveCmd;
import snegrid.move.hangar.mqtt.message.LogParams;
import snegrid.move.hangar.netty.handler.CommonNettyHandler;
import snegrid.move.hangar.system.domain.entity.SysDictData;
import snegrid.move.hangar.system.service.ISysDictTypeService;
import snegrid.move.hangar.utils.common.DateUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志业务处理
 *
 * @author wangwei
 */
@Service
@AllArgsConstructor
public class LogStrategyServiceImpl implements IMqttStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(LogStrategyServiceImpl.class);

    private final ISysDictTypeService sysDictTypeService;

    private final CommonNettyHandler nettyHandler;

    private final L1CacheService l1CacheService;

    private final IFlyTaskService flyTaskService;

    private final IElasticService<EsFlyTaskLog> elasticService;

    @Override
    public void processBiz(Parameter dto) {
        String msg;
        Integer msgType = null;
        List<SysDictData> sysDictData = sysDictTypeService.selectDictDataByType("device_cmd_process");
        Map<String, String> collect = sysDictData.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
        //1.判断日志类型
        if (dto.getTopicSuffix().startsWith("SNE")) {
            //机库日志
            CommonReceiveCmd<Integer> commonReceiveCmd = JSON.parseObject(dto.getMessage(), new CommonReceiveCmd<Integer>() {}.getClass());
            //保存日志
            boolean flag = elasticService.insert("demo", IdUtil.fastUUID(),
                    EsFlyTaskLog.builder()
//                            .flyTaskId(flyTaskService.getLastFlyTask().getId())
                            .flyTaskId("frwvqvqervqerwgv")
                            .logSource(1)
                            .logCode(commonReceiveCmd.getParams())
                            .logLevel(0)
                            .logContent(collect.get(commonReceiveCmd.getParams().toString()))
                            .logCreateTime(DateUtils.getTime())
                            .build());
            if (!flag) logger.error("机库日志保存失败！");
            msg = JSONObject.toJSONString(commonReceiveCmd);
            msgType = commonReceiveCmd.getMsgType();
        } else {
            CommonReceiveCmd<LogParams> commonReceiveCmd = JSON.parseObject(dto.getMessage(), new CommonReceiveCmd<LogParams>() {}.getClass());
            if (commonReceiveCmd.getMsgType() == 8) {
                //保存无人机日志
                boolean flag = elasticService.insert("demo", IdUtil.fastUUID(),
                        EsFlyTaskLog.builder()
//                            .flyTaskId(flyTaskService.getLastFlyTask().getId())
                                .flyTaskId("abdgdg")
                                .logSource(2)
                                .logCode(commonReceiveCmd.getParams().getCode())
                                .logLevel(commonReceiveCmd.getParams().getLevel())
                                .logContent(collect.get(commonReceiveCmd.getParams().getCode().toString()))
                                .logCreateTime(commonReceiveCmd.getParams().getCreateTime())
                                .build());
                if (!flag) logger.error("无人机日志保存失败！");
            }
            if (commonReceiveCmd.getMsgType() == 10) {
                //大疆日志
                boolean flag = elasticService.insert("demo", IdUtil.fastUUID(),
                        EsFlyTaskLog.builder()
//                            .flyTaskId(flyTaskService.getLastFlyTask().getId())
                                .flyTaskId("iluiluimk")
                                .logSource(3)
                                .logCode(commonReceiveCmd.getParams().getCode())
                                .logLevel(commonReceiveCmd.getParams().getLevel())
                                .logContent(collect.get(commonReceiveCmd.getParams().getCode().toString()))
                                .logCreateTime(commonReceiveCmd.getParams().getCreateTime())
                                .build());
                if (!flag) logger.error("大疆日志保存失败！");
            }
            msg = JSONObject.toJSONString(commonReceiveCmd);
            msgType = commonReceiveCmd.getMsgType();
        }
        //3.推送日志
        nettyHandler.sendMessageToAll(msgType.toString(), msg);
    }

    @Override
    public String getType() {
        return "/sys/log/";
    }
}
