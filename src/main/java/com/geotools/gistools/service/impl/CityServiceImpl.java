package com.geotools.gistools.service.impl;

import com.geotools.gistools.beans.City;
import com.geotools.gistools.mapper.CityMapper;
import com.geotools.gistools.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/11 16:03
 */
@Service
public class CityServiceImpl implements CityService {
    @Autowired(required = false)
    private CityMapper cityMapper;

    @Override
    public List<City> getList() {
        return cityMapper.getList();
    }
}
