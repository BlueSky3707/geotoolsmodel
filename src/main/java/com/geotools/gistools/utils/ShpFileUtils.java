package com.geotools.gistools.utils;

import org.gdal.gdal.Dataset;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.geotools.gistools.request.ShpParam;
import com.geotools.gistools.request.ShpQueryParam;

import java.nio.charset.Charset;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2020/12/9 9:29
 */
@Component
public class ShpFileUtils {
    @Value("${shp.code}")
    String code ;
  
    public void setFilters(ShpQueryParam shpQueryParam, List<Filter> filters) {
        try {
            // 属性过滤
            if (shpQueryParam.getFilter() != null) {
                Filter filter = ECQL.toFilter(shpQueryParam.getFilter());
                filters.add(filter);
            }
            // 空间过滤
            // 地市过滤
            if (shpQueryParam.getCityFileName() != null && shpQueryParam.getSelCity() != null) {
                List<Filter> filterscity = new ArrayList<>();
                Filter filtercity = ECQL.toFilter(shpQueryParam.getSelCity());
                filterscity.add(filtercity);
                SimpleFeatureCollection resultcity = readStoreByShp(shpQueryParam.getCityFileName(), filterscity);

                SimpleFeatureIterator cityIterator = resultcity.features();
                String wkt = null;
                while (cityIterator.hasNext()) {
                    SimpleFeature next = cityIterator.next();
                    wkt = next.getDefaultGeometryProperty().getValue().toString();
                }
                if(wkt!=null&&!wkt.equals("")) {
                    Filter filter = ECQL.toFilter("INTERSECTS(the_geom," + wkt + ")");
                    filters.add(filter);
                }

                //SpatialRel wkt过滤
            } else if (shpQueryParam.getSpatialFilter() != null && shpQueryParam.getSpatialRel() != null) {
                Filter gfilter = null;
                if (shpQueryParam.getSpatialRel().equals("INTERSECTS")) {
                    gfilter = ECQL.toFilter("INTERSECTS(the_geom," + shpQueryParam.getSpatialFilter() + ")");
                } else if (shpQueryParam.getSpatialRel().equals("CONTAINS")) {
                    gfilter = ECQL.toFilter("CONTAINS(the_geom," + shpQueryParam.getSpatialFilter() + ")");
                } else if (shpQueryParam.getSpatialRel().equals("DISJOINT")) {
                    gfilter = ECQL.toFilter("DISJOINT(the_geom," + shpQueryParam.getSpatialFilter() + ")");
                } else if (shpQueryParam.getSpatialRel().equals("CROSSES")) {
                    gfilter = ECQL.toFilter("CROSSES(the_geom," + shpQueryParam.getSpatialFilter() + ")");
                }
                filters.add(gfilter);

            }
        } catch (CQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SimpleFeatureCollection readStoreByShp(String path, List<Filter> filters) throws IOException {
        ShapefileDataStore shpDataStore = null;
        File file = new File(path);
        shpDataStore = new ShapefileDataStore(file.toURL());
        // 设置编码
        if(code.equals("GBK")) {
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
        }else {
            Charset charset = Charset.forName("UTF-8");
            shpDataStore.setCharset(charset);
        }
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = null;
        featureSource = shpDataStore.getFeatureSource(typeName);
        Query query = new Query();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        query.setFilter(ff.and(filters));
        SimpleFeatureCollection result = featureSource.getFeatures(query);
        shpDataStore.dispose();// 使用之后必须关掉
        return result;
    }

    public List<Map<String, Object>> shape2Geojson(ShpQueryParam shpQueryParam) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (shpQueryParam.getFileName() != null && !shpQueryParam.getFileName().equals("")) {

                List<Filter> filters = new ArrayList<>();
//				设置过滤条件
                setFilters(shpQueryParam, filters);
                SimpleFeatureCollection result = readStoreByShp(shpQueryParam.getFileName(), filters);
                SimpleFeatureIterator itertor = result.features();
                while (itertor.hasNext()) {
                    SimpleFeature feature = itertor.next();

                    Collection<Property> properties = feature.getProperties();
                    Object defaultGeometry = feature.getDefaultGeometry();
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (defaultGeometry != null) {
                        map.put("geometry", feature.getDefaultGeometryProperty().getValue().toString());
                    } else {
                        map.put("geometry", "");
                    }
                    for (Property property : properties) {
                        String name = property.getName().toString();
                        if (name != null && !name.equals("the_geom")) {
                            map.put(property.getName().toString().toLowerCase(), property.getValue());
                        }
                    }

                    list.add(map);
                }
                itertor.close();
            }
        } catch (Exception e) {

            return list;
        }

        return list;
    }
    
