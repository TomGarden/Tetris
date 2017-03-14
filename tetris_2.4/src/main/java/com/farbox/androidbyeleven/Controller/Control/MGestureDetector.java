package com.farbox.androidbyeleven.Controller.Control;

import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.farbox.androidbyeleven.Controller.Control.GameState;
import com.farbox.androidbyeleven.Controller.Control.MoveDirection;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveGetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Model.HiScore;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.View.Weight.Beaker;
import com.farbox.androidbyeleven.View.Weight.TetrisMove;

/**
 * Created by kys_8 on 16/9/8,0008. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 * <p>
 * 简单实现接口完成简单逻辑的手势操作的监听，这个在后期是不能满足特殊需求的，当然以后再说
 * <p>
 * 在实现OnGestureListener接口的类中，如果 ovrride onDown() 方法返回false onScroll不能触发。[还不能知道]
 */
public class MGestureDetector implements GestureDetector.OnGestureListener {

    private ITetrisMoveGetterService serverGetter;
    private ITetrisMoveInteractiveService serverInteractive;
    private Beaker beaker;
    private TextView hiScore;

    /**
     * 灵敏度
     */
    private final int sensitivity = 2;
    /**
     * 累计手指滑动的像素数为移动作指导
     */
    private float left = 0, right = 0, bottom = 0, top = 0;

    public MGestureDetector(Beaker beaker, TextView hiScore, ITetrisMoveGetterService serverGetter, ITetrisMoveInteractiveService serverInteractive) {
        this.beaker = beaker;
        this.serverGetter = serverGetter;
        this.serverInteractive = serverInteractive;
        this.hiScore = hiScore;
    }


    //region GestureDetector.OnGestureListener
    @Override

    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * 解释：是在touchdown后又没有滑动（onScroll），又没有长按（onLongPress），然后Touchup时触发。
     * 点击一下非常快的（不滑动）Touchup：
     * 　　onDown->onSingleTapUp->onSingleTapConfirmed
     * 点击一下稍微慢点的（不滑动）Touchup：
     * 　　onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
     * <p/>
     * // 短按ACTION_DOWN、ACTION_UP
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (this.serverInteractive.moveTo(MoveDirection.rotate)) {
            TetrisMove.getInstance().refreshTetris();
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceX / distanceY) > sensitivity) {
            if (distanceX > 0) {//left
                this.left += distanceX;
                if (this.left >= this.serverGetter.getSideAddSpacePix()) {
                    if (this.serverInteractive.moveTo(MoveDirection.left)) {
                        TetrisMove.getInstance().refreshTetris();
                    }
                    this.left -= this.serverGetter.getSideAddSpacePix();
                }
            } else if (distanceX < 0) {//right
                this.right -= distanceX;
                if (this.right >= this.serverGetter.getSideAddSpacePix()) {
                    if (this.serverInteractive.moveTo(MoveDirection.right)) {
                        TetrisMove.getInstance().refreshTetris();
                    }
                    this.right -= this.serverGetter.getSideAddSpacePix();
                }
            }
        } else if (Math.abs(distanceY / distanceX) > sensitivity) {
            if (distanceY > 0) {//top
                this.top += distanceY;
                if (this.top >= this.serverGetter.getSideAddSpacePix()) {
                    if (this.serverInteractive.moveTo(MoveDirection.top)) {
                        TetrisMove.getInstance().refreshTetris();
                    }
                    this.top -= this.serverGetter.getSideAddSpacePix();
                }
            } else if (distanceY < 0) {//down,控制下移的入口有两个，一个是线程，一个是手动
                this.bottom -= distanceY;
                if (this.bottom >= this.serverGetter.getSideAddSpacePix()) {
                    if (this.serverInteractive.moveTo(MoveDirection.bottom)) {
                        TetrisMove.getInstance().refreshTetris();
                    } else {
                        Global.setGameState(GameState.pastTetrisIng);
                        Point eliminateData = this.serverInteractive.tetrisPast2BeakerMatris();
                        this.beaker.invalidate();
                        TetrisMove.getInstance().refreshTetris();
                        Global.setGameState(GameState.pastTetrisOk);
                        if (eliminateData == null) {
                            Global.setGameState(GameState.gameOver);
                            Toast.makeText(Global.applicationContext, "GameOver", Toast.LENGTH_LONG).show();
                        } else {
                            Global.setGameState(GameState.eliminating);
                            int eliminateNum = this.serverInteractive.eliminate(eliminateData.x, eliminateData.y);
                            if (eliminateNum > 0) {
                                HiScore.getInstance().eliminateNum(eliminateNum);
                                this.hiScore.setText(""+HiScore.getInstance().getScore());
                            }
                            this.beaker.invalidate();
                            Global.setGameState(GameState.eliminateOk);
                            Global.setGameState(GameState.ready);
                        }
                    }
                    this.bottom -= this.serverGetter.getSideAddSpacePix();
                }
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 快速滑动
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    //endregion
}