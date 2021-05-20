package com.geotools.gistools.postgis;


import com.geotools.gistools.mapper.CommonMapper;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:52
 */
@Component
public class PostGisSearch {
    private static final Logger logger = LoggerFactory.getLogger(PostGisSearch.class);
    private static final double distance = 50;
    @Resource
    CommonMapper commonMapper;
    public Features search(QueryParameter queryParameter){
        return null;
    }
    public Features bufferSearch(QueryParameter queryParameter){
        return null;
    }

}
