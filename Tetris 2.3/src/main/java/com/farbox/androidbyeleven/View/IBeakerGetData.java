package com.farbox.androidbyeleven.View;

/**
 * 杨铭 Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */

public interface IBeakerGetData extends IGetData {

    /**
     * 获取烧杯数组
     */
    int[][] getBeakerMatris();

    /**
     * 获取垂直页边距
     */
    int getMarginVertical_Pix();

    /**
     * 获取水平页边距
     */
    int getMarginHorizontal_Pix();

    /**
     * 获取每一个格子的像素数
     */
    int getSquareSide_Pix();

    /**
     * 这是一个边长和两个格子间隔一半的和
     */
    int getSideAddSpace_Pix();

    /**
     * TetrisBeaker初始化尺寸完成了getWidth和getHeight可以获取到正确的值了
     */
    void initSizeOver();

    /**
     * 询问Controller: Model是否初始化完成
     */
    //boolean isModelInitOver();
}
