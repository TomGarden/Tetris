package com.farbox.androidbyeleven.Model;

import android.util.DisplayMetrics;

import com.farbox.androidbyeleven.Model.Impl.BeakerModel;
import com.farbox.androidbyeleven.Utils.ConvertUtil;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;

/**
 * describe: 本类是app的基础信息，不仅Beaker使用，Square也要使用。
 * time: 2017/3/6 10:06
 * email: tom.work@foxmail.com
 * <p>
 * 处于节省内存和cpu的考虑我们把这些公用的数据提取出来供大家调用，这里我们使用单例模式。
 */
public class BaseModel /*implements IModelGet*/ {
    /**
     * square边长[英寸][1英寸=2.54厘米]
     */
    private float squareSideInch = Global.notSet;
    /**
     * 烧杯背景格子线宽度的。
     */
    private int beakerBGLineWidthPix = Global.notSet;
    /**
     * square间距[像素]方块之间的总空隙/2
     */
    private int halfSquareSpacePix = Global.notSet;

    //region 单例 getInstance();
    private static volatile BaseModel instance = null;

    private BaseModel() {
        //do something
    }

    public static BaseModel getInstance() {
        if (instance == null) {
            synchronized (BaseModel.class) {
                if (instance == null) {
                    instance = new BaseModel();
                }
            }
        }
        return instance;
    }

    //endregion

    /**
     * 背景方块的边长
     */
    private int squareSidePix = Global.notSet;
    /**
     * 边长加上一个两个格子之间的间距。halfSquareSpace+squareSide+halfSquareSpace
     */
    private int sideAddSpacePix = Global.notSet;
    /**
     * 烧杯背景格子线宽度的一半。
     */
    private int halfBeakerBGLineWidthPix = Global.notSet;
    /**
     * 烧杯数组
     */
    private int[][] beakerMatris = null;

    /**
     * 垂直页边距
     */
    private int marginVerticalPix = Global.notSet;
    /**
     * 水平页边距
     */
    private int marginHorizentalPix = Global.notSet;
    /**
     * 俄罗斯方块外层线条的笔触宽度像素
     */
    private int tetrisLineWidthPix = Global.notSet;
    /**
     * 俄罗斯方块外层线条的笔触宽度像素 ÷ 2
     */
    private int halfTetrisLineWidthPix = Global.notSet;

    /**
     * 设置Square边长
     *
     * @param squareSlideInch 单位英寸
     */
    public void setSquareSideInch(float squareSlideInch) {
        this.squareSideInch = squareSlideInch;
    }

    /**
     * 设置Square间距
     *
     * @param halfSquareSpacePix 单位像素
     */
    public void setHalfSquareSpacePix(int halfSquareSpacePix) {
        this.halfSquareSpacePix = halfSquareSpacePix;
    }

    /**
     * 获取Square之间的间距
     *
     * @return 像素距离
     */
    public int getHalfSquareSpacePix() {
        return this.halfSquareSpacePix;
    }

    /**
     * 设置Beaker背景格子线条笔触宽度
     *
     * @param beakerBGLineWidthPix
     */
    public void setBeakerBGLineWidthPix(int beakerBGLineWidthPix) {
        this.beakerBGLineWidthPix = beakerBGLineWidthPix;
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
    public int getBeakerBGLineWidthPix() {
        return this.beakerBGLineWidthPix;
    }

    public float getSquareSideInch() {
        return squareSideInch;
    }

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    public int getSideAddSpacePix() {
        if (this.sideAddSpacePix == Global.notSet) {
            this.sideAddSpacePix = getHalfSquareSpacePix() + this.getSquareSidePix() + getHalfSquareSpacePix();
        }
        return this.sideAddSpacePix;
    }

    /**
     * 获取以像素为单位的边长
     */
    public int getSquareSidePix() {
        if (this.squareSidePix == Global.notSet) {
            DisplayMetrics displayMetrics = Global.applicationContext.getResources().getDisplayMetrics();
            this.squareSidePix = (int) (BaseModel.getInstance().getSquareSideInch() * displayMetrics.densityDpi);
        }
        return squareSidePix;
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

    public int[][] getBeakerMatris() {
        return this.beakerMatris;
    }

    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    public String getBakerMatris2Str() {
        return ConvertUtil.array2Str(this.getBeakerMatris());
    }

    public void setBeakerMatris(int[][] beakerMatris) {
        this.beakerMatris = beakerMatris;
    }

    public int getMarginVerticalPix() {
        return marginVerticalPix;
    }

    public void setMarginVerticalPix(int marginVerticalPix) {
        this.marginVerticalPix = marginVerticalPix;
    }

    public int getMarginHorizentalPix() {
        return marginHorizentalPix;
    }

    public void setMarginHorizentalPix(int marginHorizentalPix) {
        this.marginHorizentalPix = marginHorizentalPix;
    }

    public int getTetrisLineWidthPix() {
        if (tetrisLineWidthPix == Global.notSet) {
            this.tetrisLineWidthPix = (int) (getSquareSidePix() / 6);
        }
        return tetrisLineWidthPix;
    }

    public int getHalfTetrisLineWidthPix() {
        if (this.halfTetrisLineWidthPix == Global.notSet) {
            this.halfTetrisLineWidthPix = this.getTetrisLineWidthPix() / 2;
            if (this.halfTetrisLineWidthPix <= 0) {
                this.halfTetrisLineWidthPix = 1;
            }
        }
        return this.halfTetrisLineWidthPix;
    }

    /**
     * 通知BaseModel矩阵已经改变了
     */
    public void notifyBeakerMatrisChange() {
        BeakerModel.getInstance().notifyBeakerMatrisChange();
    }
}
