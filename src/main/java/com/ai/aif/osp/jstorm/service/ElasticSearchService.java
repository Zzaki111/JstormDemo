package com.ai.aif.osp.jstorm.service;


import com.ai.aif.osp.jstorm.bean.RowBean;

import java.util.List;
import java.util.Map;

/**
* @date: 2018/3/27 17:20
* @author: zuogy
**/

public interface ElasticSearchService {
    /**
     * 保存日志信息到指定的主题连接
     *
     * @param topic    存储主题
     * @param rowBeans 存储日志数据内容
     * @return 返回存储结果
     */
     void receiveAndSave(String topic, List<RowBean> rowBeans);

    /**
     * 保存日志信息到指定的主题连接
     *
     * @param msgType  ES存储的数据_type
     * @param topic    存储主题
     * @param rowBeans 存储日志数据内容
     * @return 返回存储结果
     */
     void receiveAndSave(String msgType, String topic, List<RowBean> rowBeans);

}
