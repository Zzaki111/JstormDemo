package com.ai.aif.osp.jstorm.cache;

public interface StatsUpdateListener {

    /**
     * 将本地缓存表中的源数据更新到远程缓存
     * @param table 源数据缓存表
     */
    void updateToRemote(AbstractTableCache table);
}
