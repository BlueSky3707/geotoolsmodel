package com.geotools.gistools.request;

import com.geotools.gistools.beans.InterpolData;

import java.util.List;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/9/27 11:00
 */
public class InterpolParam {
    //插值数据
    protected List<InterpolData> InterpolDatas;
    //数据间隔
    protected double[] dataInterVal;
    //创建格网的大小
    protected Integer size ;
    //裁剪区域名称
    protected String clipCity;
    //是否裁剪
    protected Boolean isClip;
    //生层类型(1代表等值线，0 代表等值面)
    private  Integer geoType;
    public InterpolParam() {
        super();
    }

    public List<InterpolData> getInterpolDatas() {
        return this.InterpolDatas;
    }
    public double[] getDataInterVal() {
        return this.dataInterVal;
    }

    public void setDataInterVal(double[] dataInterVal) {
        this.dataInterVal = dataInterVal;
    }

    public void setInterpolDatas(List<InterpolData> interpolDatas) {
        this.InterpolDatas = interpolDatas;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getClipCity() {
        return this.clipCity;
    }

    public void setClipCity(String clipCity) {
        this.clipCity = clipCity;
    }

    public Boolean getClip() {
        return this.isClip;
    }

    public void setClip(Boolean clip) {
        isClip = clip;
    }

    public Integer getGeoType() {
        return this.geoType;
    }

    public void setGeoType(Integer geoType) {
        this.geoType = geoType;
    }
}
