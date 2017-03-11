package com.farbox.androidbyeleven.CustomView;

import android.graphics.Point;

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
    //region 属性
    private static TetrisAttribute singleInstance;
    /**
     * 数字未设置的标志，如果数字是这个量那就是没有设置
     */
    private final int notSetting = -1;
    /**
     * 俄罗斯方块每一个小正方形的边长——单位像素
     */
    private int squareSide_PIX = notSetting;
    /**
     * 这是格子之间的总空隙是本数值是2倍 --> TetrisBeaker.initTetrisAttribute()可以知道
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

    //endregion

    public static synchronized TetrisAttribute getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new TetrisAttribute();
        }
        return singleInstance;
    }

    /**
     * 私有构造方法
     */
    private TetrisAttribute() {
    }

    //region Getters & Setters

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
    //endregion

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
     * 把tetris粘贴到烧杯矩阵上
     * [这里会检查粘贴后是否可以消除]
     *
     * @param tetrisMatris:Tetris矩阵
     * @param point:tetris在beakerMatris中的位置，记录的是TetrisMatris中左下角那一块在beakerMatris中的位置
     * @return true粘贴成功，false粘贴失败，原因是俄罗斯方块超过了背景的顶部
     */
    public boolean tetrisPast2BeakerMatris(int[][] tetrisMatris, Point point) {
        int beakerH, beakerV;
        for (int tetirsH = 0; tetirsH < tetrisMatris.length; tetirsH++) {
            beakerH = point.y - (tetrisMatris.length - 1 - tetirsH);
            if (beakerH < 0) {//说明有一部分在顶部的背景之外
                return false;
            }
            for (int tetrisV = 0; tetrisV < tetrisMatris[0].length; tetrisV++) {
                if (tetrisMatris[tetirsH][tetrisV] == 1) {
                    beakerV = point.x + tetrisV;
                    beakerMatris[beakerH][beakerV] = 1;
                }
            }
        }
        return true;
        //删除Tetris
        //消除可以消除的行
    }

    /**
     * 消除给定的行
     *
     * @param start  开始行，行的索引从上倒下增大，，我们给定的是最下方的行索引
     * @param length 总行数
     */
    public void eliminate(int start, int length) {//3,20
        boolean eliminate = true;//消除？
        //_Log.e(_Log.msg() + _Log.likeCoordinate("[应该消除的行数,倒数第一行索引]", length, start));
        for (int i = 0, h = start - i; i < length; i++, h--) {
            //_Log.i(_Log.msg() + ":::" + (h));
            for (int v = 0; v < beakerMatris[0].length; v++) {
                if (beakerMatris[h][v] == 0) {
                    eliminate = false;
                    break;
                }
            }
            if (eliminate) {//消除这一行
                for (int v = 0; v < beakerMatris[0].length; v++) {
                    if (beakerMatris[h][v] != 0) {
                        beakerMatris[h][v] = 0;
                    }
                }
                //以上整体下移
                for (int mh = h; mh > 0; mh--) {
                    for (int v = 0; v < beakerMatris[0].length; v++) {
                        if (beakerMatris[mh][v] != beakerMatris[mh - 1][v]) {
                            beakerMatris[mh][v] = beakerMatris[mh - 1][v];
                        }
                    }
                }
                //顶行置空
                for (int v = 0; v < beakerMatris[0].length; v++) {
                    if (beakerMatris[0][v] != 0) {
                        beakerMatris[0][v] = 0;
                    }
                }
                //索引重置
                h++;
                length--;
                i--;
                _Log.e(_Log.msg() + "消除了：" + h);
            }
            eliminate = true;
        }
    }
}
