package com.farbox.androidbyeleven.Model;

/**
 * Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 */
public interface ITetrisShowModelGet extends IModelGet {
    /**
     * 获取每一个格子的像素数
     */
    int getSquareSidePix();

    /**
     * 获取当前俄罗斯方块矩阵[已经处理好方向的]
     *
     * @return
     */
    int[][] getCurrentMatrix();
}
