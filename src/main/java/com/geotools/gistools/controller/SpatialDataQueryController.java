package com.geotools.gistools.controller;

import com.geotools.gistools.respose.ApiResult;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:32
 */
@RestController
@RequestMapping("rest")
@Api(description = "空间数据查询REST实现")
@CacheConfig(cacheNames = "cacheTest")
public class SpatialDataQueryController {
    private static final Logger logger = LoggerFactory.getLogger(SpatialDataQueryController.class);
    @Cacheable
    public ApiResult search(){
        return null;
    }
    @Cacheable
    public ApiResult bufferSearch(){
        return null;
    }
}
