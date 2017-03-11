package com.farbox.androidbyeleven.Controller.V2M.SuperServer;

/**
 * Created by Tom on 2016/11/7.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */
public interface IDataGetter {
    /**
     * 这是一个边长 + 两个格子间隔一半
     */
    int getSideAddSpacePix();

    /**
     * 获取一个格子的边长的像素数
     */
    int getSquareSidePix();

    /**
     * 获取Square之间的间距的一半。
     *
     * @return 像素距离
     */
    int getHalfSquareSpacePix();

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    int getTetrisLineWidthPix();

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    int getHalfTetrisLineWidthPix();
}
