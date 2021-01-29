package com.geotools.gistools.controller;

import com.geotools.gistools.respose.ApiResult;
import com.geotools.gistools.utils.ShpFileUtils;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(value = "/getShpInfo",method = RequestMethod.GET)
    public ApiResult getShpInfo() throws IOException, CQLException {

        shpFileUtils.shape2Geojson("D:\\ProjectWorkSpace\\hbt2\\data\\sxxzqh.shp");
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
        return null;
   }
}
