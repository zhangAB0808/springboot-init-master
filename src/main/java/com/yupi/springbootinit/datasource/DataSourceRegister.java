package com.yupi.springbootinit.datasource;

import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源注册入map(注册器模式)，通过类型获得对应数据源
 *
 * @author Administrator
 */
@Component
public class DataSourceRegister {

    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private VideoDataSource videoDataSource;

    Map<String, DataSource> dataSourceMap;

    @PostConstruct
    public void init() {
        dataSourceMap = new HashMap() {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
            put(SearchTypeEnum.VIDEO.getValue(), videoDataSource);
        }};
    }

    public DataSource getDataSourceByType(String type) {
        if (dataSourceMap == null) {
            return null;
        }
        return dataSourceMap.get(type);
    }
}
