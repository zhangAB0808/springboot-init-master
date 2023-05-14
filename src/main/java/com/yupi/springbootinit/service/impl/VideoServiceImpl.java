package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Video;
import com.yupi.springbootinit.service.VideoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class VideoServiceImpl implements VideoService {


    @Override
    public Page<Video> searchVideo(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/videos/search?q=%s&first=%s",searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        Elements select = doc.select(".dg_u");
        List<Video> videoList = new ArrayList<>();
        for (Element element : select) {
            //取图片url
            String src = element.select(".rms_iac").get(0).attr("data-src");
            //取图片标题
            String title = element.select(".rms_iac").get(0).attr("data-alt");
            //取视频地址
            String ourl = element.select(".mc_vtvc_con_rc").get(0).attr("ourl");
            Video video = new Video();
            video.setTitle(title);
            video.setUrl(src);
            video.setHref(ourl);
            videoList.add(video);
            if (videoList.size() >= pageSize) {
                break;
            }
        }
        Page<Video> videoPage = new Page<>(pageNum, pageSize);
        videoPage.setRecords(videoList);
        return videoPage;
    }
}
