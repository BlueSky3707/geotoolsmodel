package com.geotools.gistools.controller;

import com.alibaba.fastjson.JSONObject;
import com.geotools.gistools.exception.ExceptionMsg;
import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.ApiResult;
import com.geotools.gistools.respose.Features;
import com.geotools.gistools.service.SpatialDataQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:32
 */
@RestController
@RequestMapping("rest")
@Api(description = "空间数据查询REST实现")
@CacheConfig(cacheNames = "cacheTest")
public class SpatialDataQueryController {
    private static final Logger logger = LoggerFactory.getLogger(SpatialDataQueryController.class);
    @Autowired
    private SpatialDataQueryService spatialDataQueryService;
    @ApiOperation(value = "空间数据属性查询")
    @RequestMapping(value = "search", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "layerName", required = true, dataType = "String", value = "空间数据库中的图层名称"),
            @ApiImplicitParam(paramType = "query", name = "filter", required = false, dataType = "String", value = "属性过滤条件，语法请参考SQL，例如：LXBM='G45' AND SXXFX=1"),
            @ApiImplicitParam(paramType = "query", name = "spatialFilter", required = false, dataType = "String", value = "空间过滤条件，标准的WKT"),
            @ApiImplicitParam(paramType = "query", name = "outFields", required = false, dataType = "String", value = "查询返回的字段,例如：LXBM,LXMC"),
            @ApiImplicitParam(paramType = "query", name = "isReturnGeometry", required = true, dataType = "Boolean", value = "是否返回空间数据"),
            @ApiImplicitParam(paramType = "query", name = "orderByFields", required = false, dataType = "String", value = "排序条件，语法参考SQL，例如：ORDER BY NAME DESC"),
            @ApiImplicitParam(paramType = "query", name = "spatialRel", required = true, dataType = "String", allowableValues = "INTERSECTS,CONTAINS,DISJOINT,TOUCHES,CROSSES,WITHIN,OVERLAPS", value = "空间位置关系"),
            @ApiImplicitParam(paramType = "query", name = "current", required = false, dataType = "String", value = "分页参数，第几页，不传此参数默认不分页，开始页数为1"),
            @ApiImplicitParam(paramType = "query", name = "limit", required = false, dataType = "String", value = "每页记录数，此参数可选，默认为10")})
    @Cacheable
    public ApiResult search(@RequestParam(value = "layerName", required = true) String layerName,
                            @RequestParam(value = "filter", required = false) String filter,
                            @RequestParam(value = "spatialFilter", required = false) String spatialFilter,
                            @RequestParam(value = "outFields", required = false) String outFields,
                            @RequestParam(value = "isReturnGeometry", required = true) Boolean isReturnGeometry,
                            @RequestParam(value = "orderByFields", required = false) String orderByFields,
                            @RequestParam(value = "spatialRel", required = false) String spatialRel,
                            @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) throws RemoteException, ExceptionMsg {
//        logger.info("空间数据属性查询调用接口开始=======图层名称:{},属性过滤条件:{},空间过滤条件:{},返回的字段:{},是否返回空间数据:{},排序条件:{},空间位置关系:{},页码:{},每页记录数:{}", layerName, filter, spatialFilter, outFields, isReturnGeometry, orderByFields, spatialRel, current, limit);
        ApiResult apiData=new ApiResult();
        QueryParameter param=new QueryParameter(layerName,filter,spatialFilter,outFields,isReturnGeometry,orderByFields,spatialRel,current,limit);
      
        Features pFeartrues= spatialDataQueryService.search(param);
        apiData.setData(pFeartrues);
        return apiData;
    }
    
    @ApiOperation(value = "缓冲查询")
    @RequestMapping(value = "bufferSearch", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "layerName", required = true, dataType = "String", value = "空间数据库中的图层名称"),
            @ApiImplicitParam(paramType = "query", name = "filter", required = false, dataType = "String", value = "属性过滤条件，语法请参考SQL，例如：LXBM='G45' AND SXXFX=1"),
            @ApiImplicitParam(paramType = "query", name = "spatialFilter", required = false, dataType = "String", value = "空间过滤条件，标准的WKT"),
            @ApiImplicitParam(paramType = "query", name = "outFields", required = false, dataType = "String", value = "查询返回的字段,例如：LXBM,LXMC"),
            @ApiImplicitParam(paramType = "query", name = "isReturnGeometry", required = true, dataType = "Boolean", value = "是否返回空间数据"),
            @ApiImplicitParam(paramType = "query", name = "orderByFields", required = false, dataType = "String", value = "排序条件，语法参考SQL，例如：ORDER BY NAME DESC"),
            @ApiImplicitParam(paramType = "query", name = "buffDis", required = false, dataType = "String", value = "缓冲距离"),
            @ApiImplicitParam(paramType = "query", name = "current", required = false, dataType = "String", value = "分页参数，第几页，不传此参数默认不分页，开始页数为1"),
            @ApiImplicitParam(paramType = "query", name = "limit", required = false, dataType = "String", value = "每页记录数，此参数可选，默认为10")})
    @Cacheable
    public ApiResult bufferSearch(@RequestParam(value = "layerName", required = true) String layerName,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "spatialFilter", required = false) String spatialFilter,
            @RequestParam(value = "outFields", required = false) String outFields,
            @RequestParam(value = "isReturnGeometry", required = true) Boolean isReturnGeometry,
            @RequestParam(value = "orderByFields", required = false) String orderByFields,
            
            @RequestParam(value = "buffDis", required = false) Integer buffDis,
            @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
    	ApiResult apiData=new ApiResult();
        QueryParameter param=new QueryParameter( layerName,  filter,  spatialFilter,  outFields,
    			 isReturnGeometry,  orderByFields,  buffDis,  current,  limit);
      
        Features pFeartrues= spatialDataQueryService.bufferSearch(param);
        
        apiData.setData(pFeartrues);
        return apiData;
    }
    
    @ApiOperation(value = "空间数据属性查询按行政区")
    @RequestMapping(value = "getDataByNameOrCode", method = RequestMethod.GET, produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "layerName", required = true, dataType = "String", value = "空间数据库中的图层名称"),
            @ApiImplicitParam(paramType = "query", name = "where", required = false, dataType = "String", value = "属性过滤条件，语法请参考SQL，例如：LXBM='G45' AND SXXFX=1"),
            @ApiImplicitParam(paramType = "query", name = "cityLayerName", required = true, dataType = "String", value = "城市空间数据库中的图层名称"),
            @ApiImplicitParam(paramType = "query", name = "cityname", required = false, dataType = "String", value = "城市名称过滤条件，语法请参考SQL，cityname='延安市'"),
            @ApiImplicitParam(paramType = "query", name = "citycode", required = false, dataType = "String", value = "城市代码，语法请参考SQL，citycode='600061000'"),
            @ApiImplicitParam(paramType = "query", name = "outFields", required = false, dataType = "String", value = "查询返回的字段,例如：LXBM,LXMC"),
            @ApiImplicitParam(paramType = "query", name = "orderByFields", required = false, dataType = "String", value = "排序条件，语法参考SQL，例如：ORDER BY NAME DESC"),
            @ApiImplicitParam(paramType = "query", name = "current", required = false, dataType = "String", value = "分页参数，第几页，不传此参数默认不分页，开始页数为1"),
            @ApiImplicitParam(paramType = "query", name = "limit", required = false, dataType = "String", value = "每页记录数，此参数可选，默认为10")})
    @Cacheable
    public ApiResult getDataByNameOrCode(@RequestParam(value = "layerName", required = true) String layerName,
								            @RequestParam(value = "where", required = false) String where,
								            @RequestParam(value = "cityLayerName", required = false) String cityLayerName,
								            @RequestParam(value = "cityname", required = false) String cityname,
								            @RequestParam(value = "citycode", required = false) String citycode,
								            @RequestParam(value = "outFields", required = false) String outFields,
								            @RequestParam(value = "orderByFields", required = false) String orderByFields,
								            @RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
								            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit){
    	  ApiResult apiData=new ApiResult();
          QueryParam param=new QueryParam( layerName,  cityLayerName,  cityname,  citycode,  where,
      			 outFields,  orderByFields,  current,  limit);
        
          Features pFeartrues= spatialDataQueryService.getDataByNameOrCode(param);
          
          apiData.setData(pFeartrues);
          return apiData;
    }
}
