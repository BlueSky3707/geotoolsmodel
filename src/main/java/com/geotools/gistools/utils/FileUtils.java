package com.geotools.gistools.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2022/7/28 9:51
 */

public class FileUtils {
    //存放符合规定的文件
    private static final List<File> fileArrayList = new ArrayList<>();
    /**
     * 获取文件夹下所有的文件 , ext参数可选
     * @param file 文件夹路径
     * @param ext 文件格式 , 可变参数 , 不写将获取文件夹下所有文件
     * @return 返回符合要求文件的File数组
     */
    public static File[] getFiles(File file , String... ext){
        //lambda表达式 , FilenameFilter
        //将当前目录下的所有文件和文件夹都添加到files中
        File[] files =file.listFiles((dir, name) -> {
            //如果文件夹返回true
            if(new File(dir, name).isDirectory()) {
                return true;
            }
            //如果指定了文件类型 , 进行文件筛选
            if (ext!=null&&ext.length>0) {
                for (String s : ext) {
                    //如果文件符合规则 , 返回true
                    if (name.toLowerCase().endsWith(s.toLowerCase())) {
                        return true;
                    }
                }
            }else {
                //没有规定文件类型 , 所有文件都返回true
                return true;
            }
            return false;
        });
        //如果files不为null , 遍历files . 如果files为null , 遍历空数组.
        for (File f : files!=null? files : new File[0]) {
            //如果f不是文件夹 , 添加到指定list中
            if (!f.isDirectory()) {
                fileArrayList.add(f);
                continue;
            }
            // f 是文件夹 递归
            // 如果对文件格式有要求 ,直接把方法中的ext参数填进去
            // 如果没有要求,写不写都行
            getFiles(f,ext);
        }
        //将list转换成File数组并进行返回
        return fileArrayList.toArray(new File[0]);
    }
}
