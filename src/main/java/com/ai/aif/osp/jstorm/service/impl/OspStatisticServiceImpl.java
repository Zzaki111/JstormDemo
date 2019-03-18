package com.ai.aif.osp.jstorm.service.impl;

import com.ai.aif.osp.jstorm.bean.OspAggRowBean;
import com.ai.aif.osp.jstorm.bean.OspRowBean;
import com.ai.aif.osp.jstorm.bean.RowBean;
import com.ai.aif.osp.jstorm.cache.OspTableCache;
import com.ai.aif.osp.jstorm.cache.RawData;
import com.ai.aif.osp.jstorm.common.Constant;
import com.ai.aif.osp.jstorm.common.OspColumnKey;
import com.ai.aif.osp.jstorm.service.ElasticSearchService;
import com.ai.aif.osp.jstorm.service.OspStatisticsService;
import com.ai.aif.osp.jstorm.util.Tools;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/8.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OspStatisticServiceImpl implements OspStatisticsService{

    private static final Logger LOG = LoggerFactory.getLogger(OspStatisticServiceImpl.class);

    /**
     * osp统计的本地缓存
     */
    private OspTableCache ospTableCache =new OspTableCache(this,60000);

    @Resource
    private ElasticSearchService elasticSearchService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void counter(List<OspRowBean> rowBeans) {
        LOG.info("将osp数据聚合统计写入内存缓存");
        rowBeans.forEach(ospRowBean->{
            String expCode = "";
            if(ospRowBean.field(Constant.FIELD_EXPCODE)!=null){
                expCode += ospRowBean.field(Constant.FIELD_EXPCODE);
            }
            ospTableCache.updateRow(
                    RawData.values2RowKey(String.valueOf(ospRowBean.channelId()),
                            String.valueOf(ospRowBean.appId()),ospRowBean.appName(),
                            ospRowBean.abilityCode(), ospRowBean.abilityName(),
                            ospRowBean.channelName(), String.valueOf(ospRowBean.uId()),
                            expCode,String.valueOf(ospRowBean.invokedTime()/5000*5000),
                            String.valueOf(ospRowBean.abilityId())),
                    ospRowBean.elapsedTime(),ospRowBean.success()
            );
        });
    }


    @Override
    public void attachOspStatistics(String rowKey,Map<String, Object> columnMap) {

        LOG.info("从内存缓存中读取数据插入es");
        if (columnMap == null || columnMap.size() == 0)
            return;

        OspAggRowBean rowBean = createRowBeanByTableCache(rowKey,columnMap);
        List<RowBean> rowBeans = new ArrayList<>();
        rowBeans.add(rowBean);

       elasticSearchService.receiveAndSave("ospAgg", Constant.INDEX_TYPE_AGG,rowBeans);
    }


    private static OspAggRowBean createRowBeanByTableCache(String rowKey,Map<String, Object> columnMap){

        if (!columnMap.containsKey(OspColumnKey.INVOKED_FAIL))
            columnMap.put(OspColumnKey.INVOKED_FAIL, 0);
        else if (!columnMap.containsKey(OspColumnKey.INVOKED_NUMS)
                || columnMap.get(OspColumnKey.INVOKED_NUMS).equals(0))
            columnMap.put(OspColumnKey.INVOKED_NUMS, 1);

        columnMap.put(OspColumnKey.INVOKED_SUCCESS,
                Integer.valueOf(columnMap.get(OspColumnKey.INVOKED_NUMS).toString())-
                        Integer.valueOf(columnMap.get(OspColumnKey.INVOKED_FAIL).toString()));

        String[] values = RawData.rowKey2Values(rowKey);

        Long channelId = Long.valueOf(values[0]);
        Long appId = Long.valueOf(values[1]);
        Long uId = Long.valueOf(values[6]);
        Long timestamp = Long.valueOf(values[8]);
        Long abilityId = Long.valueOf(values[9]);
        //String invokedDate  = format.format(timestamp);
       // DateTime invokedDate = transLongToDateTime(timestamp);
        //DateTime invokedDate = new DateTime(timestamp).plusHours(8);

        columnMap.put(OspColumnKey.CHANNEL_ID,channelId);
        columnMap.put(OspColumnKey.APP_ID,appId);
        columnMap.put(OspColumnKey.APP_NAME,values[2]);
        columnMap.put(OspColumnKey.ABILITY_ID,abilityId);
        columnMap.put(OspColumnKey.ABILITY_CODE ,values[3]);
        columnMap.put(OspColumnKey.ABILITY_NAME,values[4]);
        columnMap.put(OspColumnKey.CHANNEL_NAME,values[5]);
        columnMap.put(OspColumnKey.U_ID,uId);
        columnMap.put(OspColumnKey.INVOKED_TIME,timestamp);
        columnMap.put(OspColumnKey.EXP_CODE,values[7]);
        columnMap.put(OspColumnKey.INVOKED_DATE,
                Tools.buildElasticSearchTimeFormat(new DateTime(timestamp).plusHours(8)));
        long inTime = System.currentTimeMillis();
        //DateTime inDate = transLongToDateTime(inTime);
        columnMap.put(OspColumnKey.IN_TIME,inTime);
        columnMap.put(OspColumnKey.IN_DATE, Tools.buildElasticSearchTimeFormat(new DateTime(inTime).plusHours(8)));

        OspAggRowBean rowBean = OspAggRowBean.builder().id("testOspAgg").fields(columnMap).build();
        return  rowBean;
    }

    private static DateTime transLongToDateTime(Long timestamp) {
        try{
            Date date = format.parse(format.format(timestamp));
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.plusHours(8);
            return  dateTime;
        }catch (Exception e){
            LOG.error(e.getMessage());
            return null;
        }
    }
}
