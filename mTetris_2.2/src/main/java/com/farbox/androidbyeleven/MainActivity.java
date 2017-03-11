package com.farbox.androidbyeleven;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.farbox.androidbyeleven.CustomView.TetrisBeaker;
import com.farbox.androidbyeleven.CustomView.TetrisSquare;
import com.farbox.androidbyeleven.Util._Log;

public class MainActivity extends Activity implements View.OnClickListener, TetrisBeaker.InitOverLinstener {

    private LinearLayout ll;
    private FrameLayout fl_viewGroup;
    private TetrisSquare showTetris, moveTetris;
    private Thread gameThread;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        this.init();
        initTH();
    }

    private void init() {
        findViewById(R.id.btn_sp).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.btn_crateMyView).setOnClickListener(this);
        TetrisBeaker.tetrisBeakerInstance = (TetrisBeaker) findViewById(R.id.tetrisBeaker);
        TetrisBeaker.tetrisBeakerInstance.addInitOverLinstener(this);

        this.ll = (LinearLayout) findViewById(R.id.ll);
        this.fl_viewGroup = (FrameLayout) findViewById(R.id.fl_viewGroup);
//        Tetris Beaker.tetrisBeakerInstance.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        _Log.e(_Log.msg() + "onGlobalLayout");
//                        test();
//                    }
//                });
//        Tetris Beaker.
//        tetrisBeakerInstance.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
//            @Override
//            public void onDraw() {
//                // TODO Auto-generated method stub
//                _Log.e(_Log.msg() + "onGlobalLayout");
//                test();
//            }
//        });
    }

    private void initTH() {
        if (gameThread == null) {
            gameThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (isAlive()) {
                            Thread.sleep(500);
                            switch (TetrisSquare.state) {
                                case move_ing:
                                    TetrisSquare.getMoveTetris().move(TetrisSquare.Move.bottom);
                                    break;
                                case createTetris_ok:
                                    Message message = handler.obtainMessage();
                                    message.what = 1;
                                    handler.sendMessage(message);
//                                    _Log.d(_Log.msg() + "发送新建命令-----发送新建命令------发送新建命令");
                                    break;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
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
                            test();
                            TetrisSquare.state = TetrisSquare.UiThreadState.move_ing;
                        default:
                            super.handleMessage(msg);
                    }
                }
            };
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sp:
                test();
                this.gameThread.start();
                break;
            case R.id.btn_stop:
                startActivity(new Intent(this, ActivityClassic_1.class));
                break;
            case R.id.btn_test:
                test();
                break;
            case R.id.btn_crateMyView:
                TetrisSquare.getMoveTetris().pastTetris();
                TetrisBeaker.tetrisBeakerInstance.invalidate();
                break;
        }
    }

    /**
     * 创建新的Tetris方块，展示方块变成移动方块，并且重新创建展示方块
     */
    private void test() {
        if (this.showTetris == null) {
            this.showTetris = TetrisSquare.getShowInstance(this);
            this.ll.addView(this.showTetris);
            return;
        }
        if (this.moveTetris != null) {
            this.fl_viewGroup.removeView(this.moveTetris);
        }
        this.ll.removeView(this.showTetris);
        this.moveTetris = TetrisSquare.getMoveInstance(this);
        this.showTetris = TetrisSquare.getShowTetris();
        this.ll.addView(this.showTetris);
        //region 置为0
        this.moveTetris.setTop(0);
        this.moveTetris.setRight(0);
        this.moveTetris.setBottom(0);
        this.moveTetris.setLeft(0);
        //endregion 置为0
        this.moveTetris.setX(this.moveTetris.getmPointPIX().x);
        this.moveTetris.setY(this.moveTetris.getmPointPIX().y);
        this.fl_viewGroup.addView(this.moveTetris);
    }

    @Override
    public void viewInitOver() {
        test();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        _Log.e(_Log.msg() + "onMeasure" + String.format("[%d,%d]", TetrisBeaker.tetrisBeakerInstance.getWidth(), TetrisBeaker.tetrisBeakerInstance.getHeight()));
    }
}

