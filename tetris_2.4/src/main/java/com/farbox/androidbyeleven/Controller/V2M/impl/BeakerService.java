package com.farbox.androidbyeleven.Controller.V2M.impl;

import com.farbox.androidbyeleven.Controller.V2M.IBeakerGetterService;
import com.farbox.androidbyeleven.Model.RunModel.IBeakerModelGet;
import com.farbox.androidbyeleven.Model.RunModel.IBeakerModelSet;
import com.farbox.androidbyeleven.Controller.V2M.IBeakerConditionService;
import com.farbox.androidbyeleven.Utils.LogUtil;

/**
 * Created by Tom on 2017/1/17.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 本类用来为Beaker提供所有的需求，当然beaker为了向本类索要数据，必须提供一些他自己的基本信息。
 */
public class BeakerService implements IBeakerGetterService, IBeakerConditionService {

    /**
     * 这是BeakerModel的需求说明对象
     */
    private IBeakerModelSet iBeakerModelSet;
    /**
     * 从BeakerModel获取数据的接口
     */
    private IBeakerModelGet iBeakerModelGet;

    public BeakerService(IBeakerModelSet iBeakerModelSet, IBeakerModelGet iBeakerModelGet) {
        if (iBeakerModelSet == null || iBeakerModelGet == null) {
            throw new RuntimeException("开什么国际玩笑，乖乖搞个像样的对象过来");
        }
        this.iBeakerModelSet = iBeakerModelSet;
        this.iBeakerModelGet = iBeakerModelGet;
    }

    /**
     * 设置俄罗斯方块的边长，实现类需要根据边长获取一系列更重要的信息封装起来等待调用
     *
     * @param slideInch 边长，单位为英寸
     */
    @Override
    public void setSquareSlideInch(float slideInch) {
        if (slideInch < 0) {
            throw new RuntimeException("数据非法");
        }

        iBeakerModelSet.setSquareSideInch(slideInch);
    }

    /**
     * 设置俄罗斯方块的边长，实现类需要根据边长获取一系列更重要的信息封装起来等待调用
     *
     * @param halfSquareSpace 边长，单位为像素
     */
    @Override
    public void setHalfSquareSpace(int halfSquareSpace) {
        if (halfSquareSpace < 0) {
            throw new RuntimeException("数据非法");
        }

        iBeakerModelSet.setHalfSquareSpacePix(halfSquareSpace);
    }

    /**
     * 在能获取到的第一时间设置设置Beaker的尺寸，调用这个方法的时候就是开始计算间接参数的最好时机了
     *
     * @param beakerWidth  Beaker宽度
     * @param beakerHeight Beaker高度
     */
    @Override
    public void setBeakerSize(int beakerWidth, int beakerHeight) {
        if (beakerHeight == 0 || beakerWidth == 0) {
            throw new RuntimeException("请在合适的时机设置这个关键参数");
        }
        this.iBeakerModelSet.setBeakerSizePix(beakerWidth, beakerHeight);
    }

    /**
     * 设置Beaker背景格子线条笔触宽度
     *
     * @param beakerBGLineWidthPix
     */
    @Override
    public void setBeakerBGLineWidthPix(int beakerBGLineWidthPix) {
        this.iBeakerModelSet.setBeakerBGLineWidthPix(beakerBGLineWidthPix);
    }

    /**
     * 获取垂直页边距
     */
    @Override
    public int getMarginVerticalPix() {
        return this.iBeakerModelGet.getMarginVerticalPix();
    }

    /**
     * 获取水平页边距
     */
    @Override
    public int getMarginHorizontalPix() {
        return this.iBeakerModelGet.getMarginHorizontalPix();
    }

    /**
     * 获取每一个格子的像素数
     */
    @Override
    public int getSquareSidePix() {
        return 0;
    }

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    @Override
    public int getSideAddSpacePix() {
        return this.iBeakerModelGet.getSideAddSpacePix();
    }

    /**
     * TetrisBeaker初始化尺寸完成了getWidth和getHeight可以获取到正确的值了
     */
    @Override
    public void initSizeOver() {
        LogUtil.i(LogUtil.msg() + "本方法暂无实际意义。");
        //本来是用来一起初始化所有需要的值的，后来我们的思路变了，我们到需要的时候才初始化的。
    }

    /**
     * 获取笔触宽度所占的像素数：后文是原理
     * <p>
     * 实际上是获取和strockWith最相近的偶数原理：
     * 2016-09-01[关于Paint笔触的了解]：
     * http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return >=strockWith 的一个整数
     */
    @Override
    public int getBeakerBGLineWidthPix() {
        return iBeakerModelGet.getBeakerBGLineWidthPix();
    }

    /**
     * 获取烧杯数组
     */
    @Override
    public int[][] getBeakerMatris() {
        return iBeakerModelGet.getBeakerMatris();
    }

    /**
     * 代表squareLineWidth宽度笔触所画出来的线所占像素宽度值的一半。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return 笔触宽度/2 单位像素
     */
    @Override
    public int getHalfBeakerBGLineWidthPix() {
        return iBeakerModelGet.getHalfBeakerBGLineWidthPix();
    }

    /**
     * 获取Square之间的间距
     *
     * @return 像素距离
     */
    /*@Override
    public int getSquareSpacePix() {
        return iBeakerModelGet.getSquareSpacePix();
    }*/

    /**
     * 获取Square之间的间距的一半。
     *
     * @return 像素距离
     */
    @Override
    public int getHalfSquareSpacePix() {
        return iBeakerModelGet.getHalfSquareSpacePix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    @Override
    public int getTetrisLineWidthPix() {
        return this.iBeakerModelGet.getTetrisLineWidthPix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    @Override
    public int getHalfTetrisLineWidthPix() {
        return this.iBeakerModelGet.getHalfTetrisLineWidthPix();
    }
}
