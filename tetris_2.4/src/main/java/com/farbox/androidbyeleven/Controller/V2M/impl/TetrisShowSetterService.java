package com.farbox.androidbyeleven.Controller.V2M.impl;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveSetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowSetterService;
import com.farbox.androidbyeleven.Model.RunModel.ITetrisMoveModelSet;
import com.farbox.androidbyeleven.Model.RunModel.ITetrisShowModelSet;

/**
 * describe:把自己的需求用接口的形式告知Model，model按接口约定好的内容为Controller提供支持。
 * time: 2017/3/6 7:56
 * email: tom.work@foxmail.com
 */
public class TetrisShowSetterService implements ITetrisShowSetterService {

    private ITetrisShowModelSet setSquareShowModel;

    public TetrisShowSetterService(ITetrisShowModelSet setSquareShowModel) {
        this.setSquareShowModel = setSquareShowModel;
    }

    /**
     * 设置当前正在显示的矩阵
     *
     * @return
     */
    @Override
    public void setCurrentMatrix(int[][] currentMatrix) {
        this.setSquareShowModel.setCurrentMatrix(currentMatrix);
    }
}
