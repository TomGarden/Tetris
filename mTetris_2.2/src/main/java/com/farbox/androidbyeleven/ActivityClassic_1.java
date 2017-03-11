package com.farbox.androidbyeleven;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.farbox.androidbyeleven.CustomView.TetrisAttribute;
import com.farbox.androidbyeleven.CustomView.TetrisBeaker;
import com.farbox.androidbyeleven.CustomView.TetrisSquare;
import com.farbox.androidbyeleven.Util._Log;

/**
 * Created by Tom on 2016/11/1.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * 我们约定可以移动的那个动画内容称之为俄罗斯方块也就是Tetris，背景我们称之为烧杯(容器)Beaker
 */
public class ActivityClassic_1 extends Activity implements TetrisBeaker.InitOverLinstener, CompoundButton.OnCheckedChangeListener {

    private Switch aSwitch;
    /**
     * 展示下一个Tetris的地方
     */
    private LinearLayout ll_widget;
    /**
     * 盛放Beaker和Tetris并且在这个区域内完成游戏动画
     */
    private FrameLayout fl_viewGroup;
    private Thread gameThread;
    public static Handler handler;
    private int level = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_1);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        this.initWidget();
        this.initThread();
        this.initHander();
    }

    //region init
    private void initWidget() {
        aSwitch = (Switch) findViewById(R.id.switch_);
        aSwitch.setOnCheckedChangeListener(this);
        TetrisBeaker.tetrisBeakerInstance = (TetrisBeaker) findViewById(R.id.tetrisBeaker);
        TetrisBeaker.tetrisBeakerInstance.addInitOverLinstener(this);

        this.ll_widget = (LinearLayout) findViewById(R.id.ll_widget);
        this.fl_viewGroup = (FrameLayout) findViewById(R.id.fl_viewGroup);
    }

    private void initThread() {
        if (gameThread == null) {
            gameThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (this.isAlive()) {
                            Thread.sleep(level);
                            switch (TetrisSquare.state) {
                                case move_ing:
                                    TetrisSquare.getMoveTetris().move(TetrisSquare.Move.bottom);
                                    break;
                                case createTetris_ok:
                                    Message message = handler.obtainMessage();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                    break;
                                case letSumThread_waiting:
                                    synchronized (gameThread) {
                                        gameThread.wait();
                                    }
                                    break;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
    }

    /**
     * 初始化Handler
     */
    private void initHander() {
        if (this.handler == null) {
            this.handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0://粘贴
                            //TetrisSquare.state = TetrisSquare.UiThreadState.pastTetris_ing;--发送消息之前就已经设置了粘贴状态了
                            TetrisSquare.getMoveTetris().pastTetris();
                            TetrisBeaker.tetrisBeakerInstance.invalidate();
                            TetrisSquare.state = TetrisSquare.UiThreadState.createTetris_ok;
                            break;
                        case 1://创建
                            TetrisSquare.state = TetrisSquare.UiThreadState.createTetris_ing;
                            createNewTetrisSquare();
                            TetrisSquare.state = TetrisSquare.UiThreadState.move_ing;
                        default:
                            super.handleMessage(msg);
                    }
                }
            };
        }
    }

    /**
     * 自定义接口个当TetrisBeaker初始化完成，他将调用本方法，完成TetrisSquare的一次初始化
     * <p>
     * 这同时也是控件支配容器的不良结构，要问容器中的各个孩子怎么出现什么时候出现还是容器最清楚了。
     */
    @Override
    public void viewInitOver() {
        TetrisSquare.getShowInstance(this);
        //设置最小ll_widght 高度
        this.ll_widget.setMinimumHeight(TetrisAttribute.getSingleInstance().getSideAddSpace() * 5);
        this.ll_widget.addView(TetrisSquare.showTetrisSquare);
    }
    //endregion

    /**
     * 创建新的Tetris方块，展示方块变成移动方块，并且重新创建展示方块
     */
    private void createNewTetrisSquare() {
        this.fl_viewGroup.removeView(TetrisSquare.moveTetrisSquare);
        this.ll_widget.removeView(TetrisSquare.showTetrisSquare);
        TetrisSquare.getMoveInstance(this);
        this.ll_widget.addView(TetrisSquare.showTetrisSquare);
        TetrisSquare.moveTetrisSquare.setX(TetrisSquare.moveTetrisSquare.getmPointPIX().x);
        TetrisSquare.moveTetrisSquare.setY(TetrisSquare.moveTetrisSquare.getmPointPIX().y);
        this.fl_viewGroup.addView(TetrisSquare.moveTetrisSquare);
    }

    /**
     * Switch控件的状态改变
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        _Log.e(_Log.msg() + "gameThread.getState() = " + gameThread.getState());
        _Log.i(_Log.msg() + "isChecked = " + isChecked);
        if (isChecked) {//启动游戏
            operateGame();
        } else {//暂停
            TetrisSquare.state = TetrisSquare.UiThreadState.letSumThread_waiting;
        }
    }

    /**
     * 重复调用可以暂停已经开始的游戏，开始已经暂停的游戏
     */
    private void operateGame() {
        switch (this.gameThread.getState()) {
            case NEW://至今尚未启动的线程处于这种状态。
                this.createNewTetrisSquare();
                this.gameThread.start();
                break;
            case RUNNABLE://正在 Java 虚拟机中执行的线程处于这种状态。
                this.gameThread.suspend();
                break;
            case BLOCKED:// 受阻塞并等待某个监视器锁的线程处于这种状态。
                break;
            case WAITING://无限期地等待另一个线程来执行某一特定操作的线程处于这种状态。
                TetrisSquare.state = TetrisSquare.UiThreadState.move_ing;
                synchronized (gameThread) {
                    gameThread.notify();
                }
                break;
            case TIMED_WAITING://等待另一个线程来执行取决于指定等待时间的操作的线程处于这种状态。
                TetrisSquare.state = TetrisSquare.UiThreadState.move_ing;
                synchronized (gameThread) {
                    gameThread.notify();
                }
                break;
            case TERMINATED://已退出的线程处于这种状态。
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        _Log.v(_Log.msg() + "onWindowFocusChanged" + String.format("[%d,%d]", TetrisBeaker.tetrisBeakerInstance.getWidth(), TetrisBeaker.tetrisBeakerInstance.getHeight()));
    }
}

