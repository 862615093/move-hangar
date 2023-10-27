package snegrid.move.hangar.business.service;

/**
 * 一级缓存操作
 * @author wangwei
 */
public interface L1CacheService {

    /**
     * 查询对象
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * l1缓存更新
     *
     * @param key 键
     * @param val 值
     */
    void cacheUpdate(String key, Object val);

    /**
     * 删除缓存
     *
     * @param key 键
     */
    void remove(String key);
}