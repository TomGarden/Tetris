package com.farbox.androidbyeleven.Controller.V2M;

import android.graphics.Point;

/**
 * describe:
 * time: 2017/3/6 7:54
 * email: tom.work@foxmail.com
 */
public interface ITetrisMoveGetterService extends ITetrisShowGetterService {

    /**
     * 获取当前俄罗斯方块在烧杯中的位置
     *
     * @return
     */
    Point getTetrisInBeakerPosI();
}
