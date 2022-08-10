package com.geotools.gistools.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/27 17:30
 */
@Repository
@Mapper
public interface RasterMapper {
	int insertRater(Map<String, Object> obj);

	String getMaxtime(@Param("fieldMax") String fieldMax, @Param("rastertype") String rastertype);

	double getMaxRmax(@Param("starttime") String starttime, 
			@Param("endtime") String endtime,
			@Param("rastertype") String rastertype);

	String getMaxRmaxToName(@Param("starttime") String starttime, 
			@Param("endtime") String endtime,
			@Param("rastertype") String rastertype,
			@Param("max") double max);
}
