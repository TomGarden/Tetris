package com.farbox.androidbyeleven.mtetris_20.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.androidbyeleven.mtetris_20.R;
import com.farbox.androidbyeleven.mtetris_20.Util._Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 杨铭 Created by Tom on 2016/10/24.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 这是真正的俄罗斯方块，俄罗斯方块由若干Square组成
 */

public class TetrisSquare extends View {
    /**
     * 盛放所有的7个初始化俄罗斯方块矩阵
     */
    private static List<int[][]> matrixList = new ArrayList<>();

    static {
        /** 俄罗斯方块的矩阵表示 */
//        int[] matrixTetris_0 = {1, 1, 1, 1};
//        int[][] matrixTetris_1 = {{1, 0, 0}, {1, 1, 1}};
//        int[][] matrixTetris_2 = {{0, 1, 0}, {1, 1, 1}};
//        int[][] matrixTetris_3 = {{0, 0, 1}, {1, 1, 1}};
//        int[][] matrixTetris_4 = {{1, 1}, {1, 1}};
//        int[][] matrixTetris_5 = {{1, 1, 0}, {0, 1, 1}};
//        int[][] matrixTetris_6 = {{0, 1, 1}, {1, 1, 0}};
        matrixList.add(new int[][]{{1, 1, 1, 1}});
        matrixList.add(new int[][]{{1, 0, 0}, {1, 1, 1}});
        matrixList.add(new int[][]{{0, 1, 0}, {1, 1, 1}});
        matrixList.add(new int[][]{{0, 0, 1}, {1, 1, 1}});
        matrixList.add(new int[][]{{1, 1}, {1, 1}});
        matrixList.add(new int[][]{{1, 1, 0}, {0, 1, 1}});
        matrixList.add(new int[][]{{0, 1, 1}, {1, 1, 0}});
    }

    /**
     * 当前正在被代码操作的俄罗斯方块矩阵
     */
    private int[][] currentMatrixTetris;
    /**
     * 4个默认的方向
     */
    private final int directionTop = 0, directionRight = 1, directionBottom = 2, directionLeft = 3;
    /**
     * 这个程序中一共有4个方向
     */
    private final int totalDirection = 4;
    /**
     * 俄罗斯方块方向我们认为他是一个抽象量，即：初始俄罗斯方块的方向为0，每一个俄罗斯方块按顺时针方向共有 0,1,2,3 这4个方向
     * 也能理解为上，右，下，左这四个方向
     */
    private int direction;

    private TetrisAttribute tetrisAttr = TetrisAttribute.getSingleInstance();
    private Paint mPaint;

    public TetrisSquare(Context context) {
        this(context, null);
    }

    public TetrisSquare(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TetrisSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initTetris();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        int sideAddSpace = tetrisAttr.getSquareSide_PIX() + tetrisAttr.getSquareSpace_PIX() * 2;
        width = this.currentMatrixTetris[0].length * sideAddSpace;
        height = this.currentMatrixTetris.length * sideAddSpace;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawClassic(canvas);
    }

    /**
     * 从7个初始块和4个方向这两个数据中，初始化一个俄罗斯方块
     */
    private void initTetris() {
        _Log.e(_Log.msg() + "这里的旋转功能还需要测试。");
        Random random = new Random();
//        this.currentMatrixTetris = matrixList.get(random.nextInt(matrixList.size()));
//        this.direction = random.nextInt(totalDirection);
        this.currentMatrixTetris = matrixList.get(0);
        this.direction = 1;
        this.printArray(currentMatrixTetris);
        _Log.e(_Log.msg() + "tetris direction is:" + this.direction);
        switch (this.direction) {
            case directionTop:
                break;
            case directionRight:
                this.matrixTransform_Clockwise();
                break;
            case directionBottom:
                this.matrixTransform_Clockwise();
                this.matrixTransform_Clockwise();
                break;
            case directionLeft:
                this.matrixTransrorm_AntiClockwise();
        }
        this.printArray(currentMatrixTetris);
    }

    /**
     * 顺时针矩阵旋转[1次]这里操作的是currentMatrixTetris
     */
    private void matrixTransform_Clockwise() {
        /**转换后的俄罗斯方块矩阵*/
        int[][] result = new int[currentMatrixTetris[0].length][currentMatrixTetris.length];

        /*newH 代表新生成数组的行索引 oldV代表旧数组的列索引*/
        for (int newH = 0, oldV = 0; newH < result.length && oldV < currentMatrixTetris[0].length; newH++, oldV++) {//旧列值给新行
            for (int newV = 0, oldH = currentMatrixTetris.length - 1; newV < currentMatrixTetris.length && oldH >= 0; newV++, oldH--) {//旧行值给新列
                result[newH][newV] = currentMatrixTetris[oldH][oldV];
            }
        }
        currentMatrixTetris = result;
    }

    /**
     * 逆时针矩阵旋转[1次数]这里操作的是currentMatrixTetris
     */
    private void matrixTransrorm_AntiClockwise() {
        int[][] result = new int[currentMatrixTetris[0].length][currentMatrixTetris.length];

        /*newH 代表新生成数组的行索引 oldV代表旧数组的列索引*/
        for (int newH = 0, oldV = currentMatrixTetris[0].length - 1; newH < result.length && oldV >= 0; newH++, oldV--) {
            for (int newV = 0, oldH = 0; newV < result[0].length && oldH < currentMatrixTetris.length; newV++, oldH++) {
                result[newH][newV] = currentMatrixTetris[oldH][oldV];
            }
        }
        currentMatrixTetris = result;
    }

    /**
     * 绘制经典俄罗斯方块
     */
    private void drawClassic(Canvas canvas) {
        int strokeWidth = 4;

        canvas.drawColor(getResources().getColor(R.color.colorAccent));
//        canvas.drawColor(Color.WHITE);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(strokeWidth);

        {
            _Log.i(_Log.msg() + "SquareSide=" + tetrisAttr.getSquareSide_PIX());
            _Log.i(_Log.msg() + "SquareSpace=" + tetrisAttr.getSquareSpace_PIX());
            _Log.i(_Log.msg() + "SideAdd2Space=" + tetrisAttr.getSideAddSpace());
        }
        _Log.e(_Log.msg() + _Log.likeCoordinate("[getWidth,getHeight]", getWidth(), getHeight()));
        int left, top, right, bottom;
        for (int h = 0; h < currentMatrixTetris.length; h++) {
            top = tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX() + strokeWidth / 2;
            bottom = tetrisAttr.getSideAddSpace() * (h + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth / 2;
            for (int v = 0; v < currentMatrixTetris[0].length; v++) {
                if (currentMatrixTetris[h][v] == 0) {
                    continue;
                }
                _Log.e(_Log.msg() + "没有打印");
                left = tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX() + strokeWidth / 2;
                right = tetrisAttr.getSideAddSpace() * (v + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth / 2;
                //在下方一个一个绘制
                //外圈
                _Log.v(_Log.msg() + "left= " + left + "  top= " + top + "  right= " + right + "  bottom= " + bottom);
                canvas.drawRect(left, top, right, bottom, mPaint);
                //内心
                /**外圈和实心的之间的环形距离*/
                int space = 8;
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(left + space, top + space, right - space, bottom - space, mPaint);
                this.mPaint.setStyle(Paint.Style.STROKE);
            }
        }
    }

    private void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(" " + array[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 判断num是否为偶数
     *
     * @param num
     * @return true num为偶数，false num不是偶数
     */
    private boolean isEven(int num) {
        if ((num & 1) == 0) {
            return true;
        } else {
            return false;
        }
    }
}
