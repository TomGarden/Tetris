package com.farbox.androidbyeleven.Controller.V2M.impl;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowGetterService;
import com.farbox.androidbyeleven.Model.ITetrisShowModelGet;

/**
 * describe:把自己的需求用接口的形式告知Model，model按接口约定好的内容为Controller提供支持。
 * time: 2017/3/6 7:56
 * email: tom.work@foxmail.com
 */
public class TetrisShowGetterService implements ITetrisShowGetterService {

    private ITetrisShowModelGet iGetSquareShowModel = null;

    public TetrisShowGetterService(ITetrisShowModelGet iGetSquareShowModel) {
        this.iGetSquareShowModel = iGetSquareShowModel;
    }

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    @Override
    public int getSideAddSpacePix() {
        return iGetSquareShowModel.getSideAddSpacePix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    @Override
    public int getTetrisLineWidthPix() {
        return iGetSquareShowModel.getTetrisLineWidthPix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    @Override
    public int getHalfTetrisLineWidthPix() {
        return iGetSquareShowModel.getHalfTetrisLineWidthPix();
    }

    /**
     * 获取Square之间间距的一半
     *
     * @return 像素距离
     */
    @Override
    public int getHalfSquareSpacePix() {
        return iGetSquareShowModel.getHalfSquareSpacePix();
    }

    /**
     * 获取每一个格子的像素数
     */
    @Override
    public int getSquareSidePix() {
        return iGetSquareShowModel.getSquareSidePix();
    }

    /**
     * 获取当前俄罗斯方块矩阵[已经处理好方向的]
     *
     * @return
     */
    @Override
    public int[][] getCurrentMatrix() {
        return iGetSquareShowModel.getCurrentMatrix();
    }
}
