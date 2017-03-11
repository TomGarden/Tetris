package com.farbox.androidbyeleven.Controller.V2M;

import android.graphics.Point;

/**
 * describe:
 * time: 2017/3/6 7:54
 * email: tom.work@foxmail.com
 */
public interface ITetrisMoveSetterService {

    /**
     * 设置当前正在显示的矩阵
     *
     * @return
     */
    void setCurrentMatrix(int[][] currentMatrix);
}
