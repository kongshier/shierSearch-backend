package com.shier.search.dataSource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author Shier
 * 搜索的数据源
 */
public interface DataSource<T> {

    /**
     * 统一的搜索接口
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);

    // 更多功能的接口，比如视频啊聊天等

}