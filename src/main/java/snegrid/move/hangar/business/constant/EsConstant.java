package snegrid.move.hangar.business.constant;

/**
 * es 常量
 *
 * @author wangwei
 */
public class EsConstant {
    /**
     * es类型
     */
    public static final String ES_TYPE = "_doc";

    /**
     * index 分片
     */
    public static final Integer INDEX_NUMBER_OF_SHARDS = 3;

    /**
     * index 副本
     */
    public static final Integer INDEX_NUMBER_OF_REPLICAS = 1;

    /**
     * 批量同步数据默认值设置
     */
    public static final Integer SYNC_ES_BULK_DEFAULT_LIMIT = 600;
}
