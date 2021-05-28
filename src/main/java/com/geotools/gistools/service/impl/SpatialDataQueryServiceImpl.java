package com.geotools.gistools.service.impl;

import com.geotools.gistools.exception.ExceptionMsg;
import com.geotools.gistools.postgis.PostGisSearch;
import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.Features;
import com.geotools.gistools.service.SpatialDataQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.rmi.RemoteException;

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
    @Override
    public Features search(QueryParameter queryParameter) throws RemoteException, ExceptionMsg {
        Features features= postGisSearch.search(queryParameter);
        return features;
    }

    @Override
    public Features bufferSearch(QueryParameter queryParameter) {
        Features features= postGisSearch.bufferSearch(queryParameter);
        return features;
    }

	@Override
	public Features getDataByNameOrCode(QueryParam queryParam) {
		 Features features= postGisSearch.getDataByNameOrCode(queryParam);
		return features;
	}
}
