package snegrid.move.hangar.aspect;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.annotation.ProcessLogToEs;
import snegrid.move.hangar.business.domain.entity.EsFlyTaskLog;
import snegrid.move.hangar.business.service.IElasticService;
import snegrid.move.hangar.business.service.IFlyTaskService;
import snegrid.move.hangar.netty.message.CommonMessage;
import snegrid.move.hangar.utils.common.DateUtils;

import javax.annotation.Resource;

import static snegrid.move.hangar.constant.MagicValue.MSG;

/**
 * 日志流程业务层
 *
 * @author wangwei
 */
@Slf4j
@Aspect
@Component
public class ProcessLogToEsAspectService implements ProcessLogToEsAspect {

    private static final Logger logger = LoggerFactory.getLogger(ProcessLogToEsAspectService.class);

    @Resource
    private IElasticService<EsFlyTaskLog> elasticService;

    @Resource
    private IFlyTaskService flyTaskService;

    @Override
    public void processLogToEs(JoinPoint joinPoint, ProcessLogToEs processLogToEs, Object methodResult) {
        CommonMessage afterResult = (CommonMessage) methodResult;
        boolean flag = elasticService.insert("demo", IdUtil.fastUUID(),
                EsFlyTaskLog.builder()
//                            .flyTaskId(flyTaskService.getLastFlyTask().getId())
                        .flyTaskId("123654789")
                        .logSource(4)
                        .logCode(0)
                        .logLevel(0)
                        .logContent("[平台]" + afterResult.get(MSG))
                        .logCreateTime(DateUtils.getTime())
                        .build());
        if (!flag) logger.error("processLogToEs()方法流程日志保存失败！");
    }
}