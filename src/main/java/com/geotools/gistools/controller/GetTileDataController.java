package com.geotools.gistools.controller;

import com.geotools.gistools.request.TileParam;
import com.geotools.gistools.service.GetTileUrlService;
import com.geotools.gistools.utils.TileUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/21 15:16
 */
@RestController
@RequestMapping("rest")
@Api(description = "空间数据查询REST实现")
public class GetTileDataController {
    private static final Logger logger = LoggerFactory.getLogger(GetTileDataController.class);
    @Autowired
    private GetTileUrlService getTileUrlService;
    @RequestMapping(value = "/getSimpleTileUrl",method = RequestMethod.GET)
    public String getSimpleTileUrl(@RequestParam(value = "layerName", required = true) String layerName,
                                   @RequestParam(value = "ceterX", required = true) Double ceterX,
                                   @RequestParam(value = "ceterY", required = true) Double ceterY,
                                   @RequestParam(value = "layerZoom", required = true) int layerZoom) throws IOException {


        System.out.println(111);
        TileParam tileParam=new TileParam();
        tileParam.centerX=ceterX;
        tileParam.centerY=ceterY;
        tileParam.zoom=layerZoom;
        getTileUrlService.getSimpleTileUrl(tileParam);
        return "";

    }
}
