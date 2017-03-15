package com.farbox.androidbyeleven.Utils;

/**
 * describe: 各种常用转换的集合
 * time: 2017/3/14 10:58
 * email: tom.work@foxmail.com
 */
public class ConvertUtil {
    public static final String array2Str(int[][] array) {
        StringBuffer sb = new StringBuffer();
        sb.append(array.length);
        sb.append(":");
        sb.append(array[0].length);
        sb.append(":");
        for (int h = 0; h < array.length; h++) {
            for (int v = 0; v < array[0].length; v++) {
                sb.append(array[h][v]);
            }
        }
        return sb.toString();
    }
}
