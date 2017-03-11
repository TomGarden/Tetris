package com.farbox.androidbyeleven.View.Weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.farbox.androidbyeleven.Controller.Control.GameState;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveGetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveInteractiveService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisMoveSetterService;
import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.View.Weight.SuperClass.TetrisSquare;

/**
 * Created by Tom on 2016/10/24.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 这是真正的俄罗斯方块，俄罗斯方块由若干Square组成
 * <p>
 * 本控件的作用就是显示一个矩阵，为了达到这个目的需要两个参数[数组，位置]
 */
public class TetrisMove extends TetrisSquare {
    private static ITetrisMoveGetterService serverGetter;
    private static ITetrisMoveSetterService serverSetter;
    private static ITetrisMoveInteractiveService serverInteractive;

    //region 单例 getInstance();
    private static volatile TetrisMove instance = null;

    /**
     * 首次初始化调用本方法
     *
     * @return
     */
    public static TetrisMove getInstance(ITetrisMoveGetterService serverGetter, ITetrisMoveSetterService serverSetter, ITetrisMoveInteractiveService serverInteractive) {
        if (instance == null) {
            synchronized (TetrisMove.class) {
                if (instance == null || serverSetter == null || serverGetter == null || serverInteractive == null) {
                    instance = new TetrisMove(Global.applicationContext, serverGetter, serverSetter, serverInteractive);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化一次之后就调用本方法
     */
    public static TetrisMove getInstance() {
        if (instance == null || serverSetter == null || serverGetter == null || serverInteractive == null) {
            throw new RuntimeException("本对象尚未初始化。");
        }
        return instance;
    }

    //endregion

    private TetrisMove(Context context, ITetrisMoveGetterService serverGetter, ITetrisMoveSetterService serverSetter, ITetrisMoveInteractiveService serverInteractive) {
        super(context);
        this.serverGetter = serverGetter;
        this.serverSetter = serverSetter;
        this.serverInteractive = serverInteractive;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[][] currentMatrix = this.serverGetter.getCurrentMatrix();
        int width, height;
        if (currentMatrix != null) {
            width = currentMatrix[0].length * serverGetter.getSideAddSpacePix();
            height = currentMatrix.length * serverGetter.getSideAddSpacePix();
        } else {
            width = 0;
            height = 0;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int[][] currentMatrix = this.serverGetter.getCurrentMatrix();
        if (currentMatrix == null) {
            return;
        }
        canvas.drawColor(getResources().getColor(R.color.transparent));
        int left, top, right, bottom;
        for (int h = 0; h < currentMatrix.length; h++) {
            for (int v = 0; v < currentMatrix[0].length; v++) {
                if (currentMatrix[h][v] == 0) {
                    continue;
                }
                top = serverGetter.getSideAddSpacePix() * h + serverGetter.getHalfSquareSpacePix() + serverGetter.getHalfTetrisLineWidthPix();
                bottom = serverGetter.getSideAddSpacePix() * (h + 1) - serverGetter.getHalfSquareSpacePix() - serverGetter.getHalfTetrisLineWidthPix();
                left = serverGetter.getSideAddSpacePix() * v + serverGetter.getHalfSquareSpacePix() + serverGetter.getHalfTetrisLineWidthPix();
                right = serverGetter.getSideAddSpacePix() * (v + 1) - serverGetter.getHalfSquareSpacePix() - serverGetter.getHalfTetrisLineWidthPix();
                super.draw1Square(canvas, Color.BLACK, serverGetter.getTetrisLineWidthPix(), left, top, right, bottom);
            }
        }
    }

    public void setCurrentMatrix(int[][] currentMatrix) {
        this.serverSetter.setCurrentMatrix(currentMatrix);
    }

    /**
     * 刷新控件位置
     */
    public void refreshPosition() {
        Point position = serverGetter.getTetrisInBeakerPosI();
        if (position != null) {
            this.setX(position.x);
            this.setY(position.y + this.getTop());
        }
        //当粘贴完成后尚未完成新的交接之前，matrix和position都是null
    }

    /**
     * 刷新控件的大小
     * <p>
     * 注：当粘贴完成后尚未完成新的交接之前，matrix和position都是null，所以此时，大小设置为零
     */
    @Override
    protected void refreshSize() {
        int[][] currentMatrix = this.serverGetter.getCurrentMatrix();
        int width, height;
        if (currentMatrix != null) {
            width = currentMatrix[0].length * serverGetter.getSideAddSpacePix();
            height = currentMatrix.length * serverGetter.getSideAddSpacePix();
        } else {
            width = 0;
            height = 0;
        }
        this.setLayoutParams(new FrameLayout.LayoutParams(width, height));
    }

    /**
     * 刷新俄罗斯方块，也就是刷新俄罗斯方块数组和显示的位置
     */
    public void refreshTetris() {
        this.refreshSize();
        if (this.serverGetter.getCurrentMatrix() != null) {
            /*当粘贴完成后尚未完成新的交接之前，matrix和position都是null，所以此时，大小设置为零
            这种时候计算position是会发生异常的，我们在这里过滤掉这个可能发生的异常
             */
            this.refreshPosition();
        }
        if (Global.getGameState() != GameState.gameOver) {
            this.invalidate();
        } else {
            Toast.makeText(Global.applicationContext, "GameOver", Toast.LENGTH_LONG).show();
        }
    }
}
