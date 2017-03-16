package com.farbox.androidbyeleven.Model.RunModel;

/**
 * Created by Tom on 2017/1/18.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 这是Controller向BeakerModel中填充数据的协议，BeakerModel应该满足这一协议。
 */

public interface IBeakerModelSet {

    /**
     * 设置Square边长
     *
     * @param slideInch 单位英寸
     */
    void setSquareSideInch(float slideInch);

    /**
     * 设置Square间距的一半
     *
     * @param halfSquareSpace 单位像素
     */
    void setHalfSquareSpacePix(int halfSquareSpace);

    /**
     * 设置Beaker的尺寸，调用这个方法的时候就是开始计算间接参数的最好时机了
     *
     * @param beakerWidth  Beaker宽度
     * @param beakerHeight Beaker高度
     */
    void setBeakerSizePix(int beakerWidth, int beakerHeight);

    /**
     * 设置Beaker背景格子线条笔触宽度
     *
     * @param squareLineWidthPix
     */
    void setBeakerBGLineWidthPix(int squareLineWidthPix);
}
