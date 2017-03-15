package com.farbox.androidbyeleven.Controller.V2M.impl;

import android.graphics.Point;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveGetterService;
import com.farbox.androidbyeleven.Model.ITetrisMoveModelGet;

/**
 * describe:把自己的需求用接口的形式告知Model，model按接口约定好的内容为Controller提供支持。
 * time: 2017/3/6 7:56
 * email: tom.work@foxmail.com
 */
public class TetrisMoveGetterService implements ITetrisMoveGetterService {

    private ITetrisMoveModelGet iTetrisMoveModelGet = null;

    public TetrisMoveGetterService(ITetrisMoveModelGet iTetrisMoveModelGet) {
        this.iTetrisMoveModelGet = iTetrisMoveModelGet;
    }

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    @Override
    public int getSideAddSpacePix() {
        return iTetrisMoveModelGet.getSideAddSpacePix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    @Override
    public int getTetrisLineWidthPix() {
        return iTetrisMoveModelGet.getTetrisLineWidthPix();
    }

    @Override
    public int getHalfTetrisLineWidthPix() {
        return iTetrisMoveModelGet.getHalfTetrisLineWidthPix();
    }

    /**
     * 获取Square之间间距的一半
     *
     * @return 像素距离
     */
    @Override
    public int getHalfSquareSpacePix() {
        return iTetrisMoveModelGet.getHalfSquareSpacePix();
    }

    /**
     * 获取每一个格子的像素数
     */
    @Override
    public int getSquareSidePix() {
        return iTetrisMoveModelGet.getSquareSidePix();
    }

    /**
     * 获取当前俄罗斯方块矩阵[已经处理好方向的]
     *
     * @return
     */
    @Override
    public int[][] getCurrentMatrix() {
        return iTetrisMoveModelGet.getCurrentMatrix();
    }

    /**
     * 获取当前俄罗斯方块在烧杯中的位置
     *
     * @return
     */
    @Override
    public Point getTetrisInBeakerPosI() {
        return iTetrisMoveModelGet.getTetrisInBeakerPosI();
    }

    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    @Override
    public String getMatris2Str() {
        return iTetrisMoveModelGet.getMatris2Str();
    }
}
