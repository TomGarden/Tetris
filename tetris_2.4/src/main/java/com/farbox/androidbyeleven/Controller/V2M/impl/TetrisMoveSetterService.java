package com.farbox.androidbyeleven.Controller.V2M.impl;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveSetterService;
import com.farbox.androidbyeleven.Model.ITetrisMoveModelSet;

/**
 * describe:把自己的需求用接口的形式告知Model，model按接口约定好的内容为Controller提供支持。
 * time: 2017/3/6 7:56
 * email: tom.work@foxmail.com
 */
public class TetrisMoveSetterService implements ITetrisMoveSetterService {

    private ITetrisMoveModelSet setSquareMoveModel;

    public TetrisMoveSetterService(ITetrisMoveModelSet setSquareMoveModel) {
        this.setSquareMoveModel = setSquareMoveModel;
    }

    /**
     * 设置当前正在显示的矩阵
     *
     * @return
     */
    @Override
    public void setCurrentMatrix(int[][] currentMatrix) {
        this.setSquareMoveModel.setCurrentMatrix(currentMatrix);
    }
}
