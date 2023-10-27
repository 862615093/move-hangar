package snegrid.move.hangar.netty.handler;

import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

/**
 * handler 注解
 *
 * @author wangwei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Handle {

    @NotBlank public int value();
}
