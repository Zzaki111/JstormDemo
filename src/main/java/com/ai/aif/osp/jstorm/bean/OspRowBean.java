package com.ai.aif.osp.jstorm.bean;

import com.ai.aif.osp.jstorm.common.row.RowHandlerFactory;
import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/4.
 */
@AutoValue
public abstract class OspRowBean implements Serializable,RowBean{

    private static final long serialVersionUID = 3932427040029051187L;

    public abstract String id();

    public abstract long uId();

    public abstract long channelId();

    public abstract String channelName();

    public abstract long appId();

    public abstract String appName();

    public abstract long abilityId();

    public abstract String abilityName();

    public abstract String abilityCode();

    public abstract long invokedTime();

    public abstract long elapsedTime();

    public abstract boolean success();

    /**
     * 其他相关属性
     *
     * @return 属性映射表
     */
    public abstract Map<String, Object> fields();

    /**
     * 从json数组创建对象列表
     *
     * @param jsonStr json字符串
     * @return 结果对象列表
     */
    public static List<RowBean> createByJsonArray(String jsonStr, RowHandlerFactory factory) {
        return RowBean.fromJsonArray(jsonStr, factory);
    }

    public static Builder builder() {
        return new AutoValue_OspRowBean.Builder();
    }
    @AutoValue.Builder
    public abstract static class Builder{

        public abstract OspRowBean build();

        public abstract Builder id(String id);

        public abstract Builder uId(long uId);

        public abstract Builder channelId(long channelId);

        public abstract Builder channelName(String channelName);

        public abstract Builder appId(long appId);

        public abstract Builder appName(String appName);

        public abstract Builder abilityId(long abilityId);

        public abstract Builder abilityName(String abilityName);

        public abstract Builder abilityCode(String abilityCode);

        public abstract Builder invokedTime(long invokedTime);

        public abstract Builder elapsedTime(long elapsedTime);

        public abstract Builder success(boolean success);

        public abstract Builder fields(Map<String, Object> fields);

    }


}
