package snegrid.move.hangar.utils.fly;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 映射工具类
 *
 * @author wangwei
 */
@Component
public class MappingUtil {

    /**
     * 获取映射字段值
     *
     * @param id
     * @param function
     * @param <R>
     * @return
     */
    public static <R> R getMappingVal(Long id, Function<Long, R> function) {
        return function.apply(id);
    }

    /**
     * @param sources: 数据源类
     * @param target:  目标类
     * @return 赋值后的list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            BeanUtil.copyProperties(source, t);
            list.add(t);
        }
        return list;
    }
}
