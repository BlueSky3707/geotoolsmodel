package com.geotools.gistools.beans;

import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/27 17:19
 */
@Data
public class RasterInfo {
    public String RasterType;
    public String RasterName;
    public String RaterTime;
    public Double Max;
    public Double Min;
    public Double Mean;
    public Double Stddev;

}
