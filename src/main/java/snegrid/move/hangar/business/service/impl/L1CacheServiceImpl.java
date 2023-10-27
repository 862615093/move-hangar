package snegrid.move.hangar.business.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.service.L1CacheService;

import java.util.concurrent.TimeUnit;

/**
 * 一级缓存业务层
 *
 * @author wangwei
 */
@Service
public class L1CacheServiceImpl implements L1CacheService {

    protected final Logger logger = LoggerFactory.getLogger(L1CacheServiceImpl.class);

    private final Cache<String, Object> cache = Caffeine.newBuilder()
            .maximumSize(20)
            .expireAfterAccess(60L, TimeUnit.MINUTES)
            .build();

    @Override
    public Object get(String key) {
//        logger.info("查询一级缓存key={}", key);
        return cache.getIfPresent(key);
    }

    @Override
    public void cacheUpdate(String key, Object val) {
//        logger.info("更新一级缓存key={}", key);
        cache.put(key, val);
    }

    @Override
    public void remove(String key) {
//        logger.info("删除一级缓存key={}", key);
        cache.invalidate(key);
    }
}