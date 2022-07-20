package com.geotools.gistools.controller;

import com.geotools.gistools.respose.ApiResult;
import com.geotools.gistools.utils.ShpFileUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2020/12/9 9:24
 */
@RestController()
public class ParseShpfileController {
    @Resource
    ShpFileUtils shpFileUtils;

    @RequestMapping(value = "/getShpInfo", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "fileName", required = true, dataType = "String", value = "空间数据文件路径"),
            @ApiImplicitParam(paramType = "query", name = "filter", required = false, dataType = "String", value = "属性条件过滤"),
            @ApiImplicitParam(paramType = "query", name = "selCity", required = false, dataType = "String", value = "通过地市名称获取几何体过滤"),
            @ApiImplicitParam(paramType = "query", name = "spatialFilter", required = false, dataType = "String", value = "空间过滤条件，标准的WKT"),
            @ApiImplicitParam(paramType = "query", name = "spatialRel", required = false, dataType = "String", allowableValues = "INTERSECTS,CONTAINS,DISJOINT,TOUCHES,CROSSES,WITHIN,OVERLAPS", value = "空间位置关系"),
           })
    public ApiResult getShpInfo(@RequestParam(value = "fileName", required = true) String fileName,
                                @RequestParam(value = "filter", required = false) String filter,
                                @RequestParam(value = "selCity", required = false) String selCity,
                                @RequestParam(value = "spatialFilter", required = false) String spatialFilter,
                                @RequestParam(value = "spatialRel", required = false) String spatialRel
    ) throws IOException, CQLException {

        Object datas=shpFileUtils.shape2Geojson("D:\\dev\\shanxi1.shp");
        //shpFileUtils.getFeatures();
//        SimpleFeatureCollection smple=shpFileUtils.readShp("");
//        SimpleFeatureIterator iterator = smple.features();
//        try {
//            while( iterator.hasNext() ){
//                SimpleFeature feature = iterator.next();
//                System.out.println( feature.getAttribute("AREA_NAME"));
//            }
//        }
//        finally {
//            iterator.close();
//        }
//
        ApiResult api = new ApiResult();
        api.setCode(200);
        System.out.println(datas);
        api.setData(datas);
        return api;
    }
}
