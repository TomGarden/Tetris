package com.farbox.androidbyeleven.Model;

/**
 * Created by Tom on 2017/1/18.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 这是Controller从BeakerModel中获取数据的协议，BeakerModel应该满足这一协议。
 */
public interface IBeakerModelGet extends IModelGet {

    /**
     * 获取笔触宽度所占的像素数：后文是原理
     * <p>
     * 实际上是获取和strockWith最相近的偶数原理：
     * 2016-09-01[关于Paint笔触的了解]：
     * http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return >=strockWith 的一个整数
     */
    int getBeakerBGLineWidthPix();

    /**
     * 获取Square之间的间距
     *
     * @return 像素距离
     */
    int getSquareSpacePix();

    /**
     * 获取垂直页边距
     */
    int getMarginVerticalPix();

    /**
     * 获取水平页边距
     */
    int getMarginHorizontalPix();

    /**
     * 获取烧杯数组
     */
    int[][] getBeakerMatris();
}
