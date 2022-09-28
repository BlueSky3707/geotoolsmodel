package com.geotools.gistools.utils;


import com.geotools.gistools.beans.InterpolData;
import com.geotools.gistools.request.InterpolParam;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.ClippedFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQLException;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import org.springframework.util.StringUtils;
import wContour.Contour;
import wContour.Global.Border;
import wContour.Global.PointD;
import wContour.Global.PolyLine;
import wContour.Global.Polygon;
import wContour.Interpolate;
import org.geotools.feature.collection.ClippedFeatureCollection;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/9/27 11:23
 */
@Component
public class GeoAnlysisUtils {
    private static final Logger logger = LoggerFactory.getLogger(GeoAnlysisUtils.class);
    private static GeometryFactory geometryFactory = new GeometryFactory();
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    @Value("${clipshp.filePath}")
    private String filePath;
    @Value("${clipshp.encode}")
    private String encode;

    public List<Map<String, Object>> getFeaturesInf(InterpolParam inParam) throws SchemaException, ParseException, CQLException, IOException {
        FeatureCollection features = createequiSurface(inParam);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

       // ClippedFeatureIterator iterator = (ClippedFeatureIterator) features.features();
        FeatureIterator iterator= features.features();
        while (iterator.hasNext()) {
            Feature pfes = iterator.next();
            Collection<Property> properties = pfes.getProperties();
            Object defaultGeometry = pfes.getDefaultGeometryProperty();
            Map<String, Object> map = new HashMap<String, Object>();
            if (defaultGeometry != null) {
                map.put("geometry", pfes.getDefaultGeometryProperty().getValue().toString());
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
        iterator.close();
        return list;

    }

    /**
     * @描述
     * @创建人 ddw
     * @创建时间 2022/9/27
     * 生成等值线或等值面
     */
    public FeatureCollection createequiSurface(InterpolParam inParam) throws IOException, CQLException, SchemaException, ParseException {
        SimpleFeatureCollection fc = createFeatureCl(inParam.getClipCity());
        Object geom = fc.features().next().getAttribute("the_geom");
        WKTReader2 wktReader = new WKTReader2();
        Geometry oldGeom = wktReader.read(geom.toString());
        FeatureCollection pfeatureCol = null;
        double _undefData = -9999.0;
        List<PolyLine> cPolylineList;
        List<Polygon> cPolygonList;
        int width = inParam.getSize(), height = inParam.getSize();
        double[] _X = new double[width];
        double[] _Y = new double[height];
        double minX = fc.getBounds().getMinX();
        double minY = fc.getBounds().getMinY();
        double maxX = fc.getBounds().getMaxX();
        double maxY = fc.getBounds().getMaxY();
        Interpolate.CreateGridXY_Num(minX, minY, maxX, maxY, _X, _Y);
        double[] interp = {62, 65, 72, 75, 78, 81, 85, 90, 96, 100, 105, 110.9};
        inParam.setDataInterVal(interp);
        double[][] _gridData;
        int nc = inParam.getDataInterVal().length;
        double[] dataInterval = inParam.getDataInterVal();
        //获取插值数据
        inParam.setInterpolDatas(createInterpolData());

        double[][] trainData = trainData(inParam.getInterpolDatas());
        // IDW插值格网点
        _gridData = Interpolate.Interpolation_IDW_Neighbor(trainData,
                _X, _Y, 12, _undefData);
        int[][] S1 = new int[_gridData.length][_gridData[0].length];
        // 获取轮廓
        List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y,
                S1, _undefData);
        // 生成等值线
        cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc,
                dataInterval, _undefData, _borders, S1);
        // 平滑
        cPolylineList = Contour.smoothLines(cPolylineList);
        if (inParam.getGeoType() != 1) {
            cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList,
                    _borders, dataInterval);
            pfeatureCol = getFeatureCollection(cPolygonList);
        } else {
            pfeatureCol = getFeaturesBycPolyline(cPolylineList);
        }
        if (inParam.getClip()) {
            //pfeatureCol = clipFeatureCollection(fc, (SimpleFeatureCollection) pfeatureCol);
            //pfeatureCol = new ClippedFeatureCollection((SimpleFeatureCollection) pfeatureCol, oldGeom, true);
        }

