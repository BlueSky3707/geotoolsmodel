package com.geotools.gistools.controller;

import com.geotools.gistools.beans.InterpolData;
import com.geotools.gistools.request.InterpolParam;
import com.geotools.gistools.respose.ApiResult;
import com.geotools.gistools.utils.GeoAnlysisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/9/27 9:54
 */
@RestController
@RequestMapping("rest")
@Api(description = "地理分析")


public class GeoAnalysis {
    @Autowired
    private GeoAnlysisUtils geoAnlysisUtils;

    @ApiOperation(value = "生成等值线")
    @PostMapping("/createContour")
    @ResponseBody
    @CrossOrigin
    public ApiResult createContour(@RequestBody InterpolParam inputData) throws SchemaException, IOException, CQLException, ParseException {

        List<Map<String, Object>> pfeaCl = geoAnlysisUtils.getFeaturesInf(inputData);
        ApiResult api = new ApiResult();
        api.setCode(200);
        api.setData(pfeaCl);
        return api;
    }
}
