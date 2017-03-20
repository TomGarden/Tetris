package com.farbox.androidbyeleven.Controller.Control;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Model.RunModel.HiScore;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.Beaker;
import com.farbox.androidbyeleven.View.Weight.TetrisMove;
import com.farbox.androidbyeleven.View.Weight.TetrisShow;

/**
 * describe:
 * time: 2017/3/9 16:26
 * email: tom.work@foxmail.com
 */
public class MHandler extends Handler {
    private ITetrisMoveInteractiveService serverInteractive = null;
    private Beaker beaker = null;
    private TextView hiScore = null;


    public MHandler(Beaker beaker, TextView hiScore, ITetrisMoveInteractiveService serverInteractive) {
        this.serverInteractive = serverInteractive;
        this.beaker = beaker;
        this.hiScore = hiScore;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        GameState reason = (GameState) bundle.get(Global.msgReason);
        GameState result = (GameState) bundle.get(Global.msgResult);

        switch (reason) {
            case ready:
                TetrisShow.getInstance().refreshTetris();
                TetrisMove.getInstance().refreshTetris();
                Global.gameState = result;
                break;
            case moving:
                if (result == null) {
                    TetrisMove.getInstance().refreshTetris();
                } else {
                    LogUtil.i(LogUtil.msg() + "收到粘贴消息");

                    Global.gameState = GameState.pastTetrisIng;

                    Point eliminateData = this.serverInteractive.tetrisPast2BeakerMatris();
                    TetrisMove.getInstance().refreshTetris();
                    this.beaker.invalidate();
                    Global.gameState = GameState.pastTetrisOk;
                    if (eliminateData == null) {
                        Global.setGameState(GameState.gameOver);
                        Toast.makeText(Global.applicationContext, "GameOver", Toast.LENGTH_SHORT).show();
                    } else {
                        Global.gameState = GameState.eliminating;
                        int eliminateNum = this.serverInteractive.eliminate(eliminateData.x, eliminateData.y);
                        if (eliminateNum > 0) {
                            HiScore.getInstance().eliminateNum(eliminateNum);
                            this.hiScore.setText("" + HiScore.getInstance().getScore());
                        }
                        this.beaker.invalidate();
                        //Global.gameState = GameState.eliminateOk;
                        Global.setGameState(result);
                    }
                }
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }
}
