package com.shier.search.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shier.search.common.ErrorCode;
import com.shier.search.exception.BusinessException;
import com.shier.search.model.entity.Pictures;
import com.shier.search.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Shier 2023/3/16 14:32
 * 图片服务实现类
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Pictures> searchPicture(String searchText, long pageNum, long pageSize) {
        long currentPage = (pageNum - 1) * pageSize;
        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s", searchText, currentPage);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        // css类名
        Elements elements = doc.select(".iuscp.isv");
        List<Pictures> picturesList = new ArrayList<>();
        // 总数
        long count = 0;
        for (Element element : elements) {
            if (picturesList.size() >= pageSize) {
                break;
            }
            // 取图片地址url
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            count++;
            Pictures picture = new Pictures();
            picture.setTitle(title);
            picture.setUrl(murl);
            picturesList.add(picture);
        }
        Page<Pictures> picturePage = new Page<>(pageNum, pageSize, count);
        picturePage.setRecords(picturesList);
        return picturePage;
    }
}
