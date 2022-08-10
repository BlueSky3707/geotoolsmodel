package com.geotools.gistools.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;


@Repository
@Mapper
public interface CityptDao {
    List<Map<String, Object>> selectList();

    List<Map<String, Object>> selectList2(QueryParam queryParam);

    List<String> group();

    List<Map<String, Object>> findlist(@Param("id") String id);

    int update(@Param("gid") int gid, @Param("id") String id);

}