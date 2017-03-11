package com.farbox.androidbyeleven.View;

/**
 * 杨铭 Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * TetrisSquare类用来获取数据的接口
 * <p>
 * 需要修改的位置的关键字“(需要修改)”
 */

public interface ISquareGetData extends IGetData {


    /**
     * 获取烧杯数组
     */
    int[][] getBeakerMatris();

    /**
     * 获取垂直页边距
     */
    int getMarginVertical();

    /**
     * 获取水平页边距
     */
    int getMarginHorizontal();

    /**
     * 获取每一个格子的像素数
     */
    int getSquareSide_Pix();

    /**
     * 这是一个边长和两个格子间隔一半的和
     */
    int getSideAddSpace();

    /**
     * 获取当前正在移动的俄罗斯方块的矩阵
     */
    int[][] getMovingTetrisMatrix();

    /**
     * 获取绘制俄罗斯方块的笔的笔触宽度
     */
    int getSquareStrokeWidth_Pix();


    //===================================================================

    /**
     * 这是格子之间的总空隙
     */
    int getSquareSpace_Pix();
}
