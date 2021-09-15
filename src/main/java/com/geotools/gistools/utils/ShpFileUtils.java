package com.geotools.gistools.utils;

import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geojson.feature.FeatureJSON;


import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.springframework.stereotype.Component;
import top.jfunc.json.impl.JSONArray;
import top.jfunc.json.impl.JSONObject;

import java.io.StringWriter;

import java.nio.charset.Charset;
import java.io.File;
import java.io.IOException;

import java.util.*;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2020/12/9 9:29
 */
@Component
public class ShpFileUtils {


    public void getFeatures() throws IOException {
        String strShpPath = "D:\\ProjectWorkSpace\\hbt2\\data\\qxczd.shp";
        File file = new File(strShpPath);

        DataStoreFactorySpi factory = new ShapefileDataStoreFactory();


        Map map = Collections.singletonMap("url", file.toURL());

        DataStore dataStore = factory.createDataStore(map);

        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source =
                dataStore.getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)"

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                System.out.print(feature.getID());
                System.out.print(": ");
                System.out.println(feature.getDefaultGeometryProperty().getValue());
            }
        }
    }

    public SimpleFeatureCollection readShp(String path) throws IOException, CQLException {
        String filePath = "D:\\ProjectWorkSpace\\hbt2\\data\\qxczd.shp";
        SimpleFeatureSource featureSource = readStoreByShp(filePath);
        Filter filter = ECQL.toFilter("AREA_NAME = '宝鸡市'");
        //filter=null;
        SimpleFeatureCollection collection = featureSource.getFeatures(filter);
        if (featureSource == null) return null;
        try {
            return filter != null ? featureSource.getFeatures(filter) : featureSource.getFeatures();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public SimpleFeatureSource readStoreByShp(String path) throws IOException {
        ShapefileDataStore shpDataStore = null;
        File file = new File(path);
        shpDataStore = new ShapefileDataStore(file.toURL());
        //设置字符编码
        ((ShapefileDataStore) shpDataStore).setCharset(Charset.forName("GBK"));
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = null;
        featureSource = shpDataStore.getFeatureSource(typeName);
        return featureSource;
    }

    public Object shape2Geojson(String shpPath) {
        FeatureJSON fjson = new FeatureJSON();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
            File file = new File(shpPath);
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();
            JSONArray array = new JSONArray();
            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                JSONObject json = new JSONObject(writer.toString());
                array.put(json);
            }
            itertor.close();
            sb.append(array.toString());
            sb.append("}");


        } catch (Exception e) {


        }
        System.out.println(sb);
        return sb;
    }
}
