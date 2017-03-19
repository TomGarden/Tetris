package com.farbox.androidbyeleven.Controller.V2M.impl;

import android.graphics.Point;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveSetterService;
import com.farbox.androidbyeleven.Model.RunModel.ITetrisMoveModelSet;
import com.farbox.androidbyeleven.Utils.LogUtil;

/**
 * describe:把自己的需求用接口的形式告知Model，model按接口约定好的内容为Controller提供支持。
 * time: 2017/3/6 7:56
 * email: tom.work@foxmail.com
 */
public class TetrisMoveSetterService implements ITetrisMoveSetterService {

    private ITetrisMoveModelSet setTetrisMoveModel;

    public TetrisMoveSetterService(ITetrisMoveModelSet setTetrisMoveModel) {
        this.setTetrisMoveModel = setTetrisMoveModel;
    }

    /**
     * 设置当前正在显示的矩阵
     *
     * @return
     */
    @Override
    public void setCurrentMatrix(int[][] currentMatrix) {
        LogUtil.i(LogUtil.msg() + "this.setTetrisMoveModel.setCurrentMatrix(currentMatrix);");
        this.setTetrisMoveModel.setCurrentMatrix(currentMatrix);
    }

    /**
     * 设置在背景矩阵中的逻辑位置
     *
     * @param pos
     */
    @Override
    public void setLogicPos(Point pos) {
        setTetrisMoveModel.setLogicPos(pos);
    }
}
