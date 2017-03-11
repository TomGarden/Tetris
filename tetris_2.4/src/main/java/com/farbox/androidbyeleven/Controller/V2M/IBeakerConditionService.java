package com.farbox.androidbyeleven.Controller.V2M;

/**
 *  Created by Tom on 2017/1/18.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * Controller会为View层提供必要的帮助，但是这需要View层必要的协助。
 */
public interface IBeakerConditionService {

    /**
     * 设置俄罗斯方块的边长，实现类需要根据边长获取一系列更重要的信息封装起来等待调用
     *
     * @param slideInch 边长，单位为英寸
     */
    void setSquareSlideInch(float slideInch);

    /**
     * 设置俄罗斯方块的边长，实现类需要根据边长获取一系列更重要的信息封装起来等待调用
     *
     * @param halfSquareSpace 边长，单位为像素
     */
    public void setHalfSquareSpace(int halfSquareSpace);

    /**
     * 设置Beaker的尺寸，调用这个方法的时候就是开始计算间接参数的最好时机了
     *
     * @param beakerWidth  Beaker宽度
     * @param beakerHeight Beaker高度
     */
    void setBeakerSize(int beakerWidth, int beakerHeight);

    /**
     * 设置Beaker背景格子线条笔触宽度
     * @param beakerBGLineWidthPix
     */
    void setBeakerBGLineWidthPix(int beakerBGLineWidthPix);
}
