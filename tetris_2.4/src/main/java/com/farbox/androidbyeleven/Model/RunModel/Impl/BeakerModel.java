package com.farbox.androidbyeleven.Model.RunModel.Impl;

import com.farbox.androidbyeleven.Controller.M2V.IBeakerNotify;
import com.farbox.androidbyeleven.Model.RunModel.BaseModel;
import com.farbox.androidbyeleven.Model.RunModel.IBeakerModelGet;
import com.farbox.androidbyeleven.Model.RunModel.IBeakerModelSet;
import com.farbox.androidbyeleven.Utils.Global;

/**
 * Created by Tom on 2017/1/18.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 注：本类中的方法最好通过 其 getter 方法调用，否则极有可能出错，暂时还没有好的方案从机制上避免直接调用的可能，所以用注释的方式提醒注意。
 */
public class BeakerModel implements IBeakerModelGet, IBeakerModelSet {
    /**
     * 烧杯背景格子线宽度的。
     */
    private int halfBeakerBGLineWidthPix = Global.notSet;
    /**
     * beaker尺寸[在调用之前必须先确认是否已经被初始化了]
     */
    private BeakerSize beakerSize;

    private IBeakerNotify iBeakerNotify;

    //region 单例 getInstance();
    private static volatile BeakerModel instance = null;

    private BeakerModel(IBeakerNotify iBeakerNotify) {
        this.iBeakerNotify = iBeakerNotify;
        //do something
    }

    public static BeakerModel getInstance(IBeakerNotify iBeakerNotify) {
        if (instance == null) {
            synchronized (BeakerModel.class) {
                if (instance == null) {
                    instance = new BeakerModel(iBeakerNotify);
                }
            }
        }
        return instance;
    }

    public static BeakerModel getInstance() {
        if (instance == null) {
            throw new RuntimeException(Global.tipNotInitOver);
        }
        return instance;
    }

    //endregion

    /**
     * beaker尺寸
     */
    private class BeakerSize {

        private int width;
        private int height;

        BeakerSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getH() {
            return height;
        }

        public int getW() {
            return width;
        }
    }

    /**
     * 设置Square边长
     *
     * @param slideInch 单位英寸
     */
    @Override
    public void setSquareSideInch(float slideInch) {
        BaseModel.getInstance().setSquareSideInch(slideInch);
    }

    /**
     * 设置Square间距
     *
     * @param halfSquareSpacePix 单位像素
     */
    @Override
    public void setHalfSquareSpacePix(int halfSquareSpacePix) {
        BaseModel.getInstance().setHalfSquareSpacePix(halfSquareSpacePix);
    }

    /**
     * 设置Beaker的尺寸，调用这个方法的时候就是开始计算间接参数的最好时机了
     *
     * @param beakerWidth  Beaker宽度
     * @param beakerHeight Beaker高度
     */
    @Override
    public void setBeakerSizePix(int beakerWidth, int beakerHeight) {
        beakerSize = new BeakerSize(beakerWidth, beakerHeight);
    }

    /**
     * 设置Beaker背景格子线条笔触宽度
     *
     * @param squareLineWidthPix
     */
    @Override
    public void setBeakerBGLineWidthPix(int squareLineWidthPix) {
        if (isEven(squareLineWidthPix)) {
        } else {
            squareLineWidthPix++;
        }
        BaseModel.getInstance().setBeakerBGLineWidthPix(squareLineWidthPix);
    }

    /**
     * 获取Square之间的间距
     *
     * @return 像素距离
     */
    @Override
    public int getHalfSquareSpacePix() {
        return BaseModel.getInstance().getHalfSquareSpacePix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    @Override
    public int getTetrisLineWidthPix() {
        return BaseModel.getInstance().getTetrisLineWidthPix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    @Override
    public int getHalfTetrisLineWidthPix() {
        return BaseModel.getInstance().getHalfTetrisLineWidthPix();
    }

    /**
     * 获取Square之间的间距
     *
     * @return 像素距离
     */
    @Override
    public int getSquareSpacePix() {
        return BaseModel.getInstance().getHalfSquareSpacePix() * 2;
    }

    /**
     * 获取烧杯数组
     */
    @Override
    public int[][] getBeakerMatris() {
        if (BaseModel.getInstance().getBeakerMatris() == null) {
            BaseModel.getInstance().setBeakerMatris(this.calculateBeakerMatris());
        }
        return BaseModel.getInstance().getBeakerMatris();
    }

    /**
     * 代表squareLineWidth宽度笔触所画出来的线所占像素宽度值的一半。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return 笔触宽度/2 单位像素
     */
    public int getHalfBeakerBGLineWidthPix() {
        if (this.halfBeakerBGLineWidthPix == -1) {
            this.halfBeakerBGLineWidthPix = this.getBeakerBGLineWidthPix() / 2;
        }
        return this.halfBeakerBGLineWidthPix;
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
        return BaseModel.getInstance().getBeakerBGLineWidthPix();
    }

    /**
     * 获取垂直页边距[本数据和计算方法有些关联注意协调]
     */
    @Override
    public int getMarginVerticalPix() {
        if (BaseModel.getInstance().getMarginVerticalPix() == Global.notSet) {
            this.isInitBeakerSize();
            int calcuteResult = (this.beakerSize.getH() - this.getBeakerMatris().length * this.getSideAddSpacePix()) / 2;
            BaseModel.getInstance().setMarginVerticalPix(calcuteResult);
        }
        return BaseModel.getInstance().getMarginVerticalPix();
    }

    /**
     * 获取水平页边距
     */
    @Override
    public int getMarginHorizontalPix() {
        if (BaseModel.getInstance().getMarginHorizentalPix() == Global.notSet) {
            this.isInitBeakerSize();
            int calcuteResult = (this.beakerSize.getW() - this.getBeakerMatris()[0].length * this.getSideAddSpacePix()) / 2;
            BaseModel.getInstance().setMarginHorizentalPix(calcuteResult);
        }
        return BaseModel.getInstance().getMarginHorizentalPix();
    }

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    @Override
    public int getSideAddSpacePix() {
        return BaseModel.getInstance().getSideAddSpacePix();
    }


    /**
     * 判断num是否为偶数
     *
     * @param num 带判断的数字
     * @return true num为偶数，false num不是偶数
     */
    private boolean isEven(int num) {
        return (num & 1) == 0;
    }

    /**
     * 计算烧杯数组
     * <p>
     * 尽可能沾满所有空间来设计，边距用Beaker的容器来控制。
     */
    private int[][] calculateBeakerMatris() {
        this.isInitBeakerSize();

        int squareSidePix = BaseModel.getInstance().getSquareSidePix();
        int horizentalNum = (this.beakerSize.getW() - squareSidePix) / this.getSideAddSpacePix();
        int verticalNum = (this.beakerSize.getH() - squareSidePix) / this.getSideAddSpacePix();
        return new int[verticalNum][horizentalNum];
    }

    /**
     * 检查 烧杯尺寸 是否已经被初始化了
     *
     * @return
     */
    private boolean isInitBeakerSize() {
        //我们这样是不合理的，我们应该在手段上保证BeakerSize没有被直接调用的可能性，才能彻底避免这种错误的发生。
        if (this.beakerSize == null) {
            throw new RuntimeException("忘记初始化对象了");
        }
        return true;
    }

    /**
     * 通知BeakerModel矩阵已经改变了
     */
    public void notifyBeakerMatrisChange() {
        //告知Controller
        this.iBeakerNotify.beakerMatrisChange();
    }
}
