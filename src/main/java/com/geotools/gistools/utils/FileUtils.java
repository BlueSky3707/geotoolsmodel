package com.geotools.gistools.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.geotools.gistools.mapper.RasterMapper;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/28 9:51
 */
@Component
public class FileUtils {
	@Resource
	RasterMapper rasterMapper;
	@Resource
	RaterUtils raterUtils;
	@Value("${ratser.CO}")
	String rasterCO;
	@Value("${ratser.HCHO}")
	String rasterHCHO;
	@Value("${ratser.NO2}")
	String rasterNO2;
	@Value("${ratser.O3}")
	String rasterO3;
	@Value("${ratser.PM25}")
	String rasterPM25;
	@Value("${ratser.CHOCHO}")
	String rasterCHOCHO;
	@Value("${ratser.SO2}")
	String rasterSO2;

	public Map<String, String> configmap = null;

	/**
	 * 定时器
	 */
//	@Scheduled(fixedRate = 60000 * 5, initialDelay = 60000)
	public void startScheduled() {
		if (configmap == null) {
			configmap = new HashMap<String, String>();
			configmap.put("CO", rasterCO);
			configmap.put("HCHO", rasterHCHO);
			configmap.put("NO2", rasterNO2);
			configmap.put("O3", rasterO3);
			configmap.put("PM25", rasterPM25);
			configmap.put("CHOCHO", rasterCHOCHO);
			configmap.put("SO2", rasterSO2);
		}
		Set<String> keySet = configmap.keySet();
		for (String type : keySet) {
			String fileurl = configmap.get(type).toString();
			File file = new File(fileurl);
			List<String> files = getFiles(file, type, ".tif");
			for (String filename : files) {
				raterUtils.culRasterInf(fileurl + "\\" + filename, type, filename);
			}
		}

	}

	/**
	 * 获取文件夹下所有的文件 , ext参数可选
	 * 
	 * @param file 文件夹路径
	 * @param file 类型
	 * @param ext 文件格式 , 可变参数 , 不写将获取文件夹下所有文件
	 * @return 返回符合要求文件的File数组
	 */
	public List<String> getFiles(File file, String type, String... ext) {
//		请求获取最大值
		String maxtime = rasterMapper.getMaxtime("ratertime", type);
		List<String> list = new ArrayList<String>();
		// lambda表达式 , FilenameFilter
		// 将当前目录下的所有文件和文件夹都添加到files中
		File[] files = file.listFiles((dir, name) -> {
			// 如果文件夹返回true
			if (new File(dir, name).isDirectory()) {
				return true;
			}
			// 如果指定了文件类型 , 进行文件筛选
			if (ext != null && ext.length > 0) {
				for (String s : ext) {
					// 如果文件符合规则 , 返回true
					if (name.toLowerCase().endsWith(s.toLowerCase())) {
						return true;
					}
				}
			} else {
				// 没有规定文件类型 , 所有文件都返回true
				return true;
			}
			return false;
		});
		// 如果files不为null , 遍历files . 如果files为null , 遍历空数组.
		for (File f : files != null ? files : new File[0]) {
			// 如果f不是文件夹 , 添加到指定list中
			if (!f.isDirectory()) {
				if (f.getName().indexOf("_") == -1) {
					;
					double time = Double.parseDouble(f.getName().replace(".tif", ""));
					if (maxtime != null) {
						if (time > Double.parseDouble(maxtime)) {
							list.add(f.getName());
						}
					} else {
						list.add(f.getName());
					}
				}

				continue;
			}
			// f 是文件夹 递归
			// 如果对文件格式有要求 ,直接把方法中的ext参数填进去
			// 如果没有要求,写不写都行
			getFiles(f, type, ext);
		}

		return list;
	}

public static void main(String[] args) {
	Timer timer = new Timer();
	System.out.println(timer.purge());
    TimerTask task = new TimerTask() {
        public void run()
        {
        	System.out.println(timer.purge());
            
        }
    };
  
timer.schedule( task, 0,2000 );
System.out.println(timer.purge());
}
}
