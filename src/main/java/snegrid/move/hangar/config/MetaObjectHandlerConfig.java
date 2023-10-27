package snegrid.move.hangar.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import snegrid.move.hangar.security.util.SecurityUtils;

import java.time.LocalDateTime;

/**
 * 自定义字段填充
 *
 * @author wangwei
 */
@Configuration
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createBy", SecurityUtils.getUserId(), metaObject);
        this.setFieldValByName("deptId", SecurityUtils.getLoginUser().getDeptId(), metaObject);
        this.setFieldValByName("valid", true, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateBy", SecurityUtils.getUserId(), metaObject);
    }
}