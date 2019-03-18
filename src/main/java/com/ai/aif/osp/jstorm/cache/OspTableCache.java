package com.ai.aif.osp.jstorm.cache;


import com.ai.aif.osp.jstorm.common.OspColumnKey;
import com.ai.aif.osp.jstorm.service.OspStatisticsService;
import com.google.common.collect.HashBasedTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/8.
 */
public class OspTableCache extends AbstractTableCache {

    private static volatile Logger logger = LoggerFactory.getLogger(OspTableCache.class);


    private long secondUpdateTime = System.currentTimeMillis();

    /**
     * 数据从远程缓存更新到数据库的时间间隔
     */
    private long tickTime = 5 * 1000L;

    private static volatile int defaultRowSize = 1000;

    private StatsUpdateListener updateListener;

    /**
     * 处理更新操作的具体服务类，可将数据处理更新到数据库
     */
    private OspStatisticsService ospStatisticsService;

    @Inject
    public OspTableCache(OspStatisticsService ospStatisticsService, long flushPeriod){
        super(HashBasedTable.create(defaultRowSize, OspColumnKey.KEYS.length),flushPeriod);
        this.ospStatisticsService = ospStatisticsService;
        this.registerListener();
    }

    @Override
    public void updateRow(String rowKey, Long costTime, boolean isSuccess) {
        logger.trace("update local table cache and rowKey is {}", rowKey);
        setMax(rowKey, OspColumnKey.MAX_TIME, costTime);
        setMin(rowKey, OspColumnKey.MIN_TIME, costTime);
        setAvg(rowKey, OspColumnKey.AVG_TIME, costTime);
        incr(rowKey, OspColumnKey.INVOKED_NUMS);
        if (!isSuccess) {
            incr(rowKey, OspColumnKey.INVOKED_FAIL);
        }
        updateListener.updateToRemote(this);
    }

    @Override
    public void updateRows(List<RawData> dataList) {
        logger.trace("batch update local table cache and dataList's size: {}", dataList.size());
        dataList.forEach(data -> {
            String rowKey = data.getRowKey();
            Long costTime = data.getCostTime();
            setMax(rowKey, OspColumnKey.MAX_TIME, costTime);
            setMin(rowKey, OspColumnKey.MIN_TIME, costTime);
            setAvg(rowKey, OspColumnKey.AVG_TIME, costTime);
            incr(rowKey, OspColumnKey.INVOKED_NUMS);
            if (!data.isSuccess()) {
                incr(rowKey, OspColumnKey.INVOKED_FAIL);
            }
        });
    }

    private void registerListener(){
        this.updateListener = (AbstractTableCache table) ->{
            updateToDatabase(table);
        };
    }

    private void updateToDatabase(AbstractTableCache table){
        if (System.currentTimeMillis() - secondUpdateTime > tickTime) {
            table.rowMap().forEach((rowKey, columnMap) -> {
                //Map<String, String> fieldMap;
                    ospStatisticsService.attachOspStatistics(rowKey,(Map)columnMap);
            });
            //TODO 是否需要判断数据库更新时异常情况？？
            table.clear();
            secondUpdateTime = System.currentTimeMillis();
        }
    }
}
