package com.geotools.gistools.utils;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/27 16:51
 */
@Component
public class RaterUtils {
    public Boolean culRasterInf(String rasterPath){
        Dataset dataset = gdal.Open(rasterPath, gdalconstConstants.GA_ReadOnly);
        if (dataset == null) {
            System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
            System.err.println(gdal.GetLastErrorMsg());
            System.exit(1);
            return false;
        }
        double[] min=new double[1];double[] max=new double[1];double[] mean=new double[1];double[] stddev=new double[1];
        Band band= dataset.GetRasterBand(1);
        if(band!=null){
            band.ComputeStatistics(false,min,max,mean,stddev);
        }
        return true;
    }
    public boolean insertData(String type,Double max,Double min,double mean,double stddev){

        return true;
    }

}
