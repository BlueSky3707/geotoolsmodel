/**
 * 
 */
package com.geotools.gistools.request;

import java.util.List;
import java.util.Map;

/**
 * @author jdy
 *
 * @date 2022-07-27
 */

public class ShpParam {

	/**
	 * 文件名
	 */
	private String filename;

	/**
	 * 新增修改数据
	 */
	private List<Map<String, Object>> list;

	/**
	 * 批量移除
	 */
	private List<String> ids;

	/**
	 * 移除过滤字段
	 */
	private String filed;
	/**
	 * 文件编码
	 */
	private String code="GBK";
	

	public ShpParam() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public String getFiled() {
		return filed;
	}

	public void setFiled(String filed) {
		this.filed = filed;
	}
}
