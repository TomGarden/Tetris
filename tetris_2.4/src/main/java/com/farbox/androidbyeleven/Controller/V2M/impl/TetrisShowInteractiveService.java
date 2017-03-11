package com.farbox.androidbyeleven.Controller.V2M.impl;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowInteractiveService;
import com.farbox.androidbyeleven.Model.ITetrisShowModelInteractive;

/**
 * describe:用户和控件交互
 * time: 2017/3/6 15:56
 * email: tom.work@foxmail.com
 */
public class TetrisShowInteractiveService implements ITetrisShowInteractiveService {

    private ITetrisShowModelInteractive interactiveSquareShowModel = null;

    public TetrisShowInteractiveService(ITetrisShowModelInteractive interactiveSquareShowModel) {
        this.interactiveSquareShowModel = interactiveSquareShowModel;
    }

    /**
     * 刷新俄罗斯方块，也就是刷新俄罗斯方块数组，也就是显示一个新的俄罗斯方块
     */
    @Override
    public void refreshTetris() {
        this.interactiveSquareShowModel.refreshTetris();
    }
}
