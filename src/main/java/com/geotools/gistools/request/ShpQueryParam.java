package com.geotools.gistools.request;

import com.geotools.gistools.exception.ExceptionMsg;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能描述：
 *
 * @Author: jdy
 * @Date: 2021/5/17 15:11
 */
public class ShpQueryParam extends ValidParameter {
    /**
     * 图层名称
     */
    protected String fileName;
    /**
     * 城市图层名称
     */
    protected String cityFileName;
    /**
     * 城市名
     */
    protected String selCity;
  
    /**
     * 属性过滤条件
     */
    protected String filter;
    protected  String spatialFilter;
    protected String spatialRel;


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCityFileName() {
		return cityFileName;
	}

	public void setCityFileName(String cityFileName) {
		this.cityFileName = cityFileName;
	}

	public String getSelCity() {
		return selCity;
	}

	public void setSelCity(String selCity) {
		this.selCity = selCity;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public boolean check() throws ExceptionMsg {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSpatialFilter() {
		return spatialFilter;
	}

	public void setSpatialFilter(String spatialFilter) {
		this.spatialFilter = spatialFilter;
	}

	public String getSpatialRel() {
		return spatialRel;
	}

	public void setSpatialRel(String spatialRel) {
		this.spatialRel = spatialRel;
	}

	public ShpQueryParam(String fileName, String cityFileName, String selCity, String filter, String spatialFilter,
			String spatialRel) {
		super();
		this.fileName = fileName;
		this.cityFileName = cityFileName;
		this.selCity = selCity;
		this.filter = filter;
		this.spatialFilter = spatialFilter;
		this.spatialRel = spatialRel;
	}







}
