package com.farbox.androidbyeleven.Controller.V2M;

import com.farbox.androidbyeleven.Controller.V2M.SuperServer.ITetrisGetterService;

/**
 * describe:
 * time: 2017/3/6 7:54
 * email: tom.work@foxmail.com
 */
public interface ITetrisShowGetterService extends ITetrisGetterService {
    /**
     * 获取当前俄罗斯方块矩阵[已经处理好方向的]
     *
     * @return
     */
    int[][] getCurrentMatrix();
}
