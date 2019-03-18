package com.ai.aif.osp.jstorm.bean;

import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/12.
 */
@AutoValue
public abstract  class OspAggRowBean implements Serializable,RowBean {

    @Override
    public abstract String id();

    @Override
    public abstract Map<String, Object> fields();

    public static OspAggRowBean.Builder builder() {
        return new AutoValue_OspAggRowBean.Builder();
    }
    @AutoValue.Builder
    public abstract static class Builder{

        public abstract OspAggRowBean build();

        public abstract OspAggRowBean.Builder id(String id);

        public abstract OspAggRowBean.Builder fields(Map<String, Object> fields);

    }
}
