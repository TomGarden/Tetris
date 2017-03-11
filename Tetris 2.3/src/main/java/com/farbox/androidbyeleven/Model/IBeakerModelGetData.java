package com.farbox.androidbyeleven.Model;

/**
 * 杨铭 Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */

public interface IBeakerModelGetData {
    /**
     * 从控件中获取用户(默认)设定的每一个俄罗斯方块组成块的边长，单位英寸
     */
    float getSquareSide_Inch();

    /**
     * 从控件中获取用户(默认)设定的每一个俄罗斯方块之间的间距，单位像素
     */
    int getSquareSpace_Pix();
}
