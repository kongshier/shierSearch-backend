package com.shier.search.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shier.search.common.ErrorCode;
import com.shier.search.esdao.PostEsDao;
import com.shier.search.exception.BusinessException;
import com.shier.search.model.dto.post.PostEsDTO;
import com.shier.search.model.entity.Post;
import com.shier.search.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.Assertions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取初始贴子的列表
 */
// @Component 取消注释后，每次执行都会执行一次run方法
// @Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 1. 获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        // 爬取的网站地址
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        // System.out.println(result);
        // 2. 转换为JSON数据
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        // 先获取到code是否为0
        int code = (int) map.get("code");
        if (code == 0) {
            // data 转换为JSON
            JSONObject data = (JSONObject) map.get("data");
            // records转换为JSON
            JSONArray records = (JSONArray) data.get("records");
            List<Post> postList = new ArrayList<>();
            for (Object record : records) {
                JSONObject temRecord = (JSONObject) record;
                Post post = new Post();
                Object title = temRecord.get("title");
                Object content = temRecord.get("content");
                Object tags = temRecord.get("tags");
                Object userId = temRecord.get("userId");
                if (StringUtils.isBlank(String.valueOf(title)) || StringUtils.isBlank(String.valueOf(content))
                        || StringUtils.isBlank(String.valueOf(tags)) || StringUtils.isBlank(String.valueOf(userId))) {
                    throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "标题/内容/标签为空");
                }
                post.setTitle(temRecord.getStr("title"));
                post.setContent(temRecord.getStr("content"));
                JSONArray tagsList = (JSONArray) temRecord.get("tags");
                List<String> toList = tagsList.toList(String.class);
                post.setTags(JSONUtil.toJsonStr(toList));
                String userId2 = (String) temRecord.get("userId");
                long aLong = Long.parseLong(userId2);
                post.setUserId(aLong);
                postList.add(post);
            }
            // System.out.println(postList);
            try {
                boolean savePost = postService.saveBatch(postList);
                if (savePost){
                    log.info("获取初始帖子列表成功，总共条数={}", postList.size());
                }
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "获取初始化帖子失败");
            }
        }
    }
}
