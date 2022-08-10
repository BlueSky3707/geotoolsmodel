package com.geotools.gistools.controller;

import com.geotools.gistools.mapper.RasterMapper;
import com.geotools.gistools.request.ShpParam;
import com.geotools.gistools.request.ShpQueryParam;
import com.geotools.gistools.respose.ApiResult;
import com.geotools.gistools.utils.FileUtils;
import com.geotools.gistools.utils.ShpFileUtils;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.osr.SpatialReference;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.gdalconst.gdalconst;

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
	@Resource
	FileUtils fileUtils;
	@Resource
	RasterMapper rasterMapper;
	@Value("${shp.citypath}")
	String citypath;
	@Value("${shp.shppath}")
	String shppath;
	Timer timer = null;
	TimerTask task =null;
	
	@RequestMapping(value = "/getShpInfo", method = RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "fileName", required = true, dataType = "String", value = "空间数据文件路径,例:gp\\sx.shp"),
			@ApiImplicitParam(paramType = "query", name = "filter", required = false, dataType = "String", value = "属性条件过滤,例:type=0"),
			@ApiImplicitParam(paramType = "query", name = "selCity", required = false, dataType = "String", value = "通过地市名称获取几何体过滤,例:name='西安市'"),
			@ApiImplicitParam(paramType = "query", name = "spatialFilter", required = false, dataType = "String", value = "空间过滤条件，标准的WKT，例:MULTIPOLYGON (((108 34,108 35,109 35,108 34)))"),
			@ApiImplicitParam(paramType = "query", name = "spatialRel", required = false, dataType = "String", allowableValues = "INTERSECTS,CONTAINS,DISJOINT,CROSSES", value = "空间位置关系"), })
	public ApiResult getShpInfo(@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "selCity", required = false) String selCity,
			@RequestParam(value = "spatialFilter", required = false) String spatialFilter,
			@RequestParam(value = "spatialRel", required = false) String spatialRel) throws IOException {
		ShpQueryParam shpQueryParam = new ShpQueryParam(shppath + "\\" + fileName + ".shp", citypath, selCity, filter,
				spatialFilter, spatialRel);
		List<Map<String, Object>> datas = shpFileUtils.shape2Geojson(shpQueryParam);
		ApiResult api = new ApiResult();
		api.setCode(200);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", datas.size());
		map.put("rows", datas);
		api.setData(map);
		return api;
	}
	@ApiOperation(value = "用于生态一张图sx_hbt中,获取最大rmax对应的name")
	@RequestMapping(value = "/getMaxRmaxToName", method = RequestMethod.GET)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "starttime", required = false, dataType = "String", value = "开始时间，格式：20220701"),
			@ApiImplicitParam(paramType = "query", name = "endtime", required = false, dataType = "String", value = "结束时间，格式：20220731"),
			@ApiImplicitParam(paramType = "query", name = "rastertype", required = true, dataType = "String", allowableValues = "CO,NO2,HCHO,O3,PM25,SO2,CHOCHO", value = "类型选择"), })
	public ApiResult getMaxRmaxToName(@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "rastertype", required = true) String rastertype) {
		double maxRmax = rasterMapper.getMaxRmax(starttime, endtime, rastertype);
		String maxRmaxToName = rasterMapper.getMaxRmaxToName(starttime, endtime, rastertype, maxRmax);
		ApiResult api = new ApiResult();
		api.setCode(200);
		api.setData(maxRmaxToName);
		return api;
	}
	@ApiOperation(value = "启动定时器")
	@RequestMapping(value = "/startTimer", method = RequestMethod.GET)
	public void startTimer() {
		if(timer!=null) {
			timer.cancel();
			timer=null;
		}
		if(task!=null) {
			task.cancel();
			task=null;
		}
		timer=new Timer();
		task = new TimerTask() {
	        public void run()
	        {
	        	fileUtils.startScheduled();
	        }
	    };

		
		timer.schedule( task,60000,5*60000 );
		
	}
	@ApiOperation(value = "关闭定时器")
	@RequestMapping(value = "/endTimer", method = RequestMethod.GET)
	public void endTimer() {
		if(timer!=null) {
		
			timer.cancel();
			timer=null;
			if(task!=null) {
				task.cancel();
				task=null;
			}
		}
	}
	/**
	 * @param shppraram
	 * @return
	 */
	@ApiOperation(value = "shp新增")
	 @RequestMapping(value = "/insertShp", method = RequestMethod.POST)
	 @ApiImplicitParam(paramType = "body", name = "shppraram", required = true, dataType = "ShpParam", value = "filename必填；list:[{'name':'dfs','geom': 'POINT(108.555 34.645)'}]；其他不填")
	 public ApiResult insertShp( @RequestBody  ShpParam shppraram) {
		shpFileUtils.setShp(shppraram,"insert");
	        ApiResult api = new ApiResult();
	        api.setCode(200);
	        api.setData(null);
	       
	        return api;
	  }
	
	 /**
		 * @param shppraram
		 * @return
		 */
		@ApiOperation(value = "shp更新")
		 @RequestMapping(value = "/updateShp", method = RequestMethod.POST)
		 @ApiImplicitParam(paramType = "body", name = "shppraram", required = true, dataType = "ShpParam", value = "filename必填；list:[{'name':'dfs','geom': 'POINT(108.555 34.645)'},'where':'FID_1=425']；其他不填")
		 public ApiResult updateShp( @RequestBody  ShpParam shppraram) {
			shpFileUtils.setShp(shppraram,"update");
		        ApiResult api = new ApiResult();
		        api.setCode(200);
		        api.setData(null);
		        return api;
		  }
		 /**
		 * @param shppraram
		 * @return
		 */
		@ApiOperation(value = "shp移除")
		 @RequestMapping(value = "/deleteShp", method = RequestMethod.POST)
		 @ApiImplicitParam(paramType = "body", name = "shppraram", required = true, dataType = "ShpParam", value = "filename必填；ids:[2，5，6]；filed:过滤条件字段必填；其他不填")
		 public ApiResult deleteShp( @RequestBody  ShpParam shppraram) {
			shpFileUtils.setShp(shppraram,"delete");
		        ApiResult api = new ApiResult();
		        api.setCode(200);
		        api.setData(null);
		        return api;
		  }
	/*
	 * 主要是针对环保厅水平台，计算大气相关的影像产品的最大值、最小值、均值、方差等
	 */

	@RequestMapping(value = "/calculateRaster", method = RequestMethod.GET)
	public ApiResult calculateRaster() {
		gdal.AllRegister();
		String rasterFilePath = "D:\\dev\\shaxi.tif";
		String fileUrl = rasterFilePath;
		// 读取影像数据
		Dataset dataset = gdal.Open(fileUrl, gdalconstConstants.GA_ReadOnly);
		if (dataset == null) {
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		// 读取影像信息 宽、高、波段数
		int xSize = dataset.getRasterXSize();
		int ySzie = dataset.getRasterYSize();
		int nBandCount = dataset.getRasterCount();
		int type = dataset.GetRasterBand(1).GetRasterDataType();
		// 读取仿射变换参数
		double[] im_geotrans = dataset.GetGeoTransform();

		Band band1 = dataset.GetRasterBand(1);
		double[] min = new double[1];
		double[] max = new double[1];
		double[] mean = new double[1];
		double[] stddev = new double[1];
		band1.ComputeStatistics(false, min, max, mean, stddev);

		// 读取投影
		String im_proj = dataset.GetProjection();
		Dataset d2 = gdal.GetDriverByName("GTiff").Create("D:\\dev\\xiand11.tif", xSize, ySzie, nBandCount, type);
		d2.SetGeoTransform(im_geotrans);
		// d2.SetProjection(im_proj);
		for (int i = 0; i < ySzie; i++) {
			for (int j = 1; j <= nBandCount; j++) {
				Band band = dataset.GetRasterBand(j);
				float[] cache = new float[xSize];
				band.ReadRaster(0, i, xSize, 1, cache);
				Band newBand = d2.GetRasterBand(j);
				newBand.WriteRaster(0, i, xSize, 1, cache);
				newBand.FlushCache();
				Double[] ff = new Double[1];
				int[] buckets = new int[xSize];
				band.GetHistogram(buckets);
				System.out.println(ff.toString());
			}
		}
		dataset.delete();
		d2.delete();
		gdal.GDALDestroyDriverManager();
		return null;
	}
}
