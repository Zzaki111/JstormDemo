package com.ai.aif.osp.jstorm.common.row;



import com.ai.aif.osp.jstorm.bean.OspRowBean;
import com.ai.aif.osp.jstorm.bean.RowBean;
import com.ai.aif.osp.jstorm.common.Constant;
import com.ai.aif.osp.jstorm.util.Strings;

import java.util.Map;
import java.util.Optional;

import static com.ai.aif.osp.jstorm.util.ConvertUtil.castLong;


/**
 * @Author :zuogy@asiainfo.com
 * Created on 2018/1/5.
 */
public class OspRowHandler implements RowFieldsHandler {


    public OspRowHandler(){

    }

    @Override
    public RowBean buildOnCondition(Map<String, Object> fields) {
        OspRowBean.Builder builder = OspRowBean.builder();
        //现在不加任何过滤，如果需要过滤，在doFilter中添加过滤逻辑
        Optional<Map<String, Object>> opt = Optional.ofNullable(fields);
        opt.ifPresent(map -> {
           // map.put(L4xRow.FIELD_SERVICENAME, serviceName);
            builder.id(Strings.castToString(map.get(Constant.FIELD_ID)))
                    .uId(castLong(map.get(Constant.FIELD_UID)))
                    .channelId(castLong(map.get(Constant.FIELD_CHANNELID)))
                    .channelName(Strings.castToString(map.get(Constant.FIELD_CHANNELNAME)).trim())
                    .appId(castLong(map.get(Constant.FIELD_APPID)))
                    .appName(Strings.castToString(map.get(Constant.FIELD_APPNAME)).trim())
                    .abilityId(castLong(map.get(Constant.FIELD_ABILITYID)))
                    .abilityName(Strings.castToString(map.get(Constant.FIELD_ABILITYNAME)).trim())
                    .abilityCode(Strings.castToString(map.get(Constant.FIELD_ABILITYCODE)))
                    .elapsedTime(castLong(map.get(Constant.FIELD_ELASPETIME)))
                    .invokedTime(castLong(map.get(Constant.FIELD_INVOKEDTIME)))
                    .success(Boolean.parseBoolean(Strings.castToString(map.get(Constant.FIELD_SUCCESS))))
                    .fields(map);
        });

        return opt.isPresent() ? builder.build() : null;
    }

    @Override
    public String generateTableName(Map<String, Object> fields) {
        return null;
    }

    @Override
    public Optional<Map<String, Object>> doFilter(Map<String, Object> fields) {
        //TODO 如果需要加过滤可以在此方法中添加过滤规则
        return null;
    }

}
