package com.farbox.androidbyeleven.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * 杨铭 Created by Tom on 2016/11/9.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 专门为俄罗斯方块服务的Model
 */

public class TetrisModel {
    private final int notSetting = -1;

    /**
     * 存储基本俄罗斯方块
     */
    private List<int[][]> tetrisMatrixList = null;

    /**
     * 方向
     */
    private final class Direction {
        private final int top = 0, right = 1, bottom = 2, left = 3;
        /**
         * 这个程序中一共有4个方向
         */
        private final int totalDirection = 4;

        public int size() {
            return totalDirection;
        }
    }

    /**
     * 当前的俄罗斯方块矩阵
     */
    private int[][] moveTetrisMatrix = null, showTetrisMatrix = null;
    /**
     * 当前的俄罗斯方块方向
     */
    private int moveDirection = notSetting, showDirection = notSetting;

    public TetrisModel() {
        tetrisMatrixList = new ArrayList<>();
        /** 俄罗斯方块的矩阵表示 */
        tetrisMatrixList.add(new int[][]{{1, 1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 0, 0}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 1, 0}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 0, 1}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 1}, {1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 1, 0}, {0, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 1, 1}, {1, 1, 0}});
    }
}
