package com.geotools.gistools.postgis;


import com.alibaba.fastjson.JSON;
import com.geotools.gistools.mapper.CityptDao;
import com.geotools.gistools.mapper.CommonMapper;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.CallbackAbleFeature;
import com.geotools.gistools.respose.Features;
import com.geotools.gistools.utils.WktAndGeom;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:52
 */
@Component
public class PostGisSearch {
    private static final Logger logger = LoggerFactory.getLogger(PostGisSearch.class);
    private static final double distance = 50;
    
    @Autowired(required=false)
    CommonMapper commonMapper;
    public Features search(QueryParameter queryParameter){
    	Features featuresSet = new Features();
    	List<Map<String, Object>> lists = commonMapper.search(queryParameter);
    	List<CallbackAbleFeature> features=new ArrayList<CallbackAbleFeature>();
    	String outFields = queryParameter.getOutFields();
    	String[] atrs = outFields.split(",");
    	try {
    	for (Map<String, Object> map : lists) {
    		CallbackAbleFeature callbackAbleFeature = new CallbackAbleFeature();
    		if(queryParameter.isReturnGeometry()) {
    			String wkt = map.get("geom").toString();
    			WKTReader reader = new WKTReader();
    			Geometry geom = reader.read(wkt);
//    			WktAndGeom wktAndGeom = new WktAndGeom();
//    			Geometry geom =wktAndGeom.createGeometryByWKT(wkt);
//    			Map geo = JSON.parseObject(JSON.toJSONString(geom), Map.class);
				callbackAbleFeature.setGeometry(geom);
			System.out.println(callbackAbleFeature.getGeometry());
			System.out.println(callbackAbleFeature.getGeometry().getGeometryType());
				
				
    			
    		}
    		HashMap<String, Object> hashMap = new HashMap<String, Object>();
    		for (String atr : atrs) {
    			hashMap.put(atr, map.get("atr"));
			}
    		callbackAbleFeature.setAttributes(hashMap);
    		features.add(callbackAbleFeature);
    		
		}
    	} catch (ParseException e) {
			
			e.printStackTrace();
		}
//    	String outFields = queryParameter.getOutFields();
    	featuresSet.setAllCount(lists.size());
    	featuresSet.setLayerName(queryParameter.getLayerName());
    	featuresSet.setFeatures(features);
//    	featuresSet.setFields(fields);
    	
        return featuresSet;
    }
    public Features bufferSearch(QueryParameter queryParameter){
        return null;
    }

}
