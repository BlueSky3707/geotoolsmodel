package com.geotools.gistools.request;

import com.geotools.gistools.exception.ExceptionMsg;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/5/27 11:19
 */

public class TileParam extends  ValidParameter{
    public String layerName;
    public Double centerX;
    public Double centerY;
    public Integer zoom;

    public Double getCenterX() {
        return centerX;
    }

    public Double getCenterY() {
        return centerY;
    }

    public Integer getZoom() {
        return zoom;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setCenterX(Double centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(Double centerY) {
        this.centerY = centerY;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    @Override
    public boolean check() throws ExceptionMsg {
        return false;
    }

}
