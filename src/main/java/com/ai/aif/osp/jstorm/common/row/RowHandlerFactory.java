package com.ai.aif.osp.jstorm.common.row;



import java.util.Map;

/**
 * 生成不同Handler的工厂类
 * <p>
 * </p>
 *
 * @author gaoyuan7@asiainfo.com
 * @version 1.0
 * @company Asiainfo
 * @date 2017/8/10
 * TODO ：实现RowBean的池化实现，避免大量对象的新建和销毁操作，需要实现序列化
 */
public abstract class RowHandlerFactory {
    /**
     * 过滤器
     */

    public RowHandlerFactory(){
    }


    /**
     * 根据传入参数类型确定返回行处理器对象
     * @param fields 数据映射表
     * @return 过滤Handler对象
     */
    public abstract RowFieldsHandler createHandler(Map<String, Object> fields);
}
