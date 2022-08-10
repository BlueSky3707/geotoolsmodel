package com.geotools.gistools.mapper;

import com.geotools.gistools.beans.City;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/11 16:03
 */
@Mapper
@Repository
public interface CityMapper {
    List<City> getList();
}
