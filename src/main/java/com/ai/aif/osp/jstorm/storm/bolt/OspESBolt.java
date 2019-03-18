package com.ai.aif.osp.jstorm.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.ai.aif.osp.jstorm.bean.OspRowBean;
import com.ai.aif.osp.jstorm.bean.RowBean;
import com.ai.aif.osp.jstorm.common.Constant;
import com.ai.aif.osp.jstorm.common.row.OspRowHandlerFactory;
import com.ai.aif.osp.jstorm.common.row.RowHandlerFactory;
import com.ai.aif.osp.jstorm.service.ElasticSearchService;
import com.ai.aif.osp.jstorm.service.OspStatisticsService;
import com.ai.aif.osp.jstorm.util.OspContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/2.
 */
public class OspESBolt extends BaseRichBolt {
    private static final Logger LOG = LoggerFactory.getLogger(OspESBolt.class);

    private OutputCollector collector;

    private RowHandlerFactory rowHandlerFactory;


    private OspStatisticsService ospStatisticsService;
    private ElasticSearchService elasticSearchService;


    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        ospStatisticsService = (OspStatisticsService) OspContext.getBean(OspStatisticsService.class);
        elasticSearchService =(ElasticSearchService) OspContext.getBean(ElasticSearchService.class);
        rowHandlerFactory = new OspRowHandlerFactory();
        this.collector = collector;

    }

    public void execute(Tuple input) {
        try{
            List<RowBean> rowBeans = OspRowBean.createByJsonArray(input.getString(0), rowHandlerFactory);

            elasticSearchService.receiveAndSave("ospTrace", Constant.INDEX_TYPE_TRACE,rowBeans);
            ospStatisticsService.counter(rowBeans.stream()
                    .map(rowBean->(OspRowBean)rowBean)
                    .collect(Collectors.toList()));
            collector.ack(input);

        }catch (Exception e){
            LOG.error("OspES Error with input {}", input.getString(0), e);
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("type","message"));
    }
}
