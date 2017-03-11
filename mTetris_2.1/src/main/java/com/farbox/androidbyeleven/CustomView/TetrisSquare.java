package com.farbox.androidbyeleven.CustomView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;


import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.Util._Log;

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
     * 用于游戏移动的俄罗斯方块
     */
    private static TetrisSquare showTetrisSquare = null;
    /**
     * 用于显示下一个俄罗斯方块
     */
    private static TetrisSquare moveTetrisSquare = null;
    /**
     * 盛放所有的7个初始化俄罗斯方块矩阵
     */
    private static List<int[][]> matrixList = new ArrayList<>();

    static {
        /** 俄罗斯方块的矩阵表示 */
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

    /**
     * 记录本控件的当前位置
     */
    private Point mPoint = null;

    /**
     * 返回用于移动的俄罗斯方块的同时，制造新的用于展示的俄罗斯方块
     *
     * @param context
     * @return 用于移动的俄罗斯方块
     */
    public static synchronized TetrisSquare getMoveInstance(Context context) {
        if (showTetrisSquare == null) {
            throw new RuntimeException("Must call getShowInstance() first.");
        }
        showTetrisSquare.setSquareScale(1);
        _Log.e(_Log.msg() + "getMoveInstance中showTetrisSquare.showScale()=");
        showTetrisSquare.showScale();
        moveTetrisSquare = showTetrisSquare;
        _Log.e(_Log.msg() + "getMoveInstance中shou转移给Move之后的比例：");
        moveTetrisSquare.showScale();

        getShowInstance(context);

        return moveTetrisSquare;
    }

    /**
     * 单纯返回供展示的俄罗斯方块
     */
    public static synchronized TetrisSquare getShowInstance(Context context) {
        showTetrisSquare = new TetrisSquare(context);
        showTetrisSquare.setSquareScale(0.5f);
        return showTetrisSquare;
    }


    private TetrisSquare(Context context) {
        this(context, null);
    }

    private TetrisSquare(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private TetrisSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initTetris();
        this.initPoint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        _Log.e(_Log.msg() + "onMeasure");
        setMeasuredDimension(this.myGetWidth(), this.myGetHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _Log.e(_Log.msg() + "onDraw");
        drawClassic(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        _Log.e(_Log.msg() + "调用了：onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 自定义获取控件宽度，和原方法的getWidth相同，但是如果代码修改后就不一定了，注意修改
     */
    public int myGetWidth() {
        if (currentMatrixTetris == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return this.currentMatrixTetris[0].length * tetrisAttr.getSideAddSpace();
    }

    /**
     * 自定义获取控件高度，和原方法的getWidth相同，但是如果代码修改后就不一定了，注意修改
     */
    public int myGetHeight() {
        if (currentMatrixTetris == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return this.currentMatrixTetris.length * tetrisAttr.getSideAddSpace();
    }

    /**
     * 从7个初始块和4个方向这两个数据中，初始化一个俄罗斯方块
     */
    private void initTetris() {
        _Log.e(_Log.msg() + "这里的旋转功能还需要测试。");
        Random random = new Random();
        this.currentMatrixTetris = matrixList.get(random.nextInt(matrixList.size()));
        this.direction = random.nextInt(totalDirection);
        this.printArray(currentMatrixTetris);
        _Log.i(_Log.msg() + "tetris direction is:" + this.direction);
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
     * 绘制经典  俄罗斯方块
     */
    private void drawClassic(Canvas canvas) {
        int strokeWidth = tetrisAttr.getSquareSide_PIX() / 8;//笔触宽度
        strokeWidth = tetrisAttr.getStrockWith_Pix(strokeWidth);//用像素表示的笔触宽度
        /**这是经典块中间那个环的宽度*/
        int ringWidth = tetrisAttr.getSquareSide_PIX() / 4;
        _Log.i(_Log.msg() + "环的宽度=" + ringWidth);

        canvas.drawColor(getResources().getColor(R.color.red));
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(strokeWidth);

        {
            _Log.i(_Log.msg() + "MarginHorizontal=" + tetrisAttr.getMarginHorizontal());
            _Log.i(_Log.msg() + "MarginVertical=" + tetrisAttr.getMarginVertical());
            _Log.i(_Log.msg() + "SquareSide=" + tetrisAttr.getSquareSide_PIX());
            _Log.i(_Log.msg() + "SquareSpace=" + tetrisAttr.getSquareSpace_PIX());
            _Log.i(_Log.msg() + "SideAdd2Space=" + tetrisAttr.getSideAddSpace());
        }
        int left, top, right, bottom;
        for (int h = 0; h < currentMatrixTetris.length; h++) {
            top = tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX() + strokeWidth / 2;
            bottom = tetrisAttr.getSideAddSpace() * (h + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth / 2;
            for (int v = 0; v < currentMatrixTetris[0].length; v++) {
                if (currentMatrixTetris[h][v] == 0) {
                    continue;
                }
                left = tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX() + strokeWidth / 2;
                right = tetrisAttr.getSideAddSpace() * (v + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth / 2;
                //在下方一个一个绘制
                //外圈
                canvas.drawRect(left, top, right, bottom, mPaint);
                //内心
                /**外圈和实心的之间的环形距离*/
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(left + ringWidth, top + ringWidth, right - ringWidth, bottom - ringWidth, mPaint);
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
     * 初始化本空间的初始位置
     * 盛放TetrisBeaker的容器和TetrisBeaker同样大。
     */
    private void initPoint() {
        //首先计算在矩阵中的位置坐标，
        int matrixPositionX = (tetrisAttr.getBeakerMatris()[0].length - currentMatrixTetris[0].length) / 2;
        //然后转换为像素坐标
        this.mPoint = new Point();
        this.mPoint.x = tetrisAttr.getMarginHorizontal() + matrixPositionX * tetrisAttr.getSideAddSpace();
        //这里是认为一旦一个TetrisSquare被new出来就已经执行了onMeasure方法了
        this.mPoint.y = tetrisAttr.getMarginVertical() + tetrisAttr.getSideAddSpace() - myGetHeight();
        _Log.i(_Log.msg() + _Log.likeCoordinate("point初始化成功[x , y]" + this.mPoint.x, this.mPoint.y));
    }

    public Point getmPoint() {
        if (mPoint == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return mPoint;
    }

    /**
     * 简介设置缩放比例
     */
    public void setSquareScale(float scale) {
        this.tetrisAttr.setSquareScale(scale);
    }

    public void showScale() {
        this.tetrisAttr.showScale();
    }
}
