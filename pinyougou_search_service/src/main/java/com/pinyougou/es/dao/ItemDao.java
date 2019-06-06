package com.pinyougou.es.dao;

import entity.EsItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemDao extends ElasticsearchRepository<EsItem,Long> {
    //ElasticsearchRepository删除语法：deleteBy+域名+匹配方式
    void deleteByGoodsIdIn(Long[] goodsIds);
}