    /**
	 * 向shp文件添加数据
	 * 
	 * @param path 文件路径
	 * @param datalist 空间及属性数据
	 */
	public static void addFeature(String path, List<Map<String, Object>> datalist,String code) {

		ShapefileDataStore dataStore = null;
		File file = new File(path);
		try {
			dataStore = new ShapefileDataStore(file.toURL());
			Charset charset = Charset.forName(code);
			dataStore.setCharset(charset);
			String typeName = dataStore.getTypeNames()[0];

			SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
			SimpleFeatureType featureType = store.getSchema();
			// 获取字段列表
			List<String> fileds = getFileds(featureType);
			// System.out.println(fileds.toString());
			// 创建Features

			List<SimpleFeature> list = new ArrayList<SimpleFeature>();
			WKTReader reader = new WKTReader();
			for (Map<String, Object> data : datalist) {
				SimpleFeatureBuilder build = new SimpleFeatureBuilder(featureType);
				if (data.get("geom") != null) {
					Geometry geometry = reader.read(data.get("geom").toString());
					build.add(geometry);
				}
				for (String filed : fileds) {
					Object object = data.get(filed);
					build.add(object);
				}
				SimpleFeature feature = build.buildFeature(null);
				list.add(feature);
			}

			SimpleFeatureCollection collection = new ListFeatureCollection(featureType, list);

			Transaction transaction = new DefaultTransaction("Adding");

			store.setTransaction(transaction);

			try {
				store.addFeatures(collection);
				transaction.commit();
				System.out.println("============addFeature=====done=====");

			} catch (Exception eek) {
				eek.printStackTrace();
				transaction.rollback();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	/**
	 * 更新shp文件数据
	 * 
	 * @param path 文件路径
	 * @param datalist 空间及属性数据
	 */
	public static void updateFeature(String path, List<Map<String, Object>> datalist,String code) {
		ShapefileDataStore dataStore = null;
		File file = new File(path);
		Transaction transaction = new DefaultTransaction("handle");
		try {
			dataStore = new ShapefileDataStore(file.toURL());
			Charset charset = Charset.forName(code);
			dataStore.setCharset(charset);
			String typeName = dataStore.getTypeNames()[0];
			SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);

			// 获取字段列表
			SimpleFeatureType featureType = store.getSchema();
			List<String> fileds = getFileds(featureType);
			store.setTransaction(transaction);
			WKTReader reader = new WKTReader();
			for (Map<String, Object> data : datalist) {
				
				Filter filter = null;
				if (data.get("where") != null) {
					filter = ECQL.toFilter(data.get("where").toString());
				}

				Object[] objs = new Object[] {};
				String[] str = new String[] {};
				if (data.get("geom") != null) {
					Geometry geometry = reader.read(data.get("geom").toString());
					str = add(str, "the_geom");
					objs = add(objs, geometry);

				}
				for (String stri : fileds) {
					if (data.get(stri) != null) {

						str = add(str, stri);
						objs = add(objs, data.get(stri));
					}
				}
				store.modifyFeatures(str, objs, filter);
			}

			transaction.commit();
			System.out.println("========updateFeature====end====");
		} catch (Exception eek) {
			eek.printStackTrace();
			try {
				transaction.rollback();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	 
	/**
	 * 移除shp中的数据
	 * @param path 文件路径
	 * @param ids 字段值数组
	 * @param filed 字段名
	 */
	public static void removeFeature(String path, List<String>ids,String filed,String code){
		ShapefileDataStore dataStore = null;
		File file = new File(path);
		Transaction transaction = new DefaultTransaction("handle");
		try {
			dataStore = new ShapefileDataStore(file.toURL());
			Charset charset = Charset.forName(code);
			dataStore.setCharset(charset);
			String typeName = dataStore.getTypeNames()[0];
			SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);    
			store.setTransaction(transaction);      
			    
		    Filter filter = null;
		    if(ids.size()>0) {
		    	String join = filed +" in ("+StringUtils.join(ids,",")+")";
		    	System.out.println(join);
		    	filter = ECQL.toFilter(join);
		    }
		    if(filter!=null) {
		    	
		    	store.removeFeatures(filter);
		    	transaction.commit();            
		    	System.out.println("======removeFeature== done ========");
		    }
		 
		} catch (Exception eek) {
			eek.printStackTrace();
			try {
				transaction.rollback();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
    
	}
	/**
	 * 获取字段列表
	 * @param featureType
	 * @return
	 */
	private static List<String> getFileds(SimpleFeatureType featureType) {
		List<String> fileds = new ArrayList<String>();
		String str = featureType.toString();
		String[] splits = str.substring(str.indexOf("Feature(") + 8, str.length() - 1).split(",");
		for (String split : splits) {
			String[] split2 = split.split(":");
			if (split2[0] != null && !split2[0].equals("the_geom")) {
				fileds.add(split2[0]);
			}

		}
		return fileds;
	}
	/**
	 * Object数组新增
	 * @param arr
	 * @param elements
	 * @return
	 */
	public static Object[] add(Object[] arr, Object... elements) {
		Object[] tempArr = new Object[arr.length + elements.length];
		System.arraycopy(arr, 0, tempArr, 0, arr.length);

		for (int i = 0; i < elements.length; i++)
			tempArr[arr.length + i] = elements[i];
		return tempArr;

	}

	/**
	 * String数组新增
	 * @param arr
	 * @param elements
	 * @return
	 */
	public static String[] add(String[] arr, String... elements) {
		String[] tempArr = new String[arr.length + elements.length];
		System.arraycopy(arr, 0, tempArr, 0, arr.length);

		for (int i = 0; i < elements.length; i++)
			tempArr[arr.length + i] = elements[i];
		return tempArr;

	}
	public  void setShp(ShpParam param,String type) {
		
		if(type.equals("insert")) {
			addFeature(param.getFilename(), param.getList(),param.getCode());
		}else if(type.equals("update")) {
			updateFeature(param.getFilename(), param.getList(),param.getCode());
		}else if(type.equals("delete")) {
			removeFeature(param.getFilename(), param.getIds(), param.getFiled(),param.getCode());
		}
	}

}
