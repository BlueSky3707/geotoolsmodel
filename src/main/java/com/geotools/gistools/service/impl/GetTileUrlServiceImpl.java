package com.geotools.gistools.service.impl;

import com.geotools.gistools.request.TileParam;
import com.geotools.gistools.service.GetTileUrlService;
import com.geotools.gistools.utils.TileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/27 14:16
 */
@Service
public class GetTileUrlServiceImpl implements GetTileUrlService {
    @Autowired(required=false)
    private TileUtils tileUtils;
    @Override
    public String getSimpleTileUrl(TileParam tileParam) throws IOException {

        return tileUtils.getTileNumber(tileParam.centerX,tileParam.centerY,tileParam.zoom);
    }
}
