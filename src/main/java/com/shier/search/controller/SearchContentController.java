package com.shier.search.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.shier.search.common.BaseResponse;
import com.shier.search.common.ErrorCode;
import com.shier.search.common.ResultUtils;
import com.shier.search.exception.BusinessException;
import com.shier.search.exception.ThrowUtils;
import com.shier.search.manager.SearchFacade;
import com.shier.search.model.dto.post.PostQueryRequest;
import com.shier.search.model.dto.search.SearchContentRequest;
import com.shier.search.model.dto.user.UserQueryRequest;
import com.shier.search.model.entity.Pictures;
import com.shier.search.model.enums.SearchTypeEnum;
import com.shier.search.model.vo.PostVO;
import com.shier.search.model.vo.SearchVO;
import com.shier.search.model.vo.UserVO;
import com.shier.search.service.PictureService;
import com.shier.search.service.PostService;
import com.shier.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 图片接口
 *
 * @author Shier
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchContentController {
    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchContentRequest searchContentRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchContentRequest, request));
    }
}
