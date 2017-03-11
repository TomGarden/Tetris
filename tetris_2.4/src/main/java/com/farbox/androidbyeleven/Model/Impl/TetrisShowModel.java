package com.farbox.androidbyeleven.Model.Impl;

import com.farbox.androidbyeleven.Model.BaseModel;
import com.farbox.androidbyeleven.Model.ITetrisShowModelGet;
import com.farbox.androidbyeleven.Model.ITetrisShowModelInteractive;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tom on 2016/11/9.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 本类掌控展示用的Tetris的数据
 */
public class TetrisShowModel implements ITetrisShowModelGet, ITetrisShowModelInteractive {

    /**
     * 缩放比例 值 为 1  表示原始大小
     */
    private float scaling = 0.5f;

//==========================================================================
    /**
     * square间距[像素]方块之间的总空隙/2
     */
    private int halfSquareSpacePix = Global.notSet;
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
     * 俄罗斯方块外层线条的笔触宽度像素
     */
    private int tetrisLineWidthPix = Global.notSet;
    /**
     * 俄罗斯方块外层线条的笔触宽度像素 ÷ 2
     */
    private int tetrisHalfLineWidthPix = Global.notSet;
//==========================================================================

    /**
     * 存储基本俄罗斯方块
     */
    private List<int[][]> tetrisMatrixList = null;

    private int[][] currentSquareMatrix = null;

    /**
     * 俄罗斯方块当前方向
     */
    private int direction = Global.notSet;
    /**
     * 4个默认的方向
     */
    private final int directionTop = 0, directionRight = 1, directionBottom = 2, directionLeft = 3, totalDirection = 4;


    public TetrisShowModel() {
        tetrisMatrixList = new ArrayList<>();
        /** 俄罗斯方块的矩阵表示 */
        tetrisMatrixList.add(new int[][]{{1, 1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 0, 0}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 1, 0}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 0, 1}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 1}, {1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 1, 0}, {0, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 1, 1}, {1, 1, 0}});
    }

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    @Override
    public int getSideAddSpacePix() {
        if (this.sideAddSpacePix == Global.notSet) {
            this.sideAddSpacePix = getHalfSquareSpacePix() + this.getSquareSidePix() + getHalfSquareSpacePix();
        }
        return this.sideAddSpacePix;
    }

    /**
     * 代表squareLineWidth宽度笔触所画出来的线所占像素宽度值的一半。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return 笔触宽度/2 单位像素
     */
    @Override
    public int getHalfBeakerBGLineWidthPix() {
        //如果要修改本方法记得查看IGetModel中本方法有几处实现
        if (this.halfBeakerBGLineWidthPix == Global.notSet) {
            this.halfBeakerBGLineWidthPix = this.getSquareSidePix() / 8;
        }
        return this.halfBeakerBGLineWidthPix;
    }

    /**
     * 获取Square之间间距的一半
     *
     * @return 像素距离
     */
    @Override
    public int getHalfSquareSpacePix() {
        if (this.halfSquareSpacePix == Global.notSet) {
            this.halfSquareSpacePix = this.getScalingData(BaseModel.getInstance().getHalfSquareSpacePix());
        }
        return this.halfSquareSpacePix;
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    @Override
    public int getTetrisLineWidthPix() {
        if (this.tetrisLineWidthPix == Global.notSet) {
            this.tetrisLineWidthPix = this.getScalingData(BaseModel.getInstance().getTetrisLineWidthPix());
        }
        return this.tetrisLineWidthPix;
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    @Override
    public int getHalfTetrisLineWidthPix() {
        if (this.tetrisHalfLineWidthPix == Global.notSet) {
            this.tetrisHalfLineWidthPix = this.getTetrisLineWidthPix() / 2;
            if (this.tetrisHalfLineWidthPix <= 0) {
                this.tetrisHalfLineWidthPix = 1;
            }
        }
        return this.tetrisHalfLineWidthPix;
    }

    /**
     * 获取每一个格子的像素数
     */
    @Override
    public int getSquareSidePix() {
        if (this.squareSidePix == Global.notSet) {
            this.squareSidePix = this.getScalingData(BaseModel.getInstance().getSquareSidePix());
        }
        return this.squareSidePix;
    }

    /**
     * 获取当前俄罗斯方块矩阵，本矩阵有两个属性:[样式、方向]
     *
     * @return
     */
    public int[][] getCurrentMatrix() {
        if (this.currentSquareMatrix == null) {
            Random random = new Random();
            this.currentSquareMatrix = tetrisMatrixList.get(random.nextInt(tetrisMatrixList.size()));//随机获取基本俄罗斯方块的索引
            this.direction = random.nextInt(totalDirection);//随机获取俄罗斯方块是方向

            //选定之后根据方向对tetris做出相应的调整
            switch (this.direction) {
                case directionTop:
                    break;
                case directionRight:
                    this.currentSquareMatrix = MathUtil.matrixRotateClockwise(this.currentSquareMatrix);
                    break;
                case directionBottom:
                    this.currentSquareMatrix = MathUtil.matrixRotateClockwise(this.currentSquareMatrix);
                    this.currentSquareMatrix = MathUtil.matrixRotateClockwise(this.currentSquareMatrix);
                    break;
                case directionLeft:
                    this.currentSquareMatrix = MathUtil.matrixRotateAntiClockwise(this.currentSquareMatrix);
            }
        }
        return this.currentSquareMatrix;
    }

    /**
     * 获取缩放数据，如果元数据缩放后小于等于0我们就置1
     *
     * @param data
     * @return
     */
    private int getScalingData(int data) {
        if (data <= 0) {
            throw new RuntimeException("数据非法");
        }
        int result = 0;
        result = (int) (data * this.scaling);
        if (result <= 0) {
            result = 1;
        }
        return result;
    }

    /**
     * 刷新俄罗斯方块，也就是刷新俄罗斯方块数组，也就是显示一个新的俄罗斯方块
     */
    @Override
    public void refreshTetris() {
        this.currentSquareMatrix = null;
    }
}
