package snegrid.move.hangar;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import snegrid.move.hangar.config.RedisCache;
import snegrid.move.hangar.system.domain.entity.SysDictData;
import snegrid.move.hangar.utils.common.DictUtils;
import snegrid.move.hangar.utils.common.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class RedisCacheDemo {

    @Autowired
    private RedisCache redisCache;

    public final static String M300_TOKEN_KEY="m300token:";

    @Test
    public void t1() {
//        List<SysDictData> sysDictData = DictUtils.getDictCache("drone_box_param");
//        List<SysDictData> sysDictData = redisCache.getCacheList("sys_dict:" + "drone_box_param");
//        System.out.println(sysDictData.size());

//        redisCache.deleteObject(M300_TOKEN_KEY);

        redisCache.setCacheObject(M300_TOKEN_KEY + null, "123", 30, TimeUnit.MINUTES);
//
//        System.out.println("=================================");
//
        String m300TokenKey = redisCache.getCacheObject(M300_TOKEN_KEY  + null);
        if(StringUtils.isNotEmpty(m300TokenKey)) {
            System.out.println("不是空的....");
        } else {
            System.out.println("空的....");
        }

    }



}
