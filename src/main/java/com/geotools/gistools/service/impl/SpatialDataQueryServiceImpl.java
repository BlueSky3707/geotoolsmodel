package com.geotools.gistools.service.impl;

import com.geotools.gistools.exception.ExceptionMsg;
import com.geotools.gistools.request.QueryParameter;
import com.geotools.gistools.respose.Features;
import com.geotools.gistools.service.SpatialDataQueryService;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:37
 */
@Service
public class SpatialDataQueryServiceImpl implements SpatialDataQueryService {
    @Override
    public Features search(QueryParameter queryParameter) throws RemoteException, ExceptionMsg {
        return null;
    }

    @Override
    public Features bufferSearch(QueryParameter queryParameter) throws ExceptionMsg {
        return null;
    }
}
