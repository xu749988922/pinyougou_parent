package com.pinyougou.es.dao;

import entity.EsItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemDao  extends ElasticsearchRepository<EsItem,Long> {
}
