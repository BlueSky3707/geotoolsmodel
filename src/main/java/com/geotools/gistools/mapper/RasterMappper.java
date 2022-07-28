package com.geotools.gistools.mapper;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/27 17:30
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface RasterMappper {
    int insertRater(List<Object> pRaster);
    double getMaxtime(String fieldMax);

}
