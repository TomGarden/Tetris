package com.farbox.androidbyeleven.View.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.farbox.androidbyeleven.Controller.Control.GameState;
import com.farbox.androidbyeleven.Controller.Control.GameThread;
import com.farbox.androidbyeleven.Controller.Control.MHandler;
import com.farbox.androidbyeleven.Controller.M2V.impl.BeakerNotify;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveGetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveSetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowGetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowSetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.BeakerService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveGetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisMoveSetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisShowGetterService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisShowInteractiveService;
import com.farbox.androidbyeleven.Controller.V2M.impl.TetrisShowSetterService;
import com.farbox.androidbyeleven.Model.LocalizeModel.xml.impl.GameProgress;
import com.farbox.androidbyeleven.Model.RunModel.HiScore;
import com.farbox.androidbyeleven.Model.RunModel.Impl.BeakerModel;
import com.farbox.androidbyeleven.Model.RunModel.Impl.TetrisMoveModel;
import com.farbox.androidbyeleven.Model.RunModel.Impl.TetrisShowModel;
import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.Beaker;
import com.farbox.androidbyeleven.View.Weight.MyTableRow;
import com.farbox.androidbyeleven.View.Weight.TetrisMove;
import com.farbox.androidbyeleven.View.Weight.TetrisShow;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Beaker beaker = null;
    private LinearLayout llNextSquare = null;
    private FrameLayout frameLayout = null;
    private Switch mSwitch = null;
    private MyTableRow myTableRow = null;
    private TextView hiScore = null;
    private TextView level = null;
    private ImageView ibMenu = null;

    private final IntentFilter intentFilter = new IntentFilter();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF://关闭屏幕
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS://长按电源键出现关机选项
                    play2Pause();
                    break;
                case Intent.ACTION_SCREEN_ON:
                    LogUtil.i(LogUtil.msg() + "ACTION_SCREEN_ON");
                    break;
                case Intent.ACTION_USER_PRESENT://
                    LogUtil.i(LogUtil.msg() + "ACTION_USER_PRESENT");
                    break;
            }
        }
    };

    /**
     * 设置即将加载的布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeView() {
        super.beforeView();
        // 屏幕灭屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        // 屏幕亮屏广播
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        this.mSwitch = (Switch) findViewById(R.id.switchBtn);
        this.mSwitch.setOnCheckedChangeListener(this);
        this.llNextSquare = (LinearLayout) findViewById(R.id.ll_nextSquare);
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_viewGroup);
        this.myTableRow = (MyTableRow) findViewById(R.id.mTableRow);
        this.hiScore = (TextView) findViewById(R.id.tv_score);
        this.hiScore.setText("" + HiScore.getInstance().getScore());
        this.level = (TextView) findViewById(R.id.tv_level);
        this.ibMenu = (ImageView) findViewById(R.id.ib_menu);

        this.level.setOnClickListener(this);
        this.ibMenu.setOnClickListener(this);
        findViewById(R.id.tv_level_title).setOnClickListener(this);

        findViewById(R.id.tv_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(LogUtil.msg() + "test");
            }
        });
        findViewById(R.id.tv_next_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(LogUtil.msg() + "test22222222");
            }
        });
        findViewById(R.id.tv_score_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(LogUtil.msg() + "rootHei =" + findViewById(R.id.ll_root).getHeight());
                LogUtil.i(LogUtil.msg() + "titleHei =" + findViewById(R.id.ll_title).getHeight());
                LogUtil.i(LogUtil.msg() + "imageHei =" + findViewById(R.id.ib_menu).getHeight());
                LogUtil.i(LogUtil.msg() + "myTableRow =" + myTableRow.getHeight());
            }
        });
    }

    @Override
    protected void initData() {
        //①连接Beaker、Controller、Model三者。
        this.beaker = (Beaker) findViewById(R.id.tetrisBeaker);
        BeakerNotify beakerNotify = new BeakerNotify(this.beaker);
        BeakerModel beakerModel = BeakerModel.getInstance(beakerNotify);//这里必须用带参数的获取
        BeakerService beakerService = new BeakerService(beakerModel, beakerModel);
        this.beaker.setService(beakerService);//设置服务员
        this.beaker.setServerCondition(beakerService);//设置服务员
        //②连接showTetris、Controller、Model三者。
        TetrisShowModel tetrisShowModel = new TetrisShowModel();
        ITetrisShowGetterService tetrisShowGetterService = new TetrisShowGetterService(tetrisShowModel);
        ITetrisShowSetterService tetrisShowSetterService = new TetrisShowSetterService(tetrisShowModel);
        TetrisShowInteractiveService serverInteractive = new TetrisShowInteractiveService(tetrisShowModel);
        this.llNextSquare.addView(TetrisShow.getInstance(tetrisShowGetterService, tetrisShowSetterService, serverInteractive));
        //③连接moveTetris、Controller、Model三者
        TetrisMoveModel tetrisMoveModel = new TetrisMoveModel();
        ITetrisMoveGetterService tetrisMoveGetterService = new TetrisMoveGetterService(tetrisMoveModel);
        ITetrisMoveSetterService tetrisMoveSetterService = new TetrisMoveSetterService(tetrisMoveModel);
        TetrisMoveInteractiveService tetrisMoveInteractiveService = new TetrisMoveInteractiveService(tetrisMoveModel);
        TetrisMove tetrisMove = TetrisMove.getInstance(tetrisMoveGetterService, tetrisMoveSetterService, tetrisMoveInteractiveService);
        this.frameLayout.addView(tetrisMove);
        //④连接Gesture、Controller、Model三者
        this.myTableRow.setParam(tetrisMoveGetterService, tetrisMoveInteractiveService, this.beaker, this.hiScore);
        //⑤拿到游戏进度管理对象
        GameProgress ReadWirteGameProgress = new GameProgress(tetrisShowModel, tetrisMoveModel);
        //⑥先初始化一次Thread,免得以后调用麻烦。
        GameThread.getInstance(new MHandler(this.beaker, this.hiScore, tetrisMoveInteractiveService), tetrisMoveInteractiveService, ReadWirteGameProgress);
        this.level.setText("" + GameThread.getInstance().getLevel());
        //⑦读取上次游戏进度
        this.readLastProgress();
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (Global.getGameState()) {
            case readProgress:
            case ready:
                GameThread.getInstance().ready2Play();
                break;
            case noticeGameWait:
                GameThread.getInstance().pause2Play();
                break;
            case moving:
                GameThread.getInstance().paly2Pause();
                break;
            default:
                LogUtil.e(LogUtil.msg() + Global.tipNotOperate);
                break;
        }
    }

    private void play2Pause() {
        if (this.mSwitch.isChecked()) {
            this.mSwitch.setChecked(false);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_level:
            case R.id.tv_level_title:
                GameThread.getInstance().changeLevel();
                this.level.setText("" + GameThread.getInstance().getLevel());
                break;

            case R.id.ib_menu://菜单按钮
                LogUtil.i(LogUtil.msg() + this.ibMenu.getHeight());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.play2Pause();//先暂停游戏
          /*
          这里使用了 android.support.v7.app.AlertDialog.Builder
          可以直接在头部写 import android.support.v7.app.AlertDialog
          那么下面就可以写成 AlertDialog.Builder
          */
        DialogClickListener listener = new DialogClickListener();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("确认退出");
        builder.setMessage("已保存本次进度。");
        builder.setNegativeButton("取消", listener);
        builder.setPositiveButton("确定", listener);
        builder.show();
        //super.onBackPressed();
    }

    private class DialogClickListener implements DialogInterface.OnClickListener {
        /**
         * This method will be invoked when a button in the dialog is clicked.
         *
         * @param dialog The dialog that received the click.
         * @param which  The button that was clicked (e.g.
         *               {@link DialogInterface#BUTTON1}) or the position
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE://确认
                    unregisterReceiver(broadcastReceiver);
                    finish();
                    System.exit(0);
                    break;
                case DialogInterface.BUTTON_NEGATIVE://取消
                    break;
            }
        }
    }

    private void readLastProgress() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("请问");
        builder.setMessage("您是否继续上次进度");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Global.setGameState(GameState.readProgress);
                if (!GameThread.getInstance().readGameProgress()) {
                    Toast.makeText(Global.applicationContext, "发生事故,无法恢复,期待您的反馈。", Toast.LENGTH_SHORT).show();
                    Global.setGameState(GameState.ready);
                }
                TetrisShow.getInstance().refreshTetris();
                TetrisMove.getInstance().refreshTetris();
                beaker.invalidate();
                hiScore.setText("" + HiScore.getInstance().getScore());
                level.setText("" + GameThread.getInstance().getLevel());

            }
        });
        builder.show();
    }
}
