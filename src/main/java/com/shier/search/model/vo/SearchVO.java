package com.shier.search.model.vo;

import com.shier.search.model.entity.Pictures;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索分类
 */
@Data
public class SearchVO implements Serializable {
    private static final long serialVersionUID = -442981476297921234L;
    // 搜素用户
    private List<UserVO> userList;

    // 搜素文章
    private List<PostVO> postList;

    // 通用返回类型
    private List<?> dataList;

    // 图片搜素
    private List<Pictures> pictureList;
}
