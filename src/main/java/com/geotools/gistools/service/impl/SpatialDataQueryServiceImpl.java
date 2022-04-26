package com.geotools.gistools.service.impl;

import com.geotools.gistools.exception.ExceptionMsg;
import com.geotools.gistools.mapper.CommonMapper;
import com.geotools.gistools.postgis.PostGisSearch;
import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.request.QueryTablesParameter;
import com.geotools.gistools.request.RoadAnalysisParam;
import com.geotools.gistools.respose.Features;
import com.geotools.gistools.service.SpatialDataQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:37
 */
@Service
public class SpatialDataQueryServiceImpl implements SpatialDataQueryService {
    @Resource
    private PostGisSearch postGisSearch;
    @Resource
    private CommonMapper commonMapper;

    @Override
    public Features search(QueryParameter queryParameter) throws RemoteException, ExceptionMsg {
        Features features = postGisSearch.search(queryParameter);
        return features;
    }

    @Override
    public Features bufferSearch(QueryParameter queryParameter) {
        Features features = postGisSearch.bufferSearch(queryParameter);
        return features;
    }

    @Override
    public Features getDataByNameOrCode(QueryParam queryParam) {
        Features features = postGisSearch.getDataByNameOrCode(queryParam);
        return features;
    }

    @Override
    public int insertData(HashMap<String, Object> obj) {

        return commonMapper.insertData(obj);
    }

    @Override
    public int updateData(HashMap<String, Object> obj) {

        return commonMapper.updateData(obj);
    }

    @Override
    public int deleteData(HashMap<String, Object> obj) {

        return commonMapper.deleteData(obj);
    }

    @Override
    public List<HashMap<String, Object>> getGroupData(String layername,String filter, String citytable, String outFields, String type) {

        return commonMapper.getGroupData(layername,filter, citytable, outFields, type);
    }

    @Override
    public String getCityNameByLatLng(String tablename, String cityname, String lng, String lat) {

        return commonMapper.getCityNameByLatLng(tablename, cityname, lng, lat);
    }

    @Override
    public Features roadAnaysis(RoadAnalysisParam roadAnalysisParam) {
        return postGisSearch.roadAnaysis(roadAnalysisParam);
    }

	@Override
	public Features searchByTables(QueryTablesParameter queryParameter) {
		
	  return postGisSearch.searchByTables(queryParameter);
	}
	@Override
	public Map<String, Object> getMax(Map<String, Object> obj) {
		// TODO Auto-generated method stub
		return commonMapper.getMax(obj);
		
	}
}
