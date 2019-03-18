package com.ai.aif.osp.jstorm.common.row;


import java.util.Map;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/5.
 */
public class OspRowHandlerFactory extends RowHandlerFactory {

    public OspRowHandlerFactory(){
    }
    @Override
    public RowFieldsHandler createHandler(Map<String, Object> fields) {
        return new OspRowHandler();
    }
}
