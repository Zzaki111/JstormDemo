package com.ai.aif.osp.jstorm.service;

import com.ai.aif.osp.jstorm.bean.OspRowBean;

import java.util.List;
import java.util.Map;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/5.
 */
public interface OspStatisticsService {

    void counter(List<OspRowBean> rowBeans);

    void attachOspStatistics(String rowKey, Map<String, Object> fieldMap);
}
