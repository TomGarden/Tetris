package com.farbox.androidbyeleven.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.farbox.androidbyeleven.MainActivity;
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

    //region static final 属性
    /**
     * 用于游戏移动的俄罗斯方块
     */
    public static TetrisSquare showTetrisSquare = null;
    /**
     * 用于显示下一个俄罗斯方块
     */
    public static TetrisSquare moveTetrisSquare = null;
    /**
     * 盛放所有的7个初始化俄罗斯方块矩阵
     */
    private static List<int[][]> matrixList = new ArrayList<>();

    /**
     * UI线程当前的状态:默认处于俄罗斯方块正在移动的状态
     */
    public static UiThreadState state = UiThreadState.move_ing;

    public enum UiThreadState {
        /**
         * 正在向背景粘贴俄罗斯方块状态
         */
        pastTetris_ing,
        /**
         * UI线程处于可以创建新的俄罗斯方块的系列操作的状态
         */
        createTetris_ok,
        /**
         * UI线程处于正在创建新的俄罗斯方块的系列操作的状态
         */
        createTetris_ing,
        /**
         * 正在处于移动状态
         */
        move_ing,
        /**
         * 告诉游戏线程去wait
         */
        letSumThread_waiting
    }

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
     * 4个默认的方向
     */
    private static final int directionTop = 0, directionRight = 1, directionBottom = 2, directionLeft = 3;
    /**
     * 这个程序中一共有4个方向
     */
    private static final int totalDirection = 4;
    /**
     * 初始比例，就是不缩放情况下的比例 值为1
     */
    private static final float initialScale = 0.5f;
    //endregion

    //region 属性
    /**
     * 这是缩放比例，用来监控，俄罗斯方块的缩放;
     * 是-1表示不缩放
     */
    private float squareScale = initialScale;
    /**
     * 当前正在被代码操作的俄罗斯方块矩阵
     */
    private int[][] currentMatrixTetris;
    /**
     * 俄罗斯方块方向我们认为他是一个抽象量，即：初始俄罗斯方块的方向为0，每一个俄罗斯方块按顺时针方向共有 0,1,2,3 这4个方向
     * 也能理解为上，右，下，左这四个方向
     */
    private int direction;
    private TetrisAttribute tetrisAttr = TetrisAttribute.getSingleInstance();
    private Paint mPaint;
    /**
     * 记录本控件的当前位置[按像素] y表示 行  x 表示列
     */
    private Point mPointPIX = null;
    /**
     * 记录本控件的当前位置[按矩阵] y表示 行  x 表示列
     */
    private Point mPointMatrix = null;

    /**
     * 旋转标示
     */
    public enum Move {
        left, right, bottom, top, rotate
    }
    //endregion

    //region getTetrisSquareInstance(){

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
        moveTetrisSquare = showTetrisSquare;
        moveTetrisSquare.setSquareScale(1);//仅仅赋值是不够的
        moveTetrisSquare.refreshPointPIX();//还得刷新
        getShowInstance(context);

        return moveTetrisSquare;
    }

    /**
     * 单纯返回供展示的俄罗斯方块
     */
    public static synchronized TetrisSquare getShowInstance(Context context) {
        showTetrisSquare = new TetrisSquare(context, initialScale);
        return showTetrisSquare;
    }

    /**
     * 仅仅反馈一个已经创造好的对象
     */
    public static TetrisSquare getMoveTetris() {
        if (moveTetrisSquare == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return moveTetrisSquare;
    }

    /**
     * 仅仅反馈一个已经创造好的对象
     */
    public static TetrisSquare getShowTetris() {
        if (showTetrisSquare == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return showTetrisSquare;
    }
    //endregion

    //region private TetrisSquare(){
    private TetrisSquare(Context context, float squareScale) {
        this(context);
        this.setSquareScale(squareScale);
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
        this.initPointMatirx();
        //this.refreshPointPIX();
    }
    //endregion

    //region onMeasure  onDraw
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.myGetWidth(), this.myGetHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.squareScale == 1) {
            drawClassic(canvas);
        } else {
            drawClassic_Scale(canvas);
        }
    }
    //endregion

    //region init

    /**
     * 从7个初始块和4个方向这两个数据中，初始化一个俄罗斯方块
     */
    private void initTetris() {
        Random random = new Random();
        this.currentMatrixTetris = matrixList.get(random.nextInt(matrixList.size()));//随机获取基本俄罗斯方块的索引
        this.direction = random.nextInt(totalDirection);//随机获取俄罗斯方块是方向
        // this.printArray(currentMatrixTetris);
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
                this.matrixTransform_AntiClockwise();
        }
        //  this.printArray(currentMatrixTetris);
    }

    /**
     * 初始化本控件的初始位置
     * 盛放TetrisBeaker的容器和TetrisBeaker同样大。
     */
    private void initPointMatirx() {
        if (mPointMatrix == null) {
            mPointMatrix = new Point();
        }
        //首先计算在矩阵中的位置坐标，
        mPointMatrix.x = (tetrisAttr.getBeakerMatris()[0].length - currentMatrixTetris[0].length) / 2;
        mPointMatrix.y = 0;
        //然后转换为像素坐标
//        if (squareScale == 1) {
        this.refreshPointPIX();
    }
    //endregion

    //region matrix转换

    /**
     * 顺时针矩阵旋转[1次]这里操作的是currentMatrixTetris
     */
    public void matrixTransform_Clockwise() {
        /**转换后的俄罗斯方块矩阵*/
        int[][] result = new int[currentMatrixTetris[0].length][currentMatrixTetris.length];

        /*newH 代表新生成数组的行索引 oldV代表旧数组的列索引*/
        for (int newH = 0, oldV = 0; newH < result.length && oldV < currentMatrixTetris[0].length; newH++, oldV++) {//旧列值给新行
            for (int newV = 0, oldH = currentMatrixTetris.length - 1; newV < currentMatrixTetris.length && oldH >= 0; newV++, oldH--) {//旧行值给新列
                result[newH][newV] = currentMatrixTetris[oldH][oldV];
            }
        }
        this.fixTransform(result);
        currentMatrixTetris = result;
    }

    /**
     * 逆时针矩阵旋转[1次数]这里操作的是currentMatrixTetris
     */
    private void matrixTransform_AntiClockwise() {
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
     * 修正旋转，旋转可能造成俄罗斯方块越界风险，
     * 所以旋转之后要对当前俄罗斯方块位置做出必要的修正
     * [当前仅仅对可能超出右侧边界的情况做出修正]
     *
     * @param result 参数是旋转后的Tetris矩阵
     */
    private void fixTransform(int[][] result) {
        if (this.mPointMatrix == null) {
            return;
        }
        if (this.mPointMatrix.x + result[0].length > tetrisAttr.getBeakerMatris()[0].length) {
            this.mPointMatrix.x = tetrisAttr.getBeakerMatris()[0].length - result[0].length;
        }
    }
    //endregion

    //region Getters & Setters

    /**
     * 自定义获取控件宽度，和原方法的getWidth相同，但是如果代码修改后就不一定了，注意修改
     */
    public int myGetWidth() {
        if (currentMatrixTetris == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        if (this.squareScale == 1) {
            return this.currentMatrixTetris[0].length * tetrisAttr.getSideAddSpace();
        } else {
            return this.currentMatrixTetris[0].length * this.getSideAddSpace_Scale();
        }
    }

    /**
     * 自定义获取控件高度，和原方法的getWidth相同，但是如果代码修改后就不一定了，注意修改
     */
    public int myGetHeight() {
        if (currentMatrixTetris == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        if (this.squareScale == 1) {
            return this.currentMatrixTetris.length * tetrisAttr.getSideAddSpace();
        } else {
            return this.currentMatrixTetris.length * this.getSideAddSpace_Scale();
        }
    }

    public Point getmPointPIX() {
        if (mPointPIX == null) {
            throw new RuntimeException("字段未正常初始化异常-->这个参数还没有被我们的逻辑进行初始化");
        }
        return mPointPIX;
    }

    public void setSquareScale(float squareScale) {
        if (squareScale > 1 || squareScale < 0) {
            throw new RuntimeException("字段未正常初始化异常-->这里只接受(0,1]区间的值");
        }
        if (this.squareScale != squareScale) {
            this.squareScale = squareScale;
        }
    }

    /**
     * 获取经过缩放后的计算数值
     */
    private int getPIX(int value) {
        int result = (int) (value * this.squareScale);
        if (result > 0) {
            return result;
        } else {
            return 1;
        }
    }

    /**
     * 获取经过缩放后的计算数值 [正方形边长]
     */
    private int getSquareSide_PIX_Scale() {
        int result = getPIX(this.tetrisAttr.getSquareSide_PIX());
        if (result == 1) {
            _Log.e(_Log.msg() + "警告，这里可能会出现问题");
        }
        return result;
    }

    /**
     * 获取经过缩放后的计算数值 [正方形间距的一半]
     */
    private int getSquareSpace_PIX_Scale() {
        return getPIX(this.tetrisAttr.getSquareSpace_PIX());
    }

    /**
     * 获取经过缩放后的计算数值 [正方形边长 + half间距 * 2]
     */
    private int getSideAddSpace_Scale() {
        return this.getSquareSide_PIX_Scale() + this.getSquareSpace_PIX_Scale() * 2;
    }

    /**
     * 获取经典俄罗斯方块中间那个环的宽度
     */
    public int getRingWidth() {
        return tetrisAttr.getSquareSide_PIX() / 4;
    }

    public int[][] getCurrentMatrixTetris() {
        if (currentMatrixTetris == null) {
            throw new RuntimeException("未初始化异常，请监控");
        }
        return currentMatrixTetris;
    }

    public Point getmPointMatrix() {
        if (mPointMatrix == null) {
            throw new RuntimeException("未初始化异常，请监控");
        }
        return mPointMatrix;
    }

    //endregion

    //region  my draw

    /**
     * 绘制经典  俄罗斯方块
     */
    private void drawClassic(Canvas canvas) {
        int strokeWidth = tetrisAttr.getSquareSide_PIX() / 8;//笔触宽度
        strokeWidth = tetrisAttr.getStrockWith_Pix(strokeWidth);//用像素表示的笔触宽度
        /**这是经典块中间那个环的宽度*/
        int ringWidth = this.getRingWidth();

        canvas.drawColor(getResources().getColor(R.color.transparent));
        if (this.mPaint == null) {
            this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            this.mPaint.setColor(Color.BLACK);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(strokeWidth);
        }

        //region printf
        {
//            _Log.i(_Log.msg() + "MarginHorizontal=" + tetrisAttr.getMarginHorizontal());
//            _Log.i(_Log.msg() + "MarginVertical=" + tetrisAttr.getMarginVertical());
//            _Log.i(_Log.msg() + "SquareSide=" + tetrisAttr.getSquareSide_PIX());
//            _Log.i(_Log.msg() + "SquareSpace=" + tetrisAttr.getSquareSpace_PIX());
//            _Log.i(_Log.msg() + "SideAdd2Space=" + tetrisAttr.getSideAddSpace());
        }//endregion

        this.myDraw(canvas, strokeWidth, ringWidth);
//        int left, top, right, bottom;
//        for (int h = 0; h < currentMatrixTetris.length; h++) {
//            top = tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX() + strokeWidth / 2;
//            bottom = tetrisAttr.getSideAddSpace() * (h + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth / 2;
//            for (int v = 0; v < currentMatrixTetris[0].length; v++) {
//                if (currentMatrixTetris[h][v] == 0) {
//                    continue;
//                }
//                left = tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX() + strokeWidth / 2;
//                right = tetrisAttr.getSideAddSpace() * (v + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth / 2;
//                //绘制外圈
//                canvas.drawRect(left, top, right, bottom, mPaint);
//                //绘制内心
//                /**外圈和实心的之间的环形距离*/
//                this.mPaint.setStyle(Paint.Style.FILL);
//                canvas.drawRect(left + ringWidth, top + ringWidth, right - ringWidth, bottom - ringWidth, mPaint);
//                this.mPaint.setStyle(Paint.Style.STROKE);
//            }
//        }
    }

    private void drawClassic_Scale(Canvas canvas) {
        //笔触宽度
        int strokeWidth = this.getSquareSide_PIX_Scale() / 8;
        if (strokeWidth == 0) {
            strokeWidth = 1;
        }
        //用像素表示的笔触宽度
        strokeWidth = tetrisAttr.getStrockWith_Pix(strokeWidth);
        /**这是经典块中间那个环的宽度*/
        int ringWidth = this.getSquareSide_PIX_Scale() / 4;
        if (ringWidth == 0) {
            ringWidth = 1;
        }

        //_Log.i(_Log.msg() + "环的宽度=" + ringWidth);

        canvas.drawColor(getResources().getColor(R.color.transparent));
        if (this.mPaint == null) {
            this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            this.mPaint.setColor(Color.BLACK);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(strokeWidth);
        }

        {
//            _Log.i(_Log.msg() + "MarginHorizontal=" + tetrisAttr.getMarginHorizontal());
//            _Log.i(_Log.msg() + "MarginVertical=" + tetrisAttr.getMarginVertical());
//            _Log.i(_Log.msg() + "SquareSide=" + tetrisAttr.getSquareSide_PIX());
//            _Log.i(_Log.msg() + "SquareSpace=" + tetrisAttr.getSquareSpace_PIX());
//            _Log.i(_Log.msg() + "SideAdd2Space=" + tetrisAttr.getSideAddSpace());
        }
//        this.myDraw(canvas, strokeWidth, ringWidth);
        int left, top, right, bottom;
        for (int h = 0; h < currentMatrixTetris.length; h++) {
            top = this.getSideAddSpace_Scale() * h + this.getSquareSpace_PIX_Scale() + strokeWidth / 2;
            bottom = this.getSideAddSpace_Scale() * (h + 1) - this.getSquareSpace_PIX_Scale() - strokeWidth / 2;
            for (int v = 0; v < currentMatrixTetris[0].length; v++) {
                if (currentMatrixTetris[h][v] == 0) {
                    continue;
                }
                left = this.getSideAddSpace_Scale() * v + this.getSquareSpace_PIX_Scale() + strokeWidth / 2;
                right = this.getSideAddSpace_Scale() * (v + 1) - this.getSquareSpace_PIX_Scale() - strokeWidth / 2;
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

    /**
     * 循环获得绘制数据
     */
    private void myDraw(Canvas canvas, int strokeWidth, int ringWidth) {
        strokeWidth /= 2;
        int left, top, right, bottom;
        for (int h = 0; h < currentMatrixTetris.length; h++) {
            top = tetrisAttr.getSideAddSpace() * h + tetrisAttr.getSquareSpace_PIX() + strokeWidth;
            bottom = tetrisAttr.getSideAddSpace() * (h + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth;
            for (int v = 0; v < currentMatrixTetris[0].length; v++) {
                if (currentMatrixTetris[h][v] == 0) {
                    continue;
                }
                left = tetrisAttr.getSideAddSpace() * v + tetrisAttr.getSquareSpace_PIX() + strokeWidth;
                right = tetrisAttr.getSideAddSpace() * (v + 1) - tetrisAttr.getSquareSpace_PIX() - strokeWidth;
                draw1Square(canvas, left, top, right, bottom, ringWidth);
            }
        }
    }

    /**
     * 绘制单个正方形
     */
    public void draw1Square(Canvas canvas, int left, int top, int right, int bottom, int ringWidth) {
        //绘制外圈
        canvas.drawRect(left, top, right, bottom, mPaint);
        //绘制内心
        /**外圈和实心的之间的环形距离*/
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left + ringWidth, top + ringWidth, right - ringWidth, bottom - ringWidth, mPaint);
        this.mPaint.setStyle(Paint.Style.STROKE);
    }
    //endregion

    //region 这里是移动的反馈

    /**
     * 负责俄罗斯方块的移动事件分发
     */
    public void move(TetrisSquare.Move movie) {
        switch (movie) {
            case left:
                this.move2Left();
                break;
            case right:
                this.move2Right();
                break;
            case bottom:
                this.move2Bottom();
                break;
            case top:
                this.move2Top();
                break;
            case rotate:
                matrixTransform_Clockwise();
                this.requestLayout();
                break;
        }

        this.refreshPointPIX();
        this.setX(this.mPointPIX.x);
        this.setY(this.mPointPIX.y);
    }

    private void move2Left() {
        if (this.canMove2Left()) {
            this.mPointMatrix.x--;
        } else {
            _Log.i(_Log.msg() + "已经到达最左边");
        }
    }

    private void move2Right() {
        if (canMove2Right()) {
            this.mPointMatrix.x++;
        } else {
            _Log.i(_Log.msg() + "已经到达最右边");
        }
    }

    private synchronized void move2Bottom() {
        if (state == UiThreadState.move_ing) {
            if (canMove2Bottom()) {
                this.mPointMatrix.y++;
            } else {
                //_Log.i(_Log.msg() + "已经到达最底边--应该粘贴了");
                state = UiThreadState.pastTetris_ing;
                Message message = MainActivity.handler.obtainMessage();
                message.what = 0;
                MainActivity.handler.sendMessage(message);
//            _Log.v(_Log.msg() + "发送粘贴命令-----发送粘贴命令------发送粘贴命令");
            }
        }
    }

    private void move2Top() {
        if (this.mPointMatrix.y - 1 < 0) {
            _Log.i(_Log.msg() + "已经到达最顶边");
        } else {
            this.mPointMatrix.y--;
        }
    }

    private boolean canMove2Left() {
        if (this.mPointMatrix.x - 1 < 0) {//判断是否超出背景了
            return false;
        } else {//如果没有超出背景
            /*1.判断左边是否已经有方块当着了
            * 2.获取TetrisSquare矩阵中每一行最左侧的一个square所对应的beaker中square的左边那个square是否已经被占用了
            * */
            for (int tetrisH = currentMatrixTetris.length - 1; tetrisH >= 0; tetrisH--) {
                for (int tetrisV = 0; tetrisV < currentMatrixTetris[0].length; tetrisV++) {
                    if (currentMatrixTetris[tetrisH][tetrisV] == 1) {
                        int[] beakerPosition = this.tetrisMap2Beaker(tetrisH, tetrisV);
                        if (beakerPosition == null) {
                            return true;
                        }
                        if (tetrisAttr.getBeakerMatris()[beakerPosition[0]][beakerPosition[1] - 1] == 1) {
                            return false;
                        }
                        break;
                    }
                }
            }
            return true;
        }
    }

    private boolean canMove2Right() {
        if (this.mPointMatrix.x + currentMatrixTetris[0].length > tetrisAttr.getBeakerMatris()[0].length - 1) {//判断是否超出背景了
            return false;
        } else {//如果没有超出背景
            /*1.判断右边是否已经有方块挡着了
            * 2.获取TetrisSquare矩阵中每一行最右侧的一个square所对应的beaker中square的右边那个square是否已经被占用了
            * */
            for (int tetrisH = currentMatrixTetris.length - 1; tetrisH >= 0; tetrisH--) {
                for (int tetrisV = currentMatrixTetris[0].length - 1; tetrisV >= 0; tetrisV--) {
                    if (currentMatrixTetris[tetrisH][tetrisV] == 1) {
                        int[] beakerPosition = this.tetrisMap2Beaker(tetrisH, tetrisV);
                        if (beakerPosition == null) {
                            return true;
                        }
                        if (tetrisAttr.getBeakerMatris()[beakerPosition[0]][beakerPosition[1] + 1] == 1) {
                            return false;
                        }
                        break;
                    }
                }
            }
            return true;
        }
    }

    private boolean canMove2Bottom() {
        if (this.mPointMatrix.y + 1 > tetrisAttr.getBeakerMatris().length - 1) {//判断是否超出背景了
            return false;
        } else {//如果没有超出背景
            /*1.判断下边是否已经有方块挡着了
            * 2.获取TetrisSquare矩阵中每一列最下侧的一个square所对应的beaker中square的下边那个square是否已经被占用了
            * */
            for (int tetrisV = 0; tetrisV < currentMatrixTetris[0].length; tetrisV++) {
                for (int tetrisH = currentMatrixTetris.length - 1; tetrisH >= 0; tetrisH--) {
                    if (currentMatrixTetris[tetrisH][tetrisV] == 1) {
                        int[] beakerPosition = this.tetrisMap2Beaker(tetrisH, tetrisV);
                        if (beakerPosition == null) {
                            return true;
                        }
                        if (tetrisAttr.getBeakerMatris()[beakerPosition[0] + 1][beakerPosition[1]] == 1) {
                            return false;
                        }
                        break;
                    }
                }
            }
            return true;
        }
    }

    /**
     * 把当前俄罗斯方块粘贴到背景画布上。
     */
    public void pastTetris() {
        //把tetris粘贴到背景
        if (this.tetrisAttr.tetrisPast2BeakerMatris(currentMatrixTetris, mPointMatrix)) {
            //TetrisBeaker.tetrisBeakerInstance.invalidate();//暂时不修改这里
            //隐藏当前tetris
            moveTetrisSquare.setVisibility(GONE);
            //消除可以消除的行
            tetrisAttr.eliminate(mPointMatrix.y, currentMatrixTetris.length);
        } else {
            //Toast.makeText(getContext(), "Game Over", Toast.LENGTH_LONG).show();
            state = UiThreadState.letSumThread_waiting;
            return;
        }
    }
    //endregion

    public static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(" " + array[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * 根据当前Tetris在矩阵中的位置，确定在容器中的像素位置
     */
    private void refreshPointPIX() {
        if (this.mPointPIX == null) {
            this.mPointPIX = new Point();
        }
        this.mPointPIX.x = tetrisAttr.getMarginHorizontal() + mPointMatrix.x * tetrisAttr.getSideAddSpace();
        this.mPointPIX.y = tetrisAttr.getMarginVertical() + (mPointMatrix.y + 1) * tetrisAttr.getSideAddSpace() - myGetHeight();
        try {
            this.mPointPIX.y += moveTetrisSquare.getTop();
            //_Log.v(_Log.msg() + "refreshPointPIX--》moveTetrisSquare.getTop()" + moveTetrisSquare.getTop());
        } catch (Exception e) {
            _Log.w(_Log.msg() + "这里一直出现是不正常的，但是开始时出现是正常的");
        }
    }

    /**
     * 已知Tetris中Square在Tetris的坐标，
     * 可以得到Tetris在Beaker中的坐标
     *
     * @param tetrisH
     * @param tetrisV
     * @return 返回Tetris中Square映射到Beaker中的坐标
     */
    private int[] tetrisMap2Beaker(int tetrisH, int tetrisV) {
        int beakerH = this.mPointMatrix.y - (currentMatrixTetris.length - 1 - tetrisH);
        if (beakerH < 0) {
            return null;
        }
        int beakerV = this.mPointMatrix.x + tetrisV;
        return new int[]{beakerH, beakerV};
    }
}
