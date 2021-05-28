package com.geotools.gistools.utils;


import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

public class Util {
	/*静态单例模式*/
	private Util(){}
	private static class LazyHolder {
	        private static final Util INSTANCE = new Util();
	  }
	public static final Util getInstance() {
	        return LazyHolder.INSTANCE;
	}
	

	public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
	}

	public String getGeometryToStr(Map geometry){
		String str="";
		String type = geometry.get("type").toString();
		switch (type) {
		case "point":
			String x = geometry.get("x").toString();
			String y = geometry.get("y").toString();
			str="'POINT("+x+" "+y+")'";
			break;

		case "polyline":
			
			String paths = geometry.get("paths").toString();
			str="'MULTIPOLYGON(((%)))'";
			break;
		case "polygon":
			String rings = geometry.get("rings").toString();
			str="'MULTIPOLYGON((%s))'";
			
			str = str.replace("%s", this._changePolygonOrPolylineToJSonString(geometry, "rings"));
			break;
		}
		return str;
	}
	
	public String getStrToGeometry(Map geometry){
		String str="";
		String type = geometry.get("type").toString();
		switch (type) {
		case "point":
			String x = geometry.get("x").toString();
			String y = geometry.get("y").toString();
			
			break;

		case "polyline":
			
			String paths = geometry.get("paths").toString();
			
			break;
		case "polygon":
			String rings = geometry.get("rings").toString();
			
			break;
		}
		return str;
	}
	
	public String _changePolygonOrPolylineToJSonString(Map geo,String key){
		String[] arrs;
//		(JSONObject) geo;
//		for (String string : geo.get("key")) {
//			
//		}
//		array.forEach(geo[key], lang.hitch(this, function(arr, i){
//			var items = [];
//			array.forEach(arr, lang.hitch(this, function(item, k){
//				items.push(item[0] + " " + item[1]);
//			}));
//			if(items.length > 0){
//				arrs.push("("+items.join(",")+")");
//			}
//		}));
//		if(arrs.length > 0){
//			return arrs.join(",");
//		}
		return "";
    }

	
}
