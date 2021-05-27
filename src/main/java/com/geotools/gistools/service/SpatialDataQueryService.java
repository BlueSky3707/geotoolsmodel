package com.geotools.gistools.service;

import com.geotools.gistools.exception.ExceptionMsg;
import com.geotools.gistools.request.QueryParam;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.Features;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:35
 */
public interface SpatialDataQueryService {
    Features search(QueryParameter queryParameter) throws RemoteException, ExceptionMsg;
    Features getDataByNameOrCode(QueryParam queryParam) ;
    Features bufferSearch(QueryParameter queryParameter) throws ExceptionMsg;
}
