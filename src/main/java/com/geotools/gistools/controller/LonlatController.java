package com.geotools.gistools.controller;

import com.geotools.gistools.respose.ApiResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2020/11/13 11:39
 */
@RestController()
public class LonlatController {
    @RequestMapping(value = "/getLocation", method = RequestMethod.GET)
    public ApiResult getWgsLocationByGao() {

        ApiResult api = new ApiResult();
        api.setCode(200);
        api.setData("wwwww");


        return api;
    }
}
