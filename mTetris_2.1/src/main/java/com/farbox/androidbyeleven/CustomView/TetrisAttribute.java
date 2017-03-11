package com.farbox.androidbyeleven.CustomView;

import com.farbox.androidbyeleven.Util._Log;

/**
 * 杨铭 Created by Tom on 2016/10/22.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 从烧杯和俄罗斯方块中抽取出来的相同的属性。
 * 单例模式
 * <p>
 * 相同的属性包括俄罗斯方块的尺寸以及俄罗斯方块容器的矩阵码
 */

public class TetrisAttribute {
    /**
     * 数字未设置的标志，如果数字是这个量那就是没有设置
     */
    private final int notSetting = -1;
    /**
     * 正方形块之间的最小间距
     */
    private final int minSpace = 1;
    /**
     * 初始比例，就是不缩放情况下的比例 值为1
     */
    private final int initialScale = 1;
    /**
     * 俄罗斯方块每一个小正方形的边长——单位像素
     */
    private int squareSide_PIX = notSetting;
    /**
     * 这是格子之间的空隙总空隙是本数值是2倍 --> TetrisBeaker.initTetrisAttribute()可以知道
     */
    private int squareSpace_PIX = notSetting;
    /**
     * 这是一个边长和两个格子间隔的和
     */
    private int sideAddSpace = notSetting;
    /**
     * 水平页边距
     */
    private int marginHorizontal = notSetting;
    /**
     * 垂直页边距
     */
    private int marginVertical = notSetting;
    /**
     * 烧杯数组
     */
    private int[][] beakerMatris = null;
    /**
     * 这是缩放比例，用来监控，俄罗斯方块的缩放;
     * 是-1表示不缩放
     */
    private float squareScale = initialScale;

    private static TetrisAttribute singleInstance;

    /**
     * 私有构造方法
     */
    private TetrisAttribute() {
    }

    public static synchronized TetrisAttribute getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new TetrisAttribute();
        }
        return singleInstance;
    }

    public void setSquareScale(float squareScale) {
        if (squareScale > 1 || squareScale < 0) {
            throw new RuntimeException("字段未正常初始化异常-->这里只接受(0,1]区间的值");
        }
        this.squareScale = squareScale;
    }

    public void showScale() {
        _Log.e(_Log.msg() + "当前比例为：" + this.squareScale);
    }

    public int getSquareSide_PIX() {
        if (this.squareSide_PIX == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        if (this.squareScale == initialScale) {
            return squareSide_PIX;
        } else {
            this.isThrowException(squareSide_PIX);
            return this.scale(squareSide_PIX);
        }
    }

    public void setSquareSide_PIX(int squareSide_PIX) {
        this.squareSide_PIX = squareSide_PIX;
    }

    public int[][] getBeakerMatris() {
        if (this.beakerMatris == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return beakerMatris;
    }

    public void setBeakerMatris(int[][] beakerMatris) {
        this.beakerMatris = beakerMatris;
    }

    public int getSquareSpace_PIX() {
        if (this.squareSpace_PIX == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        if (this.squareScale == initialScale) {
            return squareSpace_PIX;
        } else {
            try {
                this.isThrowException(squareSpace_PIX);
            } catch (Exception e) {
                return minSpace;
            }
            return scale(squareSpace_PIX);
        }


    }

    public void setSquareSpace_PIX(int squareSpace_PIX) {
        this.squareSpace_PIX = squareSpace_PIX;
    }

    public int getSideAddSpace() {
        if (this.sideAddSpace == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        if (this.squareScale == initialScale) {
            return sideAddSpace;
        } else {
            return getSquareSide_PIX() + getSquareSpace_PIX() * 2;
        }
    }

    public void setSideAddSpace(int sideAddSpace) {
        this.sideAddSpace = sideAddSpace;
    }

    public int getMarginHorizontal() {
        if (this.marginHorizontal == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return marginHorizontal;
    }

    public void setMarginHorizontal(int marginHorizontal) {
        this.marginHorizontal = marginHorizontal;
    }

    public int getMarginVertical() {
        if (this.marginVertical == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return marginVertical;
    }

    public void setMarginVertical(int marginVertical) {
        this.marginVertical = marginVertical;
    }

    /**
     * 判断num是否为偶数
     *
     * @param num
     * @return true num为偶数，false num不是偶数
     */
    private boolean isEven(int num) {
        if ((num & 1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取笔触宽度所占的像素数：后文是原理
     * <p>
     * 实际上是获取和strockWith最相近的偶数原理：
     * 2016-09-01[关于Paint笔触的了解]：
     * http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @param strockWith
     * @return >=strockWith 的一个整数
     */
    public int getStrockWith_Pix(int strockWith) {
        if (isEven(strockWith)) {
            return strockWith;
        } else {
            return strockWith + 1;
        }
    }

    private void isThrowException(int num) {
        if ((int) (num * squareScale) <= 0) {
            new RuntimeException("超出预算运算结果");
        }
    }

    /**
     * 真正的缩放动作
     */
    private int scale(int num) {
        return (int) (num * squareScale);
    }
}
