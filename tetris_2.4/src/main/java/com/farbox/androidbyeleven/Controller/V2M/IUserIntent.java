package com.farbox.androidbyeleven.Controller.V2M;

/**
 * describe: 用户意图，View收集用户意图并且反馈给Controller,这个接口貌似可以归到Interactive接口中。暂时搁置。
 * time: 2017/3/8 16:48
 * email: tom.work@foxmail.com
 */
public interface IUserIntent {
    /**
     * 游戏状态从预备编程开始状态，也就是进入游戏界面后从初始化完成状态打开了开始游戏的开关。
     */
    void ready2Play();

    /**暂停切换到游戏*/
    void pause2Play();

    /**游戏切换到暂停*/
    void paly2Pause();
}
