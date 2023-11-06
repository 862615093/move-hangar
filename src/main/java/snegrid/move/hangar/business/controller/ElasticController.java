package snegrid.move.hangar.business.controller;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snegrid.move.hangar.base.AjaxResult;
import snegrid.move.hangar.base.BaseController;
import snegrid.move.hangar.business.domain.entity.EsFlyTaskLog;
import snegrid.move.hangar.business.service.IElasticService;

import java.util.Map;

/**
 * ES操作管理 控制层
 *
 * @author wangwei
 */
@RestController
@AllArgsConstructor
@Api(tags = {"ES操作管理"})
@RequestMapping("/elastic")
public class ElasticController extends BaseController {

    private final IElasticService<Object> elasticService;

    @PostMapping("/index")
    @ApiOperation(value = "创建索引", httpMethod = "POST")
    public AjaxResult index(@RequestBody Map<String, Object> map) {
        logger.info("插入数据接口请求参数，{}", JSON.toJSONString(map));
        String indexName = String.valueOf(map.get("indexName"));
        Object mappings = map.get("mappings");
        return toAjax(elasticService.index(indexName, JSON.toJSONString(mappings)));
    }

    @PostMapping("/insert")
    @ApiOperation(value = "插入数据", httpMethod = "POST")
    public AjaxResult insert(@RequestBody Map<String, Object> map) {
        logger.info("插入数据接口请求参数，{}", JSON.toJSONString(map));
        String indexName = String.valueOf(map.get("indexName"));
        String dataId = String.valueOf(map.get("dataId"));
        EsFlyTaskLog log = JSON.to(EsFlyTaskLog.class, map.get("strJson"));
        return toAjax(elasticService.insert(indexName, dataId, log));
    }
}
