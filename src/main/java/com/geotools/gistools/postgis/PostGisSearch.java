package com.geotools.gistools.postgis;
import com.geotools.gistools.mapper.CityptDao;
import com.geotools.gistools.mapper.CommonMapper;
import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.CallbackAbleFeature;
import com.geotools.gistools.respose.Features;
import com.geotools.gistools.respose.Field;
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

    /**
     * @param queryParameter
     * @return
     */

    public Features search(QueryParameter queryParameter){
    	Features featuresSet = new Features();
    	String outFields = queryParameter.getOutFields();
    	List<Field> fields=new ArrayList<Field>();
    	if(outFields==null||outFields.equals("")||outFields.equals("*")) {//不传参默认全部
    		queryParameter.setOutFields("*");
    	}
    	List<Map<String, Object>> lists = commonMapper.search(queryParameter);
    	creatFeatures(queryParameter, featuresSet, fields, lists);
    	
    	
        return featuresSet;
    }

    public Features bufferSearch(QueryParameter queryParameter){
    	Features featuresSet = new Features();
    	String outFields = queryParameter.getOutFields();
    	List<Field> fields=new ArrayList<Field>();
    	if(outFields==null||outFields.equals("")||outFields.equals("*")) {//不传参默认全部
    		queryParameter.setOutFields("*");
    	}
    	List<Map<String, Object>> lists = commonMapper.bufferSearch(queryParameter);
    	
    	creatFeatures(queryParameter, featuresSet, fields, lists);
    	
        return featuresSet;
    }
	/**
	 * featuresSet属性设置
	 * @param queryParameter
	 * @param featuresSet
	 * @param fields
	 * @param lists
	 */
	private void creatFeatures(QueryParameter queryParameter, Features featuresSet, List<Field> fields,
			List<Map<String, Object>> lists) {
		List<CallbackAbleFeature> features=new ArrayList<CallbackAbleFeature>();
    	List<Map<String, Object>> columns = commonMapper.getColumns(queryParameter.getLayerName());
    	
    	addFields(queryParameter.getOutFields(), fields, columns);//生成fields
    	for (Map<String, Object> map : lists) {
    		CallbackAbleFeature callbackAbleFeature = new CallbackAbleFeature();
    		if(queryParameter.isReturnGeometry()) {
    			String wkt = map.get("geom").toString();			
				callbackAbleFeature.setGeoJson(wkt);


    		}
    		HashMap<String, Object> hashMap = new HashMap<String, Object>();

    		for (Field field : fields) {
    			hashMap.put(field.getName(), map.get(field.getName()));
    		}

    		callbackAbleFeature.setAttributes(hashMap);
    		features.add(callbackAbleFeature);

    	}
    	featuresSet.setAllCount(lists.size());
    	featuresSet.setLayerName(queryParameter.getLayerName());
    	featuresSet.setFeatures(features);
    	featuresSet.setFields(fields);
	}
    
	/**
	 * 按行政区名称或代码查询
	 * @param queryParam
	 * @return
	 */
	public Features getDataByNameOrCode(QueryParam queryParam) {
		Features featuresSet = new Features();
		String outFields = queryParam.getOutFields();
		List<Field> fields=new ArrayList<Field>();
		
		if(outFields==null||outFields.equals("")||outFields.equals("*")) {//不传参默认全部
			queryParam.setOutFields("*");
		}
		List<Map<String, Object>> lists = commonMapper.getDataByNameOrCode(queryParam);
    	List<CallbackAbleFeature> features=new ArrayList<CallbackAbleFeature>();
    	List<Map<String, Object>> columns = commonMapper.getColumns(queryParam.getLayerName());
    	addFields(queryParam.getOutFields(), fields, columns);
    	for (Map<String, Object> map : lists) {
    		CallbackAbleFeature callbackAbleFeature = new CallbackAbleFeature();
    		//获取几何信息
    		String wkt = map.get("geom").toString();
			callbackAbleFeature.setGeoJson(wkt);
    		//生成属性
    		HashMap<String, Object> hashMap = new HashMap<String, Object>();
    		for (Field field : fields) {
    			hashMap.put(field.getName(), map.get(field.getName()));
			}
    		callbackAbleFeature.setAttributes(hashMap);
    		
    		features.add(callbackAbleFeature);
    	}
    	featuresSet.setAllCount(features.size());
    	featuresSet.setFeatures(features);
    	featuresSet.setFields(fields);
    	featuresSet.setLayerName(queryParam.getLayerName());
		return featuresSet;
	}
	/**
	 * 添加field
	 * @param queryParam
	 * @param fields
	 * @param columns
	 */
	private void addFields(String outFields, List<Field> fields, List<Map<String, Object>> columns) {
		String[] atrs;
		if(outFields.equals("*")) {
    		for (Map<String, Object> map : columns) {
				 String column_name = map.get("column_name").toString();
				 if(!column_name.equals("geom")) {
					 typeTran(fields, map, column_name);
				 }
			}
    	}else {
    		atrs=outFields.split(",");
    		for (Map<String, Object> map : columns) {
    			 String column_name = map.get("column_name").toString();
				for (String atr : atrs) {
					if(atr.equals(column_name)) {
						typeTran(fields, map, column_name);
					}
					 
				}
			}
    	}
	}
	
	/**
	 * 数据类型转化并添加field
	 * @param fields
	 * @param map
	 * @param column_name
	 */
	private void typeTran(List<Field> fields, Map<String, Object> map, String column_name) {
		String data_type = map.get("data_type").toString();
		 if(data_type.indexOf("int")>-1) {
			 data_type="integer";
		 }else if(data_type.indexOf("character")>-1) {
			 data_type="string";
		 }else if(data_type.indexOf("numeric")>-1||data_type.indexOf("float8")>-1) {
			 data_type="double";
		 }
		 Field field = new Field(column_name,data_type,column_name);
		 fields.add(field);
	}

}
