package com.ai.aif.osp.jstorm.cache;

import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractTableCache {

    /**
     * 数据更新的远程缓存的时间间隔，默认为5秒
     */
    protected long firstTickTime = 5000L;

    /**
     * 数据从远程缓存更新到数据库的时间间隔，默认为60秒
     */
    protected long secondTickTime = 60 * 1000L;

    /**
     * 是否启用远程缓存，默认不启用
     */
    protected boolean remoteCacheEnabled = false;

    /**
     * 服务统计本地缓存表
     */
    private Table<String, String, Long> table;

    // Constructor
    protected AbstractTableCache(Table table, long flushPeriod) {
        this.table = table;
        secondTickTime = flushPeriod;
    }

    // Accessors

    /**
     * Returns {@code true} if the table contains a mapping with the specified
     * row and column keys.
     *
     * @param rowKey    key of row to search for
     * @param columnKey key of column to search for
     */
    public boolean contains(String rowKey, String columnKey) {
        return this.table.contains(rowKey, columnKey);
    }

    /**
     * Returns {@code true} if the table contains a mapping with the specified
     * row key.
     *
     * @param rowKey key of row to search for
     */
    public boolean containsRow(String rowKey) {
        return this.table.containsRow(rowKey);
    }

    /**
     * Returns the value corresponding to the given row and column keys, or
     * {@code null} if no such mapping exists.
     *
     * @param rowKey    key of row to search for
     * @param columnKey key of column to search for
     */
    public Long get(String rowKey, String columnKey) {
        return this.table.get(rowKey, columnKey);
    }

    /**
     * Returns {@code true} if the table contains no mappings.
     */
    public boolean isEmpty() {
        return this.table.isEmpty();
    }

    /**
     * Returns the number of row key / column key / value mappings in the table.
     */
    public int size() {
        return this.table.size();
    }

    // Mutators

    /**
     * Removes all mappings dataFrom the table.
     */
    public void clear() {
        this.table.clear();
    }

    /**
     * Associates the specified value with the specified keys. If the table
     * already contained a mapping for those keys, the old value is replaced with
     * the specified value.
     *
     * @param rowKey    row key that the value should be associated with
     * @param columnKey column key that the value should be associated with
     * @param value     value to be associated with the specified keys
     * @return the value previously associated with the keys, or {@code null} if
     * no mapping existed for the keys
     */
    public Long put(String rowKey, String columnKey, Long value) {
        return this.table.put(rowKey, columnKey, value);
    }

    /**
     * Removes the mapping, if any, associated with the given keys.
     *
     * @param rowKey    row key of mapping to be removed
     * @param columnKey column key of mapping to be removed
     * @return the value previously associated with the keys, or {@code null} if
     * no such value existed
     */
    public Long remove(Object rowKey, Object columnKey) {
        return this.table.remove(rowKey, columnKey);
    }

    // Views

    /**
     * Returns a view of all mappings that have the given row key. For each row
     * key / column key / value mapping in the table with that row key, the
     * returned map associates the column key with the value. If no mappings in
     * the table have the provided row key, an empty map is returned.
     * <p>
     * <p>Changes to the returned map will update the underlying table, and vice
     * versa.
     *
     * @param rowKey key of row to search for in the table
     * @return the corresponding map dataFrom column keys to values
     */
    public Map<String, Long> row(String rowKey) {
        return this.table.row(rowKey);
    }

    /**
     * Returns a set of row keys that have one or more values in the table.
     * Changes to the set will update the underlying table, and vice versa.
     *
     * @return set of row keys
     */
    public Set<String> rowKeySet() {
        return this.table.rowKeySet();
    }

    /**
     * Returns a view that associates each row key with the corresponding map dataFrom
     * column keys to values. Changes to the returned map will update this table.
     * The returned map does not support {@code put()} or {@code putAll()}, or
     * {@code setValue()} on its entries.
     * <p>
     * <p>In contrast, the maps returned by {@code rowMap().get()} have the same
     * behavior as those returned by {@link #row}. Those maps may support {@code
     * setValue()}, {@code put()}, and {@code putAll()}.
     *
     * @return a map view dataFrom each row key to a secondary map dataFrom column keys to
     * values
     */
    public Map<String, Map<String, Long>> rowMap() {
        return this.table.rowMap();
    }

    // 计数

    /**
     * 步长加1
     *
     * @param rowKey    row key of mapping
     * @param columnKey column key of mapping
     * @return 增加以后的值
     */
    Long incr(String rowKey, String columnKey) {
        Long currentValue = get(rowKey, columnKey);
        if (currentValue != null) {
            return put(rowKey, columnKey, currentValue + 1L);
        }
        return put(rowKey, columnKey, 1L);
    }

    /**
     * 步长加特定值
     *
     * @param rowKey    row key of mapping
     * @param columnKey column key of mapping
     * @param value     value to be associated with the specified keys
     * @return 增加以后的值
     */
    Long incrBy(String rowKey, String columnKey, Long value) {
        Long currentValue = get(rowKey, columnKey);
        if (currentValue != null) {
            return put(rowKey, columnKey, currentValue + value);
        }
        return put(rowKey, columnKey, value);
    }

    /**
     * 设置最大值，如果表中对应字段已经有值，则设置为更大的值
     *
     * @param rowKey    row key of mapping
     * @param columnKey column key of mapping
     * @param value     value to be associated with the specified keys
     * @return 更大的那一个值
     */
    Long setMax(String rowKey, String columnKey, Long value) {
        Long currentValue = get(rowKey, columnKey);
        if (currentValue != null) {
            return put(rowKey, columnKey, Math.max(currentValue, value));
        }
        return put(rowKey, columnKey, value);
    }

    /**
     * 设置最小值，如果表中对应字段已经有值，则设置为更小的值
     *
     * @param rowKey    row key of mapping
     * @param columnKey column key of mapping
     * @param value     value to be associated with the specified keys
     * @return 更小的那一个值
     */
    Long setMin(String rowKey, String columnKey, Long value) {
        Long currentValue = get(rowKey, columnKey);
        if (currentValue != null) {
            return put(rowKey, columnKey, Math.min(currentValue, value));
        }
        return put(rowKey, columnKey, value);
    }

    /**
     * 设置平均值，如果表中对应字段已经有值，则设置为二者的平均值
     *
     * @param rowKey    row key of mapping
     * @param columnKey column key of mapping
     * @param value     value to be associated with the specified keys
     * @return 计算后的平均值
     */
    Long setAvg(String rowKey, String columnKey, Long value) {
        Long currentValue = get(rowKey, columnKey);
        if (currentValue != null) {
            return put(rowKey, columnKey, Math.addExact(currentValue, value) / 2);
        }
        return put(rowKey, columnKey, value);
    }

    /**
     * 设置总的值，如果表中对应字段已经有值，则设置为二者之和
     *
     * @param rowKey    row key of mapping
     * @param columnKey column key of mapping
     * @param value     value to be associated with the specified keys
     * @return 总值
     */
    Long setTotal(String rowKey, String columnKey, Long value) {
        Long currentValue = get(rowKey, columnKey);
        if (currentValue != null) {
            return put(rowKey, columnKey, Math.addExact(currentValue, value));
        }
        return put(rowKey, columnKey, value);
    }

    /**
     * 更新当前服务的相关的统计值
     *
     * @param rowKey    row key of mapping
     * @param costTime  服务调用耗时
     * @param isSuccess 服务调用返回是否成功
     */
    public abstract void updateRow(String rowKey, Long costTime, boolean isSuccess);

    /**
     * 批量更新多行数据的统计值
     *
     * @param dataList 原始服务数据列表
     */
    public abstract void updateRows(List<RawData> dataList);

    public void setTickTime(long firstTickTime, long secondTickTime) {
        this.firstTickTime = firstTickTime > 0 ? firstTickTime : 1000L;
        this.secondTickTime = secondTickTime > 1000 ? secondTickTime : 30 * 1000L;
        if (firstTickTime <= 0 || firstTickTime > secondTickTime) {
            this.remoteCacheEnabled = false;
        }
    }

    public boolean isRemoteCacheEnabled() {
        return remoteCacheEnabled;
    }

    public void setRemoteCacheEnabled(boolean remoteCacheEnabled) {
        this.remoteCacheEnabled = remoteCacheEnabled;
    }

    @Override
    public String toString() {
        return table.toString();
    }
}

