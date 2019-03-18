package com.ai.aif.osp.jstorm;

import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/3/27.
 */
public class ElasticSearchClient {

    private JestClient client;

    private String esUrl = "http://10.11.20.81:9200";

    public JestClient getClient(){
        return client;
    }

    public ElasticSearchClient(String esUrl){
        this.esUrl = esUrl;
    }

    public JestClient initClient(){
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(esUrl)
                .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
                .multiThreaded(true)
                .readTimeout(10000)
                .build());
        JestClient client = factory.getObject();
        return client ;
    }

}
