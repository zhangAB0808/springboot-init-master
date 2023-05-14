package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Video;

/**
 * 视频服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface VideoService {

        /**
         * 搜索视频
         * @param searchText
         * @param pageNum
         * @param pageSize
         * @return
         */
        Page<Video> searchVideo(String searchText, long pageNum, long pageSize);
}
