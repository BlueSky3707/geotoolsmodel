package com.geotools.gistools.request;

import com.geotools.gistools.exception.ExceptionMsg;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @Author: jdy
 * @Date: 2021/11/07 10:11
 */
public class QueryTablesParameter {
    /**
     * 图层名称
     */
    protected String[] tables;
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
     * 分页信息
     */
    protected Integer current;

    protected Integer limit;

    public QueryTablesParameter() {
    }


    public QueryTablesParameter(String[] tables, String filter, String spatialFilter, String outFields,
                          Boolean isReturnGeometry, Boolean isCache,String orderByFields, Integer buffDis, Integer current, Integer limit) {
        super();
        this.tables = tables;
        this.filter = filter;
        this.spatialFilter = spatialFilter;
        this.outFields = outFields;
        this.isReturnGeometry = isReturnGeometry;
        this.orderByFields = orderByFields;
        this.buffDis = buffDis;
        this.current = current;
        this.limit = limit;

    }

   

  
    public String[] getTables() {
		return tables;
	}


	public void setTables(String[] tables) {
		this.tables = tables;
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
    

}
