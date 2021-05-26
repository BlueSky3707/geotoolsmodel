package com.geotools.gistools.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.geotools.gistools.request.QueryParameter;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/20 15:10
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface CommonMapper {
	 List<Map<String, Object>> search(QueryParameter queryParameter);

}
