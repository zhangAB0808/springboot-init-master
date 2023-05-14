package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 搜索接口规范（每个新接入的数据源都要实现这个接口）
 * @author Administrator
 */
public interface DataSource<T> {

    /**
     * 统一搜索接口规范
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
