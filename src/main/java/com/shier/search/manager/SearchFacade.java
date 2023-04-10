package com.shier.search.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shier.search.common.BaseResponse;
import com.shier.search.common.ErrorCode;
import com.shier.search.common.ResultUtils;
import com.shier.search.dataSource.*;
import com.shier.search.exception.BusinessException;
import com.shier.search.exception.ThrowUtils;
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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author SearchFacade
 * CreateTime 2023/4/2 22:20
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAll(@RequestBody SearchContentRequest searchContentRequest, HttpServletRequest request) {
        String type = searchContentRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchContentRequest.getSearchText();
        long current = searchContentRequest.getCurrent();
        long pageSize = searchContentRequest.getPageSize();
        // 为空搜索所有的内容
        if (searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                // 查询用户
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
                return userVOPage;
            });
            CompletableFuture<Page<Pictures>> pictureTask = CompletableFuture.supplyAsync(() -> {
                // 查询图片
                Page<Pictures> picturesPage = pictureDataSource.doSearch(searchText, current, pageSize);
                return picturesPage;
            });
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                // 查询文章
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText, 1, 10);
                return postVOPage;
            });
            // 形成阻塞
            CompletableFuture.allOf(userTask, pictureTask, postTask).join();
            try {
                Page<PostVO> postVOPage = postTask.get();
                Page<UserVO> userVOPage = userTask.get();
                Page<Pictures> picturesPage = pictureTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturesPage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "内容查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
