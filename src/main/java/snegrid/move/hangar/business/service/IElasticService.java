package snegrid.move.hangar.business.service;

import snegrid.move.hangar.business.domain.entity.EsFlyTaskLog;

public interface IElasticService<T> {

    /**
     * 判断索引是否存在
     *
     * @param indexName
     * @return
     */
    boolean exist(String indexName);

    /**
     * 创建索引
     *
     * @param indexName
     * @return
     */
    boolean index(String indexName, String mappings);

    /**
     * 创建索引
     *
     * @param indexName
     * @param dataId
     * @param log
     * @return
     */
    boolean insert(String indexName, String dataId, T log);

//    /**
//     * 更新数据
//     *
//     * @param indexName
//     * @param dataId
//     * @param strJson
//     * @return
//     */
//    boolean update(String indexName, String dataId, String strJson);
//
//    /**
//     * 获取es单个索引数据
//     *
//     * @param queryBuilder
//     * @param indexName
//     * @param clazz
//     * @return
//     */
//    T selectOne(QueryBuilder queryBuilder, String indexName, Class<T> clazz);
//
//    /**
//     * 根据条件查询es索引数据
//     *
//     * @param queryBuilder
//     * @param indexName
//     * @param clazz
//     * @return
//     */
//    List<T> selectList(QueryBuilder queryBuilder, String orderName, String orderType, String indexName, Class<T> clazz);
//
//    /**
//     * 根据条件分页查询es索引数据
//     *
//     * @param queryBuilder
//     * @param indexName
//     * @param myPageRequest
//     * @param clazz
//     * @return
//     */
//    PageResult<T> selectPageList(QueryBuilder queryBuilder, String indexName, MyPageRequest myPageRequest, Class<T> clazz);
//
//    /**
//     * 根据条件获取数据统计
//     *
//     * @param queryBuilder
//     * @param indexName
//     * @return
//     */
//    int selectCount(QueryBuilder queryBuilder, String indexName);
//
//    /**
//     * 获取分组查询结果
//     *
//     * @param queryBuilder
//     * @param termsBuilder
//     * @param indexName
//     * @return
//     */
//    SearchResponse selectGroupCount(QueryBuilder queryBuilder, TermsAggregationBuilder termsBuilder, String indexName);

}
