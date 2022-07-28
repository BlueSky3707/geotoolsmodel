package com.geotools.gistools.utils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.stereotype.Component;
import com.geotools.gistools.mapper.RasterMappper;


/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/27 16:51
 */
@Component
public class RaterUtils {
	@Resource
	RasterMappper rasterMapper;

	/**
	 * @param rasterPath
	 *            tif文件路径
	 * @param type
	 *            类型
	 * @param filename
	 *            文件名
	 * @return
	 */
	public int culRasterInf(String rasterPath, String type, String filename) {
		int insertRater = 0;
		gdal.AllRegister();
		Dataset dataset = gdal.Open(rasterPath, gdalconstConstants.GA_ReadOnly);
		if (dataset == null) {
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
			return 0;
		}
		double[] min = new double[1];
		double[] max = new double[1];
		double[] mean = new double[1];
		double[] stddev = new double[1];
		Band band = dataset.GetRasterBand(1);
		if (band != null) {
			band.ComputeStatistics(false, min, max, mean, stddev);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rastertype", type);
			map.put("rastername", filename);
			map.put("ratertime", filename.replace(".tif", ""));
			map.put("rmax", max[0]);
			map.put("rmin", min[0]);
			map.put("mean", mean[0]);
			map.put("stddev", stddev[0]);

			insertRater = rasterMapper.insertRater(map);
		}
		return insertRater;
	}


}
