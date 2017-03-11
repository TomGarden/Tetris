package com.farbox.androidbyeleven.Model;

import android.content.Context;
import android.util.DisplayMetrics;

import com.farbox.androidbyeleven.Util._Log;

/**
 * 杨铭 Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 所有的数据都存储在本类中，不管是永久性的数据，还是运行时的数据
 */
//* 我们暂定它单挑，如果后续发现不行，再行修改
public class BeakerModel {
    /**
     * 接口
     */
    private IBeakerModelGetData getData = null;

    private Context appContext = null;

    private final int notSetting = -1;

    //region 从TetrisBeaker中获取的原始数据(直接数据),这是本类中一切数据的根
    /**
     * 格子的宽度单位英寸[1英寸=2.54厘米]
     */
    private float squareSide_Inch = notSetting;
    /**
     * 方块之间的总空隙
     */
    private int squareSpace_Pix = notSetting;
    //endregion

    //region  间接属性
    /**
     * 俄罗斯方块每一个小正方形的边长——单位像素
     */
    private int squareSide_Pix = notSetting;
    /**
     * 这是一个边长和两个格子间隔的和
     */
    private int sideAddSpace_Pix = notSetting;
    /**
     * 烧杯数组
     */
    private int[][] beakerMatris = null;
    /**
     * 页边距
     */
    private int marginHorizontal_Pix = notSetting, marginVertical_Pix = notSetting;
    //endregion

    /**
     * 构造方法
     */
    public BeakerModel(Context appContext, IBeakerModelGetData getData) {
        this.appContext = appContext;
        this.getData = getData;
        initTetrisBeakerAttrs();
    }

    /**
     * 初始化和TetrisBeaker 相关的属性
     */
    private void initTetrisBeakerAttrs() {
        //从TetrisBeaker获取直接属性
        _Log.i(_Log.msg() + _Log.likeCoordinate("[squareSide_Inch,squareSpace_Pix]", squareSide_Inch, squareSpace_Pix));
        this.squareSide_Inch = this.getData.getSquareSide_Inch();
        this.squareSpace_Pix = this.getData.getSquareSpace_Pix();
        _Log.i(_Log.msg() + _Log.likeCoordinate("[squareSide_Inch,squareSpace_Pix]", squareSide_Inch, squareSpace_Pix));
        //得到间接属性
        DisplayMetrics displayMetrics = appContext.getResources().getDisplayMetrics();
        //得到俄罗斯方块每小块的像素尺寸
        this.squareSide_Pix = (int) (this.squareSide_Inch * displayMetrics.densityDpi);
        this.sideAddSpace_Pix = this.squareSide_Pix + this.squareSpace_Pix;
        //还有些属性得等待时机，这个交给Conroller处理了
    }

    /**
     * 初始化矩阵和页边距
     * <p>
     * 绘制俄罗斯方块不需要这些属性，所以绘制俄罗斯方块时不用等待他们初始化
     */
    public void init_Matris_Margins(int beakerWidth, int beakerHeight) {
        //初始化背景矩阵
        int horizentalNum = (beakerWidth - squareSide_Pix) / sideAddSpace_Pix;
        int verticalNum = (beakerHeight - squareSide_Pix) / sideAddSpace_Pix;
        beakerMatris = new int[verticalNum][horizentalNum];
        //初始化页边距
        marginHorizontal_Pix = (beakerWidth - sideAddSpace_Pix * horizentalNum + squareSpace_Pix) / 2;
        marginVertical_Pix = (beakerHeight - sideAddSpace_Pix * verticalNum + squareSpace_Pix) / 2;

        _Log.i(_Log.msg() + _Log.likeCoordinate("[horizentalNum,verticalNum]", horizentalNum, verticalNum));
        _Log.i(_Log.msg() + _Log.likeCoordinate("[marginHorizontal_Pix,marginVertical_Pix]", marginHorizontal_Pix, marginVertical_Pix));
    }

    //region Getter
    public int[][] getBeakerMatris() {
        return beakerMatris;
    }

    public int getMarginVertical_Pix() {
        return marginVertical_Pix;
    }

    public int getMarginHorizontal_Pix() {
        return marginHorizontal_Pix;
    }

    public int getSquareSide_Pix() {
        return squareSide_Pix;
    }

    public int getSideAddSpace_Pix() {
        return sideAddSpace_Pix;
    }
    //endregion
}
