package snegrid.move.hangar.annotation;

import java.lang.annotation.*;

/**
 * es 流程日志注解
 *
 * @author wangwei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessLogToEs {


}