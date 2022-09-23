package com.geotools.gistools.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.stereotype.Component;
import com.geotools.gistools.mapper.RasterMapper;


import org.gdal.gdal.Driver;

import org.gdal.gdalconst.gdalconst;

import org.gdal.ogr.DataSource;

import org.gdal.ogr.Feature;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;



/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/27 16:51
 */
@Component
public class RaterUtils {
	@Resource
	RasterMapper rasterMapper;

	/**
	 * @param rasterPath
	 *            tif文件路径
	 * @param type
	 *            类型
	 * @param filename
	 *            文件名
	 * @return
	 */
	public int culRasterInf(String rasterPath, String type, String filename) {
		int insertRater = 0;
		gdal.AllRegister();
		Dataset dataset = gdal.Open(rasterPath, gdalconstConstants.GA_ReadOnly);
		if (dataset == null) {
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
			return 0;
		}
		double[] min = new double[1];
		double[] max = new double[1];
		double[] mean = new double[1];
		double[] stddev = new double[1];
		Band band = dataset.GetRasterBand(1);
		if (band != null) {
			band.ComputeStatistics(false, min, max, mean, stddev);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rastertype", type);
			map.put("rastername", filename);
			map.put("ratertime", filename.replace(".tif", ""));
			map.put("rmax", max[0]);
			map.put("rmin", min[0]);
			map.put("mean", mean[0]);
			map.put("stddev", stddev[0]);

			insertRater = rasterMapper.insertRater(map);
		}
		return insertRater;
	}
	/*
	 * Java文件操作 获取不带扩展名的文件名
	 *
	 *  Created on: 2011-8-2
	 *      Author: blueeagle
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}
	public static void  FeatureToRasterPoint(String rasterTemp,String featurePath,String outPath,double pixel){
		Dataset dataset = gdal.Open(rasterTemp, gdalconstConstants.GA_ReadOnly);
		if (dataset == null) {
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		//打开矢量数据
		DataSource feaData=ogr.Open(featurePath);
		File tempFile =new File( featurePath.trim());
		String fileName = getFileNameNoEx(tempFile.getName());
		Layer player=feaData.GetLayer(fileName);
		Feature pfea= player.GetNextFeature();
		List<FeatureModel> feaLi=new ArrayList<>();
		DecimalFormat df=new DecimalFormat("######0.000");
		while(pfea!=null){
			FeatureModel pfe=new FeatureModel();
			pfe.pValue=pfea.GetFieldAsDouble("GRID_CODE");
			pfe.x=Double.valueOf(df.format(pfea.GetGeometryRef().GetX())).doubleValue();
			pfe.y=Double.valueOf(df.format(pfea.GetGeometryRef().GetY())).doubleValue();
			feaLi.add(pfe);
			pfea= player.GetNextFeature();
		}
		double[] pExtent=player.GetExtent();
		//行数和列数据计算
		int col=(int)((pExtent[1]-pExtent[0])/pixel);
		int row=(int)((pExtent[3]-pExtent[2])/pixel);
		double fx=pExtent[2];
		double fy=pExtent[0];
		double [][] resultArray=new double[row][col];
		Driver driver= gdal.GetDriverByName("GTiff");
		Dataset targetDataset= driver.Create(outPath, col, row, 1, gdalconst.GDT_Float64);
		double[] geoTransfoem ={pExtent[0],pixel,0,pExtent[2],0,pixel};
		targetDataset.SetGeoTransform(geoTransfoem);
		double[] tmpData = new double[col * row];
		float tmp;

		for (int i = 0; i < row; ++i)
		{
			for (int j = 0; j < col; ++j)
			{
				double nx=fx+0.01*i;
				double ny=fy+0.01*j;
				double nx1=Double.valueOf(df.format(nx)).doubleValue();
				double ny1=Double.valueOf(df.format(ny)).doubleValue();
				List<FeatureModel>dd= feaLi.stream().filter(FeatureModel->FeatureModel.getX()==ny1&&FeatureModel.getY()==nx1).collect(Collectors.toList());
				if(dd.size()>0){
					resultArray[i][j]=dd.get(0).getPValue();
				}else{
					resultArray[i][j]=-999;
				}
				tmpData[i * col + j] = resultArray[i][j];

			}
		}
		int check = targetDataset.GetRasterBand(1).WriteRaster(0, 0, col, row, gdalconst.GDT_Float64, tmpData);
		if(check == gdalconst.CE_Failure){
			System.out.println("失败");
		}else{
			System.out.println("成功");
		}
		targetDataset.FlushCache();
	}


}
