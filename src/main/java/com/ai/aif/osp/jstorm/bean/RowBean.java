package com.ai.aif.osp.jstorm.bean;

import com.ai.aif.osp.jstorm.util.ConvertUtil;
import com.ai.aif.osp.jstorm.util.Strings;
import com.ai.aif.osp.jstorm.common.row.RowHandlerFactory;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ai.aif.osp.jstorm.util.ConvertUtil.castLong;


/**
 * 单条记录的抽象，数据以Map存储于对象中
 *
 * @author gaoyuan7@asiainfo.com
 * @version 1.0
 * @company Asiainfo
 * @date 2017/8/4
 */
public interface RowBean extends java.io.Serializable {
    /**
     * @return 一条记录的唯一标识符
     */
    String id();

    /**
     * @return RowBean的所有属性
     */
    Map<String, Object> fields();

    /**
     * 返回字段值，没有返回空, 该方法只针对fields中的直接key值，如果存在多级Map要进行数据查找，使用方法{@link #findKeys(String)}
     *
     * @param filedName 字段名称
     * @return null if not found
     */
    default String field(String filedName) {
        return Strings.castToString(fields().get(filedName));
    }

    /**
     * 返回Long型字段值，没有返回空, 该方法只针对fields中的直接key值，如果存在多级Map要进行数据查找，使用方法{@link #findKeys(String)}
     *
     * @param filedName 字段名称
     * @return null if not found
     */
    default long fieldLong(String filedName) {
        return castLong(fields().get(filedName));
    }

    /**
     * 通过字符串数组创建RowBean的list对象
     *
     * @param json 数据字符串
     * @return 结果对象列表
     */
    static List<RowBean> fromJsonArray(String json, RowHandlerFactory factory) {
        //将json转换为Map对象
        List<Map<String, Object>> rstList = ConvertUtil.fromJsonArray(json,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        return rstList.stream()
                .map(fields -> factory.createHandler(fields).buildOnCondition(fields)) //调用handler的onbuild处理字段值，生成不同的RowBean
                .collect(Collectors.toList());
    }

    /**
     * 查找是否存在指定的key。key可以是多级map的路径，使用/进行分隔。
     *
     * @param keyStr key路径
     * @return true表示key路径存在
     */
    default boolean hasKeys(String keyStr) {
        String[] keys = keyStr.split("/");
        Object map = fields();
        for (String key : keys) {
            if (!(map instanceof Map && ((Map) map).containsKey(key)))
                return false;
            map = ((Map) map).get(key);
        }
        return true;
    }


    /**
     * 查找指定的key，并返回数据值以String对象的形式。key可以是多级map的路径，使用/进行分隔。
     *
     * @param keyStr key路径
     * @return 对应key的值
     */
    default String findKeys(String keyStr) {
        String[] keys = keyStr.split("/");
        Object map = fields();
        for (String key : keys) {
            if (!(map instanceof Map && ((Map) map).containsKey(key)))
                return null;
            map = ((Map) map).get(key);
        }
        return Strings.castToString(map);
    }

    /**
     * 修改指定路径下的的键值。key可以是多级map的路径，使用/进行分隔。
     *
     * @param keyStr key路径
     * @return true表示修改成功
     */
    default boolean setKeys(String keyStr, String value) {
        String[] keys = keyStr.split("/");
        Object map = fields();
        for (int i = 0; i < keys.length - 1; i++) {
            if (!(map instanceof Map && ((Map) map).containsKey(keys[i])))
                return false;
            map = ((Map) map).get(keys[i]);
        }
        if (!(map instanceof Map && ((Map) map).containsKey(keys[keys.length-1])))
            return false;
        else
            //noinspection unchecked
            ((Map) map).put(keys[keys.length-1], value);
        return true;
    }
}

