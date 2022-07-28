package com.geotools.gistools.utils;

import org.gdal.gdal.Dataset;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.geotools.gistools.request.ShpQueryParam;
import java.nio.charset.Charset;
import java.io.File;
import java.io.IOException;
import java.util.*;
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

}
