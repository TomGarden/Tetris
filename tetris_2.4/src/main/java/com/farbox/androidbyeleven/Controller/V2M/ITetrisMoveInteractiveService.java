package com.farbox.androidbyeleven.Controller.V2M;

import android.graphics.Point;

import com.farbox.androidbyeleven.Controller.Control.MoveDirection;

/**
 * describe:Square/Tetris和用户的交互
 * time: 2017/3/6 15:54
 * email: tom.work@foxmail.com
 */
public interface ITetrisMoveInteractiveService {
    /**
     * 移动方法
     *
     * @param direction 计划移动的方向
     * @return true，移动确实发生了
     */
    boolean moveTo(MoveDirection direction);

    /**
     * 调用真正的粘贴方法
     *
     * @return 如果粘贴成功：point.x = 所粘贴的Tetris所在的最底行的行数 point.y = Tetris本身的行数。
     * 粘贴失败 return null  ，说明Tetris有再Beaker之外的部分，游戏应该结束了
     */
    Point tetrisPast2BeakerMatris();

    /**
     * 消除给定的行
     *
     * @param start  开始行，行的索引从上倒下增大，，我们给定的是最下方的行索引
     * @param length 总行数
     */
    void eliminate(int x, int y);
}
