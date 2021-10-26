package com.geotools.gistools.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/10/26 10:45
 */
@ApiModel
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Redis implements Serializable {

    @ApiModelProperty("键")
    private String key;

    @ApiModelProperty("保存的数据对象")
    private Object value;

    @ApiModelProperty("过期时间：单位为秒")
    private Integer seconds;
}