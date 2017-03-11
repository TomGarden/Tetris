package com.farbox.androidbyeleven.Controller.V2M;

import com.farbox.androidbyeleven.Controller.V2M.SuperServer.IDataGetter;

/**
 * Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 这里是Beaker的需求。Beaker是View层的对象，View层一切不能自己解决的需求都会反馈给Controller来寻求解决。
 */
public interface IBeakerGetterService extends IDataGetter {
    /**
     * 获取垂直页边距
     */
    int getMarginVerticalPix();

    /**
     * 获取水平页边距
     */
    int getMarginHorizontalPix();

    /**
     * TetrisBeaker初始化尺寸完成了getWidth和getHeight可以获取到正确的值了
     *
     * @deprecated 本方法暂无实际意义。
     */
    void initSizeOver();

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
     * 获取俄罗斯方块绘制线的笔触宽度的一半。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return 笔触宽度/2 单位像素
     */
    int getHalfBeakerBGLineWidthPix();

    /**
     * 获取烧杯数组
     */
    int[][] getBeakerMatris();
}
