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

    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    String getMatris2Str();
}
