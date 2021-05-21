package com.geotools.gistools.service.impl;

import com.geotools.gistools.beans.City;
import com.geotools.gistools.mapper.CityMapper;
import com.geotools.gistools.mapper.CityptDao;
import com.geotools.gistools.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/11 16:03
 */
@Service
public class CityServiceImpl implements CityService {
    @Autowired(required=false)
    private CityMapper cityMapper;
    @Autowired(required=false)
    private CityptDao cityDao;
    @Override
    public List<City> getList() {
        return cityMapper.getList();
    }
    
    public void update() {
    	List<String> group = cityDao.group();
    	for (String id : group) {
    		List<Map<String, Object>> lists = cityDao.findlist(id);
    		for(int i=0;i<lists.size();i++) {
    			Map<String, Object> map = lists.get(i);
    			int gid = Integer.parseInt(map.get("gid").toString());
    			String idd = map.get("id").toString()+"00"+i;
    			int update = cityDao.update(gid, idd);
    		}
    		
		}
    }
    
}