        return pfeatureCol;
    }

    //创建测试数据
    public List<InterpolData> createInterpolData() {
        List<InterpolData> listData = new ArrayList<>();

        double lon = 105.99;
        double lat = 32.148;
        double pValue = 60;
        for (int i = 0; i < 50; i++) {
            InterpolData interp = new InterpolData();
            lon += Math.random();
            lat += Math.random();
            pValue += Math.random() * 50;
            interp.setLon(lon);
            interp.setLat(lat);
            interp.setPValue(pValue);
            listData.add(interp);
        }
        return listData;
    }

    /**
     * 转换插值数据类型
     */
    public double[][] trainData(List<InterpolData> inData) {
        double[][] douData = new double[inData.size()][3];
        for (int i = 0; i < inData.size(); i++) {
            douData[i][0] = inData.get(i).getLon();
            douData[i][1] = inData.get(i).getLat();
            douData[i][2] = inData.get(i).getPValue();
        }
        return douData;
    }

    /**
     * 将等值线转换成FeatureCollection
     */
    public static FeatureCollection getFeaturesBycPolyline(List<PolyLine> cPolylineList) throws SchemaException {
        if (cPolylineList == null || cPolylineList.size() == 0) {
            return null;
        }
        FeatureCollection cs = null;
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        LineString line;
        for (PolyLine pPolyline : cPolylineList) {
            Coordinate[] coordinates = new Coordinate[pPolyline.PointList.size()];
            for (int i = 0, len = pPolyline.PointList.size(); i < len; i++) {
                PointD ptd = pPolyline.PointList.get(i);
                coordinates[i] = new Coordinate(ptd.X, ptd.Y);
            }

            line = geometryFactory.createLineString(coordinates);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("the_geom", line);
            map.put("value", pPolyline.Value);
            values.add(map);
        }
        cs = creatFeatureCollection(
                "polygons",
                "the_geom:LineString,value:double",
                values);
        return cs;
    }

    /**
     * 将生成的等值面转换成FeatureCollection
     */
    public static FeatureCollection getFeatureCollection(List<Polygon> cPolygonList) {

        if (cPolygonList == null || cPolygonList.size() == 0) {
            return null;
        }
        FeatureCollection cs = null;
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        try {
            for (Polygon pPolygon : cPolygonList) {
                //外圈
                LinearRing mainRing;
                Coordinate[] coordinates = new Coordinate[pPolygon.OutLine.PointList.size()];
                for (int i = 0, len = pPolygon.OutLine.PointList.size(); i < len; i++) {
                    PointD ptd = pPolygon.OutLine.PointList.get(i);
                    coordinates[i] = new Coordinate(ptd.X, ptd.Y);
                }
                mainRing = geometryFactory.createLinearRing(coordinates);
                //孔洞
                LinearRing[] holeRing = new LinearRing[pPolygon.HoleLines.size()];
                for (int i = 0; i < pPolygon.HoleLines.size(); i++) {
                    PolyLine hole = pPolygon.HoleLines.get(i);
                    Coordinate[] coordinates_h = new Coordinate[hole.PointList.size()];
                    for (int j = 0, len = hole.PointList.size(); j < len; j++) {
                        PointD ptd = hole.PointList.get(j);
                        coordinates_h[j] = new Coordinate(ptd.X, ptd.Y);
                    }
                    holeRing[i] = geometryFactory.createLinearRing(coordinates_h);
                }
                org.locationtech.jts.geom.Polygon geo = geometryFactory.createPolygon(mainRing, holeRing);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("the_geom", geo);
                map.put("value", pPolygon.LowValue);
                values.add(map);
            }

            cs = creatFeatureCollection(
                    "polygons",
                    "the_geom:Polygon,value:double",
                    values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cs;
    }

    /**
     * @描述
     * @创建人 ddw
     * @创建时间 2022/9/27
     * 构建特定的FeatureCollection
     */
    public static FeatureCollection creatFeatureCollection(String typeName, String typeSpec, List<Map<String, Object>> values) throws SchemaException, SchemaException {
        SimpleFeatureType type = DataUtilities.createType(typeName, typeSpec);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        for (Map feat : values) {
            featureBuilder.reset();
            featureBuilder.add(feat.get("the_geom"));
            System.out.println(feat.get("value"));
            featureBuilder.add(feat.get("value"));
            SimpleFeature feature = featureBuilder.buildFeature(null);
            collection.add(feature);
        }
        return collection;
    }

    /**
     * @描述
     * @创建人 ddw
     * @创建时间 2022/9/27
     * 两要素进行裁剪
     */
    public FeatureCollection clipFeatureCollection(FeatureCollection fc, SimpleFeatureCollection gs) {

        FeatureCollection cs = null;
        try {
            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            FeatureIterator contourFeatureIterator = gs.features();
            FeatureIterator dataFeatureIterator = fc.features();
            while (dataFeatureIterator.hasNext()) {
                Feature dataFeature = dataFeatureIterator.next();
                Object dataGeometry = dataFeature.getProperty(
                        "the_geom").getValue();
                if (dataGeometry instanceof MultiPolygon) {
                    MultiPolygon p = (MultiPolygon) dataGeometry;
                    while (contourFeatureIterator.hasNext()) {
                        Feature contourFeature = contourFeatureIterator.next();
                        Geometry contourGeometry = (Geometry) contourFeature
                                .getProperty("the_geom").getValue();
                        double v = (Double) contourFeature.getProperty("value")
                                .getValue();
                        if (p.intersects(contourGeometry)) {
                            Geometry geo = p.intersection(contourGeometry);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("the_geom", geo);
                            map.put("value", v);
                            values.add(map);
                        }
                    }
                } else {
                    Geometry p = (Geometry) dataGeometry;
                    while (contourFeatureIterator.hasNext()) {
                        Feature contourFeature = contourFeatureIterator.next();
                        Geometry contourGeometry = (Geometry) contourFeature
                                .getProperty("the_geom").getValue();
                        double v = (Double) contourFeature.getProperty("value")
                                .getValue();
                        if (p.intersects(contourGeometry)) {
                            Geometry geo = p.intersection(contourGeometry);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("the_geom", geo);
                            map.put("value", v);
                            values.add(map);
                        }
                    }
                }
            }
            contourFeatureIterator.close();
            dataFeatureIterator.close();
            cs = creatFeatureCollection(
                    "MultiPolygons",
                    "geom:MultiPolygon:srid=4326,value:double",
                    values);

        } catch (SchemaException e) {
            e.printStackTrace();
        }
        return cs;
    }

    /**
     * 根据条件获取裁剪数据的要素
     */
    public SimpleFeatureCollection createFeatureCl(String stWhere) throws IOException, CQLException {
        ShapefileDataStore store = new ShapefileDataStore(new File(filePath)
                .toURI().toURL());
        store.setCharset(Charset.forName(encode));
        FeatureSource<SimpleFeatureType, SimpleFeature> features = store
                .getFeatureSource(store.getTypeNames()[0]);
        if (!StringUtils.isEmpty(stWhere)) {
            Filter filter = ff.equals(ff.property("NAME"), ff.literal(stWhere));
            return (SimpleFeatureCollection) features.getFeatures(filter);
        }
        return (SimpleFeatureCollection) features.getFeatures();
    }
}
