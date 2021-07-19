package com.geotools.gistools.service;

import com.geotools.gistools.request.TileParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/27 14:14
 */
public interface GetTileUrlService {
    byte[] getSimpleTileUrl(TileParam tileParam) throws IOException;
    byte[] getAggregationTile(TileParam tileParam) throws IOException;
}
