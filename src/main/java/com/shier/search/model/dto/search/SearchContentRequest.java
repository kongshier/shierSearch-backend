package com.shier.search.model.dto.search;

import com.shier.search.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 搜索内容
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchContentRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 类型
     */
    private String type;

    private static final long serialVersionUID = 1L;
}