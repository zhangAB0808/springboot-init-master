package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Video;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 搜索视频数据源
 * @author Administrator
 */
@Service
    public class VideoDataSource implements DataSource<Video> {

    @Resource
    private VideoService videoService;

    @Override
    public Page<Video> doSearch(String searchText, long pageNum, long pageSize) {
        Page<Video> videoPage = videoService.searchVideo(searchText, pageNum, pageSize);
        return videoPage;
    }

}
