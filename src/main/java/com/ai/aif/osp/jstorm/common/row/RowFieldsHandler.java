package com.ai.aif.osp.jstorm.common.row;

import com.ai.aif.osp.jstorm.bean.RowBean;

import java.util.Map;
import java.util.Optional;

/**
 * 记录处理对象，用于对来源数据的分类，将{@link RowBean}生成不同的特殊实现。
 * <p>
 * </p>
 *
 * @author gy
 * @version 1.0
 * @company Asiainfo
 * @date 2017/8/10
 */
public interface RowFieldsHandler {
    /**
     * 根据字段信息创建RowBean对象
     *
     * @param fields 字段信息map，需要根据handler不同进行过滤
     * @return RowBean对象，如果不需要进行对象创建，返回空
     */
    RowBean buildOnCondition(Map<String, Object> fields);

    /**
     * 生成表名，根据传入的字段信息
     *
     * @param fields 字段信息map
     * @return 表名集合名
     */
    String generateTableName(Map<String, Object> fields);

    /**
     * 对数据进行过滤筛选，分析是否满足过滤条件。
     *
     * @param fields 字段信息map
     * @return 返回过滤结果
     */
    Optional<Map<String, Object>> doFilter(Map<String, Object> fields);

}
