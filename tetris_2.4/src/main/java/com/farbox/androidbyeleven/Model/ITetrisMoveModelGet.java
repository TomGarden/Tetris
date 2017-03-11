package com.farbox.androidbyeleven.Model;

import android.graphics.Point;

/**
 * Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 */
public interface ITetrisMoveModelGet extends ITetrisShowModelGet {
    /**
     * 获取当前俄罗斯方块在烧杯中的位置
     *
     * @return
     */
    Point getTetrisInBeakerPosI();
}
