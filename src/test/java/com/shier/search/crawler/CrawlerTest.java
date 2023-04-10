package com.shier.search.crawler;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shier.search.common.ErrorCode;
import com.shier.search.exception.BusinessException;
import com.shier.search.model.entity.Pictures;
import com.shier.search.model.entity.Post;
import com.shier.search.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Shier 2023/3/13 18:23
 * 抓取图片和文章
 */
@SpringBootTest
public class CrawlerTest {
    @Resource
    private PostService postService;

    /**
     * 爬取文章
     */
    @Test
    public void testFetchMessage() {

        // 1. 获取数据
        String json = "{\"current\":2,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
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
            boolean savePost = postService.saveBatch(postList);
            Assertions.assertTrue(savePost);
        }
    }

    /**
     * 爬取图片
     */
    @Test
    public void testFetchPicture() throws IOException {
        int currentPage = 1;
        String url = String.format("https://www.bing.com/images/search?q=美女&form=QBIR&first=%s", currentPage);
        Document doc = Jsoup.connect(url).get();
        // css类名
        Elements elements = doc.select(".iuscp.varh.isv");
        List<Pictures> picturesList = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址url
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // System.out.println(murl);
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            // System.out.println(title);
            Pictures picture = new Pictures();
            picture.setTitle(title);
            picture.setUrl(murl);
            picturesList.add(picture);
        }
        System.out.println(picturesList);
    }
}
