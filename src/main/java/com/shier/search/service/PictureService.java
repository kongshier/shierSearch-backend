package com.shier.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shier.search.model.entity.Pictures;

/**
 * 图片服务
 */
public interface PictureService {
    Page<Pictures> searchPicture(String searchText, long pageNum, long pageSize);
}
