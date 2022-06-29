package com.geotools.gistools.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geotools.gistools.request.RoadAnalysisParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.geotools.gistools.request.QueryParam;
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
    Integer searchCount(QueryParameter queryParameter);

    List<Map<String, Object>> bufferSearch(QueryParameter queryParameter);

    List<Map<String, Object>> getColumns(@Param("tablename") String tablename);

    List<Map<String, Object>> getDataByNameOrCode(QueryParam queryParam);

    int insertData(HashMap<String, Object> obj);

    int updateData(HashMap<String, Object> obj);

    int deleteData(HashMap<String, Object> obj);

    List<HashMap<String, Object>> getGroupData(@Param("layername") String layername,
    										   @Param("filter") String filter,
                                               @Param("citytablename") String citytablename,
                                               @Param("outFields") String outFields,
                                               @Param("type") String type);

    String getCityNameByLatLng(@Param("tablename") String tablename,
                               @Param("cityname") String cityname,
                               @Param("lng") String lng,
                               @Param("lat") String lat);
    List<Map<String, Object>> roadAnaysis(RoadAnalysisParam roadAnalysisParam);
    List<HashMap<String, Object>> getWllogAndBi(@Param("kssj") String kssj,
            @Param("jssj") String jssj,
            @Param("fid") Integer fid);
    Integer getWllogCount(@Param("fid") Integer fid);
    Map<String, Object> getMax(Map<String, Object> obj);
}
