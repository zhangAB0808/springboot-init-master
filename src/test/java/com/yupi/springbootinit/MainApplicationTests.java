package com.yupi.springbootinit;
import com.google.common.collect.Lists;

import java.util.*;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.config.WxOpenConfig;

import javax.annotation.Resource;

import com.yupi.springbootinit.esdao.PostEsDao;
import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.entity.Video;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;
    @Resource
    private PostService postService;
    @Resource
    private PostEsDao postEsDao;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

    @Test
    void testFetchPassage() {
        String json = "{\n" +
                "    \"sortField\": \"createTime\",\n" +
                "    \"sortOrder\": \"descend\",\n" +
                "    \"reviewStatus\": 1,\n" +
                "    \"current\": 1\n" +
                "}";
        String url = "https://www.code-nav.cn/api/post/list/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        List<Post> postList = new ArrayList<>();
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1652930776609996801L);
            postList.add(post);
        }
        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
    }

    @Test
    void testFetchPicture() throws IOException {
        String current = "1";
        String url = String.format("https://cn.bing.com/images/search?q=小黑子&first=%s", current);
        Document doc = Jsoup.connect(url).get();
        Elements select = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : select) {
            //取图片url
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            //取图片标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictureList.add(picture);
        }
        System.out.println(pictureList);
//        Elements newsHeadlines = doc.select("#mp-itn b a");
//        for (Element headline : newsHeadlines) {
//            log("%s\n\t%s",
//                    headline.attr("title"), headline.absUrl("href"));
//        }
    }

    @Test
    void testFetchVideo() throws IOException {
        String current = "1";
        String url = String.format("https://cn.bing.com/videos/search?q=鱼皮&first=%s", current);
        Document doc = Jsoup.connect(url).get();
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
        }
        System.out.println(videoList);
    }


    @Test
    void testEs(){
        PostEsDTO postEsDto = new PostEsDTO();
        postEsDto.setId(1L);
        postEsDto.setTitle("鱼皮是狗");
        postEsDto.setContent("鱼皮项目，聚合接口平台");
        postEsDto.setTags(Arrays.asList("java","python"));
        postEsDto.setUserId(1L);
        postEsDto.setCreateTime(new Date());
        postEsDto.setUpdateTime(new Date());
        postEsDto.setIsDelete(0);
        postEsDao.save(postEsDto);
    }

}
