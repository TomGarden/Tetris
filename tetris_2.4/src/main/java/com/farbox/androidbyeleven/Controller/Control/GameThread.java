package com.farbox.androidbyeleven.Controller.Control;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.farbox.androidbyeleven.Controller.V2M.IUserIntent;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.TetrisMove;
import com.farbox.androidbyeleven.View.Weight.TetrisShow;

/**
 * describe: 游戏控制线程
 * time: 2017/3/7 15:47
 * email: tom.work@foxmail.com
 */
public class GameThread extends Thread implements IUserIntent {

    private Handler mHandler = null;
    private TetrisMoveInteractiveService tetrisMoveInteractiveService;
    /**
     * 用于线程等待之前保存线程开始等待时候的游戏状态，当恢复游戏状态的时候从这里找到值并且把本对象置空
     */
    private GameState saveState = null;

    //region 单例 getInstance();
    private static volatile GameThread instance = null;

    private GameThread(Handler mHandler, TetrisMoveInteractiveService tetrisMoveInteractiveService) {
        this.mHandler = mHandler;
        this.tetrisMoveInteractiveService = tetrisMoveInteractiveService;
    }

    /**
     * 首次初始化调用本方法
     *
     * @return
     */
    public static GameThread getInstance(Handler mHandler, TetrisMoveInteractiveService tetrisMoveInteractiveService) {
        if (instance == null) {
            synchronized (GameThread.class) {
                if (instance == null) {
                    instance = new GameThread(mHandler, tetrisMoveInteractiveService);
                }
            }
        }
        return instance;
    }

    public static GameThread getInstance() {
        if (instance == null) {
            throw new RuntimeException(Global.tipNotInitOver);
        }
        return instance;
    }

    private GameThread() {

    }
    //endregion

    /**
     * 俄罗斯方块移动间歇时间,作为游戏等级
     */
    private int level = 400;

    @Override
    public synchronized void run() {
        try {
            while (this.isAlive()) {
                Thread.sleep(level);
                switch (Global.getGameState()) {
                    case ready:
                        TetrisMove tetrisMove = TetrisMove.getInstance();
                        TetrisShow tetrisShow = TetrisShow.getInstance();
                        tetrisMove.setCurrentMatrix(tetrisShow.getCurrentMatrix());//交接矩阵
                        this.mHandler.sendMessage(this.packMsg(Global.getGameState(), GameState.moving));
                        /*tetrisShow.refreshTetris();
                        tetrisMove.refreshTetris();
                        Global.gameState = GameState.moving;*/
                        //return;
                        break;
                    case moving://down,控制下移的入口有两个，一个是线程，一个是手动
                        //Model 数据修改
                        if (this.tetrisMoveInteractiveService.moveTo(MoveDirection.bottom)) {
                            //View 界面刷新
                            this.mHandler.sendMessage(this.packMsg(Global.getGameState(), null));
                            /*tetrisMove.refreshTetris();*/
                        } else {//去先粘贴
                            this.mHandler.sendMessage(this.packMsg(Global.getGameState(), GameState.ready));
                        }
                        break;

                    case createTetrisOk:
                        break;
                    case noticeGameWait:
                        //Toast.makeText(Global.applicationContext, "暂停", Toast.LENGTH_LONG).show();
                        //保存游戏状态，然后wait
                        SaveProgress sp = new SaveProgress();
                        sp.saveProgress();
                        sp.print();
                        LogUtil.i(LogUtil.msg() + "保存完毕~~");
                        this.wait();
                        break;
                    case gameOver:
                        LogUtil.i(LogUtil.msg() + "子线程应该记录数据，主线程应该显示动画");
                        return;
                }
                //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("[level,state]", this.level, Global.gameState));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 游戏状态从预备编程开始状态，也就是进入游戏界面后从初始化完成状态打开了开始游戏的开关。
     */
    @Override
    public void ready2Play() {
        this.start();
    }

    /**
     * 暂停切换到游戏
     */
    @Override
    public synchronized void pause2Play() {
        Global.setGameState(this.getSaveState());
        this.notify();
    }

    /**
     * 游戏切换到暂停
     */
    @Override
    public void paly2Pause() {
        //首先保存本来的状态
        this.setSaveState(Global.getGameState());
        //设置最新状态
        Global.setGameState(GameState.noticeGameWait);

        LogUtil.i(LogUtil.msg() + "应该后台保存游戏进度");
    }


    /**
     * 打包消息
     *
     * @param reason 发送消息之前的 GameState
     * @param result 希望发送消息之后的 GameState
     * @return
     */
    private Message packMsg(GameState reason, GameState result) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Global.msgReason, reason);
        bundle.putSerializable(Global.msgResult, result);
        message.setData(bundle);
        return message;
    }

    public GameState getSaveState() {
        return saveState;
    }

    public void setSaveState(GameState saveState) {
        this.saveState = saveState;
    }

    public void changeLevel() {
        if (this.level + 100 > 1000) {
            this.level = 100;
        } else {
            this.level += 100;
        }
    }

    public int getLevel() {
        return this.level;
    }
}
