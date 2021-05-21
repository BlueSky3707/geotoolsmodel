package com.geotools.gistools.request;

import com.geotools.gistools.exception.ExceptionMsg;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;
import java.util.Map;

/**
 * 功能描述：
 *
 * @Author: jdy
 * @Date: 2021/5/17 15:11
 */
public class QueryParam extends  ValidParameter {
    /**
     * 图层名称
     */
    protected String layerName;
    /**
     * 属性过滤条件
     */
    protected String where;
    /**
     * 空间过滤条件
     */
    protected Map geometry;
    /**
     * 返回的字段名
     */
    protected String outFields;
    /**
     * 是否返回空间信息
     */
    protected boolean isReturnGeometry=true;
    /**
     * 排序规则 例如：ORDER BY ID DESC,NAME
     */
    protected String orderByFields;
    /**
     * 空间位置关系
     */
    protected SpatialRel spatialRel = SpatialRel.INTERSECTS;
    /**
     * 空间位置wkt
     */
    protected String geomwkt;
    /**
     * 分页信息
     */
    protected Integer current ;

    protected Integer limit ;
    public QueryParam(){}

    public QueryParam(String layerName, String where, Map geometry, String outFields, Boolean isReturnGeometry, String orderByFields, SpatialRel spatialRel, Integer current, Integer limit) {
        this.layerName = layerName;
        this.where = where;
        this.geometry = geometry;
        this.outFields = outFields;
        this.isReturnGeometry = isReturnGeometry;
        this.orderByFields = orderByFields;
        this.spatialRel = spatialRel;
        this.current = current;
        this.limit = limit;
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

    public SpatialRel getSpatialRel() {
        return spatialRel;
    }

    public void setSpatialRel(SpatialRel spatialRel) {
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

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public Map getGeometry() {
		return geometry;
	}

	public void setGeometry(Map geometry) {
		this.geometry = geometry;
	}

	public String getGeomwkt() {
		return geomwkt;
	}

	public void setGeomwkt(String geomwkt) {
		this.geomwkt = geomwkt;
	}
    
}
