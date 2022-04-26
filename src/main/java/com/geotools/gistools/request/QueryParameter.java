package com.geotools.gistools.request;

import com.geotools.gistools.exception.ExceptionMsg;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/12 10:11
 */
public class QueryParameter extends ValidParameter {
    /**
     * 图层名称
     */
    protected String layerName;
    /**
     * 属性过滤条件
     */
    protected String filter;
    /**
     * 空间过滤条件
     */
    protected String spatialFilter;
    /**
     * 返回的字段名
     */
    protected String outFields;
    /**
     * 是否返回空间信息
     */
    protected boolean isReturnGeometry = true;
    /**
     * 排序规则 例如：ORDER BY ID DESC,NAME
     */
    protected String orderByFields;
    /**
     * 空间位置关系
     */
    protected String spatialRel;
    /**
     * 缓冲距离
     */
    protected Integer buffDis;
    /**
     * 选择要缓冲的表的条件过滤
     */
    protected String selectTableFilter;
    /**
     * 选择要缓冲的表
     */
    protected String selectTable;
    /**
     * 选择要缓冲的表简化容差
     */
    protected float tolerance;
    /**
     * 是否查询缓存结果
     */
    protected boolean isCache=false;

    /**
     * 分页信息
     */
    protected Integer current;

    protected Integer limit;

    public QueryParameter() {
    }

    public QueryParameter(String layerName, String filter, String spatialFilter, String outFields, Boolean isReturnGeometry,Boolean isCache, String orderByFields, String spatialRel,Integer current, Integer limit) {
        this.layerName = layerName;
        this.filter = filter;
        this.spatialFilter = spatialFilter;
        this.outFields = outFields;
        this.isReturnGeometry = isReturnGeometry;
        this.orderByFields = orderByFields;
        this.spatialRel = spatialRel;
        this.current = current;
        this.limit = limit;
        this.isCache=isCache;

    }

    public QueryParameter(String layerName, String filter, String spatialFilter, String outFields,
                          Boolean isReturnGeometry, Boolean isCache,String orderByFields, Integer buffDis, Integer current, Integer limit) {
        super();
        this.layerName = layerName;
        this.filter = filter;
        this.spatialFilter = spatialFilter;
        this.outFields = outFields;
        this.isReturnGeometry = isReturnGeometry;
        this.orderByFields = orderByFields;
        this.buffDis = buffDis;
        this.isCache=isCache;
        this.current = current;
        this.limit = limit;

    }
    public QueryParameter(String layerName, String filter, String spatialFilter, String outFields,
    		Boolean isReturnGeometry, Boolean isCache,String orderByFields, Integer buffDis, Integer current, Integer limit,
    		String selectTable,String selectTableFilter,float tolerance) {
    	super();
    	this.layerName = layerName;
    	this.filter = filter;
    	this.spatialFilter = spatialFilter;
    	this.outFields = outFields;
    	this.isReturnGeometry = isReturnGeometry;
    	this.orderByFields = orderByFields;
    	this.buffDis = buffDis;
    	this.isCache=isCache;
    	this.current = current;
    	this.limit = limit;
    	this.selectTable = selectTable;
    	this.selectTableFilter = selectTableFilter;
    	this.tolerance = tolerance;
    	
    }

    @Override
    public boolean check() throws ExceptionMsg {
        ValidParameter.isBlank(this.layerName, "'layerName'参数不能为空!");
        return true;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSpatialFilter() {
        return spatialFilter;
    }

    public void setSpatialFilter(String spatialFilter) {
        this.spatialFilter = spatialFilter;
    }

    public String getOutFields() {
        return outFields;
    }

    public void setOutFields(String outFields) {
        this.outFields = outFields;
    }

    public boolean isReturnGeometry() {
        return isReturnGeometry;
    }
    public boolean isCache(){return isCache;}
    public void  setCache(boolean iscache){isCache=iscache;}

    public void setReturnGeometry(boolean returnGeometry) {
        isReturnGeometry = returnGeometry;
    }

    public String getOrderByFields() {
        return orderByFields;
    }

    public void setOrderByFields(String orderByFields) {
        this.orderByFields = orderByFields;
    }

    public String getSpatialRel() {
        return spatialRel;
    }

    public void setSpatialRel(String spatialRel) {
        this.spatialRel = spatialRel;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getBuffDis() {
        return buffDis;
    }

    public void setBuffDis(Integer buffDis) {
        this.buffDis = buffDis;
    }

	public String getSelectTableFilter() {
		return selectTableFilter;
	}

	public void setSelectTableFilter(String selectTableFilter) {
		this.selectTableFilter = selectTableFilter;
	}

	public String getSelectTable() {
		return selectTable;
	}

	public void setSelectTable(String selectTable) {
		this.selectTable = selectTable;
	}

	public float getTolerance() {
		return tolerance;
	}

	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

}
