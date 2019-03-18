package com.ai.aif.osp.jstorm.cache;

/**
 * 缓存数据对象,保存在本地或者远程缓存中，记录统计结果信息
 * <p>
 * </p>
 *
 * @author gaoyuan7@asiainfo.com
 * @version 1.0
 * @company Asiainfo
 * @date 2017/6/17
 */
public class RawData {
    private String rowKey;
    private Long costTime;
    private boolean isSuccess;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    /**
     * Combine the specific values into one String as the rowkey.
     *
     * @param values element of key
     * @return rowkey
     */
    public static String values2RowKey(String... values) {
        if (values == null || values.length == 0)
            return "#";
        StringBuilder sb = new StringBuilder();
        sb.append(values[0]);
        if (values.length > 1) {
            for (int i = 1; i < values.length; i++) {
                sb.append("#").append(values[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Reverse to values
     * @param rowKey
     * @return
     */
    public static String[] rowKey2Values(String rowKey) {
        return rowKey.split("#");
    }
}