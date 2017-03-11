package com.farbox.androidbyeleven.Controller.V2M.impl;

import android.graphics.Point;

import com.farbox.androidbyeleven.Controller.Control.MoveDirection;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Model.ITetrisMoveModelInteractive;

/**
 * describe:用户和控件交互
 * time: 2017/3/6 15:56
 * email: tom.work@foxmail.com
 */
public class TetrisMoveInteractiveService implements ITetrisMoveInteractiveService {

    private ITetrisMoveModelInteractive interactiveSquareMoveModel = null;

    public TetrisMoveInteractiveService(ITetrisMoveModelInteractive interactiveSquareMoveModel) {
        this.interactiveSquareMoveModel = interactiveSquareMoveModel;
    }

    /**
     * 移动方法
     *
     * @param direction 计划移动的方向
     */
    @Override
    public boolean moveTo(MoveDirection direction) {
        return this.interactiveSquareMoveModel.moveTo(direction);
    }

    /**
     * 调用真正的粘贴方法
     *
     * @return 如果粘贴成功：point.x = 所粘贴的Tetris所在的最底行的行数 point.y = Tetris本身的行数。
     * 粘贴失败 return null  ，说明Tetris有再Beaker之外的部分，游戏应该结束了
     */
    @Override
    public Point tetrisPast2BeakerMatris() {
        return this.interactiveSquareMoveModel.tetrisPast2BeakerMatris();
    }

    /**
     * 消除给定的行
     *
     * @param x
     * @param y
     */
    @Override
    public void eliminate(int x, int y) {
        this.interactiveSquareMoveModel.eliminate(x, y);
    }
}
