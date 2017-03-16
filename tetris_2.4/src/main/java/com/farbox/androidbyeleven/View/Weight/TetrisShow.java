package com.farbox.androidbyeleven.View.Weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowGetterService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowInteractiveService;
import com.farbox.androidbyeleven.Controller.V2M.ITetrisShowSetterService;
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
public class TetrisShow extends TetrisSquare {
    private static ITetrisShowGetterService serverGetter;
    private static ITetrisShowSetterService serverSetter;
    private static ITetrisShowInteractiveService serverInteractive;

    private TetrisShow(Context context, ITetrisShowGetterService serverGetter, ITetrisShowSetterService serverSetter, ITetrisShowInteractiveService serverInteractive) {
        super(context);
        this.serverGetter = serverGetter;
        this.serverInteractive = serverInteractive;
    }

    //region 单例 getInstance();
    private static volatile TetrisShow instance = null;

    /**
     * 首次初始化调用本方法
     *
     * @param server
     * @return
     */
    public static TetrisShow getInstance(ITetrisShowGetterService serverGetter, ITetrisShowSetterService serverSetter, ITetrisShowInteractiveService serverInteractive) {
        if (instance == null) {
            synchronized (TetrisShow.class) {
                if (serverInteractive == null || serverGetter == null || instance == null) {
                    instance = new TetrisShow(Global.applicationContext, serverGetter, serverSetter, serverInteractive);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化一次之后就调用本方法
     */
    public static TetrisShow getInstance() {
        if (serverInteractive == null || serverGetter == null || instance == null) {
            throw new RuntimeException("本对象尚未初始化。");
        }
        return instance;
    }

    //endregion

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Size size = this.getTetrisSize();
        setMeasuredDimension(size.getWidth(), size.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.transparent));
        int left, top, right, bottom;
        for (int h = 0; h < serverGetter.getCurrentMatrix().length; h++) {
            for (int v = 0; v < serverGetter.getCurrentMatrix()[0].length; v++) {
                if (serverGetter.getCurrentMatrix()[h][v] == 0) {
                    continue;
                }
                top = serverGetter.getSideAddSpacePix() * h + serverGetter.getHalfSquareSpacePix() + serverGetter.getHalfTetrisLineWidthPix();
                bottom = serverGetter.getSideAddSpacePix() * (h + 1) - serverGetter.getHalfSquareSpacePix() - serverGetter.getHalfTetrisLineWidthPix();
                left = serverGetter.getSideAddSpacePix() * v + serverGetter.getHalfSquareSpacePix() + serverGetter.getHalfTetrisLineWidthPix();
                right = serverGetter.getSideAddSpacePix() * (v + 1) - serverGetter.getHalfSquareSpacePix() - serverGetter.getHalfTetrisLineWidthPix();
                super.draw1Square(canvas, Color.GRAY, serverGetter.getTetrisLineWidthPix(), left, top, right, bottom);
            }
        }
    }

    /**
     * 初始化控件的大小
     */
    protected void refreshSize() {
        //先刷新显示内容
        this.serverInteractive.refreshTetris();
        //刷新显示内容的尺寸
        Size size = this.getTetrisSize();
        this.setLayoutParams(new LinearLayout.LayoutParams(size.getWidth(), size.getHeight()));
    }

    /**
     * 刷新俄罗斯方块，也就是刷新俄罗斯方块数组和显示的位置
     */
    public void refreshTetris() {
        this.refreshSize();
        this.invalidate();
    }

    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    @Override
    public String getMatris2Str() {
        return serverGetter.getMatris2Str();
    }

    /**
     * 获取当前正在显示的矩阵
     *
     * @return
     */
    public int[][] getCurrentMatrix() {
        return serverGetter.getCurrentMatrix();
    }

    private Size getTetrisSize() {
        Size size = new Size();
        size.setWidth(serverGetter.getCurrentMatrix()[0].length * serverGetter.getSideAddSpacePix());
        size.setHeight(5 * serverGetter.getSideAddSpacePix());
        return size;
    }

    public void setCurrentMatrix(int[][] matrix) {
        this.serverSetter.setCurrentMatrix(matrix);
    }
}