package com.farbox.androidbyeleven.mtetris_20.CustomView;

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
     * 烧杯数组
     */
    private int[][] beakerMatris = null;

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

    public int getSquareSide_PIX() {
        if (this.squareSide_PIX == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return squareSide_PIX;
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
        return squareSpace_PIX;
    }

    public void setSquareSpace_PIX(int squareSpace_PIX) {
        this.squareSpace_PIX = squareSpace_PIX;
    }

    public int getSideAddSpace() {
        if (this.sideAddSpace == this.notSetting) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return sideAddSpace;
    }

    public void setSideAddSpace(int sideAddSpace) {
        this.sideAddSpace = sideAddSpace;
    }
}
