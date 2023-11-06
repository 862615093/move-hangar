package snegrid.move.hangar.business.service.impl;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import snegrid.move.hangar.business.service.IElasticService;
import snegrid.move.hangar.exception.ServiceException;

import java.io.IOException;

import static snegrid.move.hangar.business.constant.EsConstant.*;

/**
 * es 业务层
 *
 * @author wangwei
 */
@Service
@AllArgsConstructor
public class ElasticServiceImpl<T> implements IElasticService<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestHighLevelClient restHighLevelClient;

    @Override
    public boolean exist(String indexName) {
        try {
            GetRequest getRequest = new GetRequest(indexName);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            getRequest.storedFields("_none_");
            return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("ES索引判断是否存在失败，{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean index(String indexName, String mappings) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", INDEX_NUMBER_OF_SHARDS)
                    .put("index.number_of_replicas", INDEX_NUMBER_OF_REPLICAS));
            if (StringUtils.isBlank(mappings)) throw new ServiceException("索引结构不能为空!");
            createIndexRequest.mapping(mappings, XContentType.JSON);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                throw new ServiceException("索引创建失败!");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ES创建索引失败，{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean insert(String indexName, String dataId, T t) {
        try {
            IndexRequest indexRequest = new IndexRequest(indexName, ES_TYPE, dataId);
            indexRequest.source(JSON.toJSONString(t), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                logger.info("ES索引插入数据成功");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ES插入数据失败，{}", e.getMessage());
            return false;
        }
    }
//
//    @Override
//    public boolean update(String indexName, String dataId, String strJson) {
//        try {
//            UpdateRequest updateRequest = new UpdateRequest(indexName, TYPE_NAME, dataId);
//            updateRequest.timeout(TimeValue.timeValueSeconds(1));
//            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//            updateRequest.retryOnConflict(3);
//            updateRequest.fetchSource(true);
//            updateRequest.doc(strJson, XContentType.JSON);
//            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED ||
//                    updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
//                log.info("ES索引更新数据成功");
//                return true;
//            }
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("ES索引更新数据失败，{}", e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public T selectOne(QueryBuilder queryBuilder, String indexName, Class<T> clazz) {
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(TYPE_NAME);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(queryBuilder);
//        searchRequest.source(searchSourceBuilder);
//        try {
//            log.info("从ES查询单个数据的语句：查询索引：{}，查询条件：{}", indexName, searchSourceBuilder.toString());
//            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHits hits = searchResponse.getHits();
//            long total = hits.getTotalHits();
//            SearchHit[] searchHits = hits.getHits();
//            List<T> list = new ArrayList<>();
//            for (SearchHit searchHit : searchHits) {
//                //String id = searchHit.getId();
//                Map<String, Object> map = searchHit.getSourceAsMap();
//                // list.add(JSONUtil.mapToObject(map, clazz));
//                list.add(BeanUtil.toBean(map, clazz));
//            }
//            return list.get(0);
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("ES查询信息数据失败，{}", e.getMessage());
//            return null;
//        }
//    }
//
//    @Override
//    public List<T> selectList(QueryBuilder queryBuilder, String orderName, String orderType, String indexName, Class<T> clazz) {
//        int count = this.selectCount(queryBuilder, indexName);
//        if (count == 0) {
//            return Collections.emptyList();
//        }
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(TYPE_NAME);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(queryBuilder);
//        if (StringUtils.isNotEmpty(orderName)) {
//            if (Sort.Direction.ASC.name().equals(orderType)) {
//                searchSourceBuilder.sort(orderName + ".keyword", SortOrder.ASC);
//            } else {
//                searchSourceBuilder.sort(orderName + ".keyword", SortOrder.DESC);
//            }
//        }
//        searchSourceBuilder.from(0).size(count);
//        searchRequest.source(searchSourceBuilder);
//        try {
//            log.info("从ES查询全部数据的语句：查询索引：{}，查询条件：{}", indexName, searchSourceBuilder.toString());
//            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHits hits = searchResponse.getHits();
//            long total = hits.getTotalHits();
//            SearchHit[] searchHits = hits.getHits();
//            List<T> list = new ArrayList<>();
//            for (SearchHit searchHit : searchHits) {
//                //String id = searchHit.getId();
//                Map<String, Object> map = searchHit.getSourceAsMap();
//                // list.add(JSONUtil.mapToObject(map, clazz));
//                list.add(BeanUtil.toBean(map, clazz));
//            }
//            return list;
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("ES查询信息数据失败，{}", e.getMessage());
//            return null;
//        }
//    }
//
//    @Override
//    public PageResult<T> selectPageList(QueryBuilder queryBuilder, String indexName, MyPageRequest myPageRequest, Class<T> clazz) {
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.types(TYPE_NAME);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(queryBuilder);
//        if (myPageRequest.getOrder() != null && StringUtils.isNotEmpty(myPageRequest.getOrder().getField())) {
//            if (Sort.Direction.ASC.name().equals(myPageRequest.getOrder().getDirection().name())) {
//                searchSourceBuilder.sort(CommonUtil.toLowerCase(myPageRequest.getOrder().getField()) + ".keyword", SortOrder.ASC);
//            } else {
//                searchSourceBuilder.sort(CommonUtil.toLowerCase(myPageRequest.getOrder().getField()) + ".keyword", SortOrder.DESC);
//            }
//        }
//        int pageNum = 0;
//        int size = BaseConstant.MAX_PAGE_SIZE;
//        if (myPageRequest.getPageNum() != null) {
//            pageNum = myPageRequest.getPageNum() - 1;
//            size =  myPageRequest.getPageSize();
//        }
//        searchSourceBuilder.from(pageNum * size).size(size);
//        searchRequest.source(searchSourceBuilder);
//        try {
//            log.info("分页从ES查询数据的语句：查询索引：{}，查询条件：{}", indexName, searchSourceBuilder.toString());
//            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            SearchHits hits = searchResponse.getHits();
//            long total = hits.getTotalHits();
//            SearchHit[] searchHits = hits.getHits();
//            List<T> list = new ArrayList<>();
//            for (SearchHit searchHit : searchHits) {
//                //String id = searchHit.getId();
//                Map<String, Object> map = searchHit.getSourceAsMap();
//                // list.add(JSONUtil.mapToObject(map, clazz));
//                list.add(BeanUtil.toBean(map, clazz));
//            }
//            return new PageResult<>(list, pageNum, size, total);
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("ES查询信息数据失败，{}", e.getMessage());
//            return null;
//        }
//    }
//
//    @Override
//    public int selectCount(QueryBuilder queryBuilder, String indexName) {
//        CountRequest countRequest = new CountRequest(indexName);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(queryBuilder);
//        countRequest.source(searchSourceBuilder);
//        try {
//            log.info("从ES查询数据统计的语句：查询索引：{}，查询条件：{}", indexName, searchSourceBuilder.toString());
//            long count = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT).getCount();
//            return (int) count;
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("ES查询信息数据失败，{}", e.getMessage());
//            return 0;
//        }
//    }
//
//    @Override
//    public SearchResponse selectGroupCount(QueryBuilder queryBuilder, TermsAggregationBuilder termsBuilder, String indexName) {
//        try {
//            SearchRequest searchRequest = new SearchRequest(indexName);
//            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//            if (Optional.ofNullable(termsBuilder).isPresent()) {
//                searchSourceBuilder.aggregation(termsBuilder);
//            }
//            if (Optional.ofNullable(queryBuilder).isPresent()) {
//                searchSourceBuilder.query(queryBuilder);
//            }
//            searchRequest.source(searchSourceBuilder);
//            log.info("从ES分组查询数据的语句：查询索引：{}，查询条件：{}", indexName, searchSourceBuilder.toString());
//            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
