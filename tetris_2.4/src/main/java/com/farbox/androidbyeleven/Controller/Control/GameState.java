package com.farbox.androidbyeleven.Controller.Control;

/**
 * describe: 存储游戏状态,主要存储两个线程的状态信息
 * time: 2017/3/7 15:57
 * email: tom.work@foxmail.com
 */
public enum GameState {
    /**
     * 向背景粘贴俄罗斯方块
     */
    pastTetrisIng, pastTetrisOk,
    /**
     * 创建新的俄罗斯方块
     */
    createTetrisIng, createTetrisOk,
    /**
     * 消除俄罗斯方块
     */
    eliminating, eliminateOk,
    /**
     * 正在处于移动状态
     */
    moving,
    /**
     * 告诉游戏线程去wait
     */
    noticeGameWait,
    /**
     * 预备，程序刚启动没有任何操作的时候的状态
     */
    ready,
    /**
     * 暂停状态
     */
    /*pause,*/
    /**
     * 游戏结束了
     */
    gameOver,
    /**
     * 读取上次的进度
     */
    readProgress,
    /**
     * GameThread向MainThread发送消息之后暂时处于本状态，因为如果level时间极短的话会造成不可预知的错误,我们从逻辑上屏蔽它
     */
    sendMsg
}

