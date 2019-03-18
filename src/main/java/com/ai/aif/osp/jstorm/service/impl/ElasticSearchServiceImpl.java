package com.ai.aif.osp.jstorm.service.impl;

import com.ai.aif.osp.jstorm.ElasticSearchClient;
import com.ai.aif.osp.jstorm.bean.RowBean;
import com.ai.aif.osp.jstorm.service.ElasticSearchService;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/3/27.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    @Autowired
    private ElasticSearchClient elasticSearchClient;

    @Override
    public void receiveAndSave(String topic, List<RowBean> rowBeans) {
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(topic)
                .defaultType("message")
                .addAction(Arrays.asList(new Index.Builder(rowBeans).build())).build();
        try {
            elasticSearchClient.getClient().execute(bulk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveAndSave(String msgType, String topic, List<RowBean> rowBeans) {
        System.out.println("=====================");
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(topic)
                .defaultType(msgType)
                .addAction(Arrays.asList(new Index.Builder(rowBeans).build())).build();
        try {
            elasticSearchClient.getClient().execute(bulk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
