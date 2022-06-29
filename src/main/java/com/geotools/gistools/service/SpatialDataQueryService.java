package com.geotools.gistools.service;

import com.geotools.gistools.exception.ExceptionMsg;
import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.request.QueryTablesParameter;
import com.geotools.gistools.request.RoadAnalysisParam;
import com.geotools.gistools.respose.Features;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:35
 */
public interface SpatialDataQueryService {
    Features search(QueryParameter queryParameter) throws RemoteException, ExceptionMsg;

    Features getDataByNameOrCode(QueryParam queryParam);

    Features bufferSearch(QueryParameter queryParameter);
    
    Features searchByTables(QueryTablesParameter queryParameter);

    int insertData(HashMap<String, Object> obj);

    int updateData(HashMap<String, Object> obj);

    int deleteData(HashMap<String, Object> obj);

    List<HashMap<String, Object>> getGroupData(String layername,String filter, String citytablename, String outFields, String type);

    String getCityNameByLatLng(String tablename, String cityname, String lng, String lat);
    Features roadAnaysis(RoadAnalysisParam roadAnalysisParam);
    List<HashMap<String, Object>> getWllogAndBi(String kssj,String jssj, Integer fid);
    Integer getWllogCount(Integer fid);
    Map<String, Object> getMax(Map<String, Object> obj);
}
