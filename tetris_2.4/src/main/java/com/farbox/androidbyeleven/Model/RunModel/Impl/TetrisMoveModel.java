package com.farbox.androidbyeleven.Model.RunModel.Impl;

import android.graphics.Point;

import com.farbox.androidbyeleven.Controller.Control.GameState;
import com.farbox.androidbyeleven.Controller.Control.MoveDirection;
import com.farbox.androidbyeleven.Model.RunModel.BaseModel;
import com.farbox.androidbyeleven.Model.RunModel.ITetrisMoveModelGet;
import com.farbox.androidbyeleven.Model.RunModel.ITetrisMoveModelInteractive;
import com.farbox.androidbyeleven.Model.RunModel.ITetrisMoveModelSet;
import com.farbox.androidbyeleven.Utils.ConvertUtil;
import com.farbox.androidbyeleven.Utils.Global;
import com.farbox.androidbyeleven.Utils.LogUtil;
import com.farbox.androidbyeleven.Utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 2016/11/9.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 * <p>
 * 本类掌控展示用的Tetris的数据
 */
public class TetrisMoveModel implements ITetrisMoveModelGet, ITetrisMoveModelSet, ITetrisMoveModelInteractive {
    /**
     * 烧杯背景格子线宽度的一半。
     */
    private int halfBeakerBGLineWidthPix = Global.notSet;
    /**
     * 存储基本俄罗斯方块
     */
    private List<int[][]> tetrisMatrixList = null;
    /**
     * 代表当前Tetris的逻辑矩阵
     * <p>
     * 若本属性为null代表刚刚完成粘贴然后被置为null了，如果要再次调用应该先把TetrisShow中的矩阵交接过来才行。
     * 像素坐标
     */
    private int[][] currentMatrix = null;
    /**
     * 记录Tetris当前在Beaker中的位置[按矩阵] y表示 行  x 表示列
     * 注：Tetris中左下角的Square在Beaker中的坐标
     * 注：当使用完成后置为Null，下次使用发现为Null会初始化的
     */
    private Point tetrisInBeakerPos = null;

    public TetrisMoveModel() {
        tetrisMatrixList = new ArrayList<>();
        /** 俄罗斯方块的矩阵表示 */
        tetrisMatrixList.add(new int[][]{{1, 1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 0, 0}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 1, 0}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 0, 1}, {1, 1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 1}, {1, 1}});
        tetrisMatrixList.add(new int[][]{{1, 1, 0}, {0, 1, 1}});
        tetrisMatrixList.add(new int[][]{{0, 1, 1}, {1, 1, 0}});
    }

    //region  ITetrisMoveModelGet

    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    @Override
    public int getSideAddSpacePix() {
        return BaseModel.getInstance().getSideAddSpacePix();
    }

    /**
     * 代表squareLineWidth宽度笔触所画出来的线所占像素宽度值的一半。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return 笔触宽度/2 单位像素
     */
    @Override
    public int getHalfBeakerBGLineWidthPix() {
        //如果要修改本方法记得查看IGetModel中本方法有几处实现
        if (this.halfBeakerBGLineWidthPix == Global.notSet) {
            this.halfBeakerBGLineWidthPix = this.getSquareSidePix() / 8;
        }
        return this.halfBeakerBGLineWidthPix;
    }

    /**
     * 获取Square之间间距的一半
     *
     * @return 像素距离
     */
    @Override
    public int getHalfSquareSpacePix() {
        return BaseModel.getInstance().getHalfSquareSpacePix();
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    @Override
    public int getTetrisLineWidthPix() {
        return BaseModel.getInstance().getTetrisLineWidthPix();
    }

    /**
     * 获取每一个格子的像素数
     */
    @Override
    public int getSquareSidePix() {
        return BaseModel.getInstance().getSquareSidePix();
    }

    /**
     * 获取当前俄罗斯方块矩阵，本矩阵有两个属性:[样式、方向]
     *
     * @return
     */
    public int[][] getCurrentMatrix() {
        return this.currentMatrix;
    }

    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    @Override
    public String getMatris2Str() {
        return ConvertUtil.array2Str(this.getCurrentMatrix());
    }

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    @Override
    public int getHalfTetrisLineWidthPix() {
        return BaseModel.getInstance().getHalfTetrisLineWidthPix();
    }

    /**
     * [这是实现的接口的方法]获取当前俄罗斯方块在烧杯中的位置,每次获取都是重新计算的
     *
     * @return
     */
    @Override
    public Point getTetrisInBeakerPixPos() {
        int marginHorizontal = BaseModel.getInstance().getMarginHorizentalPix();
        int marginVertical = BaseModel.getInstance().getMarginVerticalPix();
        if (marginHorizontal == Global.notSet || marginVertical == Global.notSet) {
            throw new RuntimeException(Global.tipNotInitOver);
        }
        Point positionPIX = new Point();
        positionPIX.x = marginHorizontal + this.getTetrisInBeakerPos().x * BaseModel.getInstance().getSideAddSpacePix();
        positionPIX.y = marginVertical + (this.getTetrisInBeakerPos().y + 1) * BaseModel.getInstance().getSideAddSpacePix() - myGetHeight();

        return positionPIX;
    }

    /**
     * 获取当前俄罗斯方块在烧杯中的位置逻辑坐标
     *
     * @return
     */
    @Override
    public Point getTetrisInBeakerLogicPos() {
        return this.getTetrisInBeakerPos();
    }
    //endregion

    //region    ITetrisMoveModelSet

    /**
     * 设置当前正在显示的矩阵
     *
     * @return
     */
    @Override
    public void setCurrentMatrix(int[][] currentMatrix) {
        this.currentMatrix = currentMatrix;
    }

    /**
     * 设置在背景矩阵中的逻辑位置
     *
     * @param pos
     */
    @Override
    public void setLogicPos(Point pos) {
        this.setTetrisInBeakerPos(pos);
    }
    //endregion

    //region  ITetrisMoveModelInteractive

    /**
     * 这个就是本类的Getter方法
     */
    private Point getTetrisInBeakerPos() {
        if (tetrisInBeakerPos == null) {
            tetrisInBeakerPos = new Point();
            LogUtil.i(LogUtil.msg() + "曾经有次BaseModel.getInstance().getBeakerMatris()得到null");
            //首先计算在矩阵中的位置坐标
            tetrisInBeakerPos.x = (BaseModel.getInstance().getBeakerMatris()[0].length - getCurrentMatrix()[0].length) / 2;
            tetrisInBeakerPos.y = 0;
        }
        //应该判断下，当前Tetris在我们求得的Pos处是否覆盖了已经存在的Square
        return this.tetrisInBeakerPos;
    }

    private void setTetrisInBeakerPos(Point point) {
        this.tetrisInBeakerPos = point;
    }

    /**
     * 移动方法
     *
     * @param direction 计划移动的方向
     */
    @Override
    public boolean moveTo(MoveDirection direction) {
        return this.move(direction);
    }

    /**
     * 负责俄罗斯方块的移动事件分发
     *
     * @return 移动动作是否真实发生
     */
    public boolean move(MoveDirection direction) {
        switch (direction) {
            case left:
                return this.move2Left();
            case right:
                return this.move2Right();
            case bottom:
                return this.move2Bottom();
            case top:
                return this.move2Top();
            case rotate:
                //LogUtil.i(LogUtil.msg() + "这个旋转尚需精心雕琢，1，从一字型的Tetris看尤为明显，2，矩形是不会真正旋转的");

                if (this.getCurrentMatrix().length == 2 && this.getCurrentMatrix().length == this.getCurrentMatrix()[0].length) {
                    LogUtil.i(LogUtil.msg() + "不必旋转");
                    return false;//这个矩形Tetris就不用旋转了
                }
                //1、旋转
                int[][] roateResultMatrix = MathUtil.matrixRotateClockwise(this.getCurrentMatrix());
                //2、修正Position--旋转之后的修正不可或缺，否则旋转有可能导致俄罗斯方块到边界之外的地方去。

                Point fixResultPosition = this.fixRoatate(roateResultMatrix, this.getTetrisInBeakerPos());

                //Test.printArray(this.getCurrentMatrix());
                //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("[h,v]", this.getTetrisInBeakerPos().y, this.getTetrisInBeakerPos().x));
                //System.out.println("---------------------------");
                //Test.printArray(roateResultMatrix);

                if (fixResultPosition == null) {//意味着位置不用修正
                    fixResultPosition = this.getTetrisInBeakerPos();
                }

                //LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("[h,v]", fixResultPosition.y, fixResultPosition.x));
                //System.out.println("---------------------------");

                if (this.isCanExist(roateResultMatrix, fixResultPosition)) {
                    this.setCurrentMatrix(roateResultMatrix);
                    this.setTetrisInBeakerPos(fixResultPosition);
                    return true;
                } else {
                    return false;
                }
        }
        LogUtil.i(LogUtil.msg() + LogUtil.likeCoordinate("[x,y]", this.getTetrisInBeakerPos().x, this.getTetrisInBeakerPos().y));
        return false;
    }

    /**
     * [假设性判断]判断给定的TetrisMatrix在给定的tetrisPosition处是否需要修正tetrisPosition.（为了防止Tetris超过Beaker右边界做出修正）
     *
     * @param tetrisMatrix
     * @param tetrisPosition 矩阵作别并非像素坐标
     * @return 如果需要修正修正后的结果，如果不需要修正 null;
     */
    private Point fixRoatate(int[][] tetrisMatrix, Point tetrisPosition) {
        Point fixMatrixPosition = new Point(tetrisPosition);
        if (fixMatrixPosition.x + tetrisMatrix[0].length > BaseModel.getInstance().getBeakerMatris()[0].length) {
            fixMatrixPosition.x = BaseModel.getInstance().getBeakerMatris()[0].length - tetrisMatrix[0].length;
            return fixMatrixPosition;//修正后的结果
        } else {
            return null;//无需修正
        }
    }

    /**
     * @return 返回值表示移动是否发生了
     */
    private boolean move2Left() {
        if (this.canMove2Left()) {
            this.getTetrisInBeakerPos().x--;
            return true;
        } else {
            //LogUtil.i(LogUtil.msg() + "已经到达最左边");
            return false;
        }
    }

    private boolean move2Right() {
        if (canMove2Right()) {
            this.getTetrisInBeakerPos().x++;
            return true;
        } else {
            //LogUtil.i(LogUtil.msg() + "已经到达最右边");
            return false;
        }
    }

    private synchronized boolean move2Bottom() {
        if (Global.gameState == GameState.moving) {
            if (canMove2Bottom()) {
                this.getTetrisInBeakerPos().y++;
                return true;
            } else {
                //LogUtil.i(LogUtil.msg() + "已经到达最下边，应该在粘贴的过程中等待一次移动的时间");
                return false;
            }
        }
        return false;
    }

    private boolean move2Top() {
        if (this.getTetrisInBeakerPos().y - 1 < 0) {
            //LogUtil.i(LogUtil.msg() + "已经到达最顶边");
            return false;
        } else {
            this.getTetrisInBeakerPos().y--;
            return true;
        }
    }

    private boolean canMove2Left() {
        if (this.getTetrisInBeakerPos().x - 1 < 0) {//判断是否超出背景了
            return false;
        } else {//如果没有超出背景
            /*1.判断左边是否已经有方块当着了
            * 2.获取TetrisSquare矩阵中每一行最左侧的一个square所对应的beaker中square的左边那个square是否已经被占用了
            * */
            for (int tetrisH = currentMatrix.length - 1; tetrisH >= 0; tetrisH--) {
                for (int tetrisV = 0; tetrisV < currentMatrix[0].length; tetrisV++) {
                    if (currentMatrix[tetrisH][tetrisV] == 1) {
                        Point squareInBeakerPosition = this.squareMap2Beaker(new Point(tetrisV, tetrisH), this.getTetrisInBeakerPos(), getCurrentMatrix());
                        if (squareInBeakerPosition == null) {
                            return true;
                        }
                        if (BaseModel.getInstance().getBeakerMatris()[squareInBeakerPosition.y][squareInBeakerPosition.x - 1] == 1) {
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
        if (this.getTetrisInBeakerPos().x + currentMatrix[0].length > BaseModel.getInstance().getBeakerMatris()[0].length - 1) {//判断是否超出背景了
            return false;
        } else {//如果没有超出背景
            /*1.判断右边是否已经有方块挡着了
            * 2.获取TetrisSquare矩阵中每一行最右侧的一个square所对应的beaker中square的右边那个square是否已经被占用了
            * */
            for (int tetrisH = currentMatrix.length - 1; tetrisH >= 0; tetrisH--) {
                for (int tetrisV = currentMatrix[0].length - 1; tetrisV >= 0; tetrisV--) {
                    if (currentMatrix[tetrisH][tetrisV] == 1) {
                        Point squareInBeakerPosition = this.squareMap2Beaker(new Point(tetrisV, tetrisH), this.getTetrisInBeakerPos(), getCurrentMatrix());
                        if (squareInBeakerPosition == null) {
                            return true;
                        }
                        if (BaseModel.getInstance().getBeakerMatris()[squareInBeakerPosition.y][squareInBeakerPosition.x + 1] == 1) {
                            return false;
                        }
                        break;
                    }
                }
            }
            return true;
        }
    }

    private synchronized boolean canMove2Bottom() {
        if (this.getTetrisInBeakerPos().y + 1 > BaseModel.getInstance().getBeakerMatris().length - 1) {//判断是否超出背景了
            return false;
        } else {//如果没有超出背景
            /*1.判断下边是否已经有方块挡着了
            * 2.获取TetrisSquare矩阵中每一列最下侧的一个square所对应的beaker中square的下边那个square是否已经被占用了
            * */
            for (int tetrisV = 0; tetrisV < currentMatrix[0].length; tetrisV++) {
                for (int tetrisH = currentMatrix.length - 1; tetrisH >= 0; tetrisH--) {
                    if (currentMatrix[tetrisH][tetrisV] == 1) {
                        Point squareInBeakerPosition = this.squareMap2BeakerQ(new Point(tetrisV, tetrisH), this.getTetrisInBeakerPos(), this.getCurrentMatrix());
                        squareInBeakerPosition.y++;
                        if (squareInBeakerPosition.y < 0) {//继续下一列
                            break;
                        }
                        if (BaseModel.getInstance().getBeakerMatris()[squareInBeakerPosition.y][squareInBeakerPosition.x] == 1) {
                            return false;
                        } else {
                            break;
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * 假设性判断，判断TetrisMatrix代表的Tetris是否可以出现在TetrisPosition所指定的位置。（判断指定位置的指定矩阵映射到Beaker上的Square是否已经有了别的Square）
     * <p>
     * 思路：从上往下做映射，这么做是不行的，我们应该从下往上做映射，因为有可能在顶部有一部分是在Beaker外的，这一部分不用判断
     *
     * @param tetrisMatrix   假设Tetris矩阵
     * @param tetrisPosition 假设Tetris在Beaker中的坐标位置(Tetris在Beaker中的位置就是Tetris中左下角Square在Beaker中的位置)
     * @return true，假设成立    false，假设不成立
     */
    private boolean isCanExist(int[][] tetrisMatrix, Point tetrisPosition) {
        /*for (int th = 0; th < tetrisMatrix.length; th++) {//从上往下*/
        for (int th = tetrisMatrix.length - 1; th >= 0; th--) {//从下到上上
            for (int tv = 0; tv < tetrisMatrix[0].length; tv++) {//从左到右
                if (tetrisMatrix[th][tv] == 0) {
                    continue;
                }
                //求得映射坐标
                /*int mapH;
                if (tetrisMatrix.length != 4) {//一字型的Tetris实际上是一维数组，这个处理需谨慎
                    mapH = tetrisPosition.y - (tetrisMatrix.length - 1 - th);
                    if (mapH < 0) {//在Beaker上边界之外了不用判断了。
                        return true;
                    }
                } else {
                    mapH = tetrisPosition.y;
                }
                int mapV = tetrisPosition.x - (0 - tv);*/
                Point squareMap2BeakerPosition = this.squareMap2Beaker(new Point(tv, th), tetrisPosition, tetrisMatrix);
                if (squareMap2BeakerPosition == null) {
                    return true;
                }
                if (BaseModel.getInstance().getBeakerMatris()[squareMap2BeakerPosition.y][squareMap2BeakerPosition.x] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 根据Square在Tetris中的坐标、Tetris在Beaker中的坐标和Tetris的逻辑矩阵计算 Square映射到在Beaker的坐标
     * <p>
     * 注：Point.y 表示行索引，point.x 表示列索引
     *
     * @param squareInTetrisPosition square在Tetris中的坐标
     * @param tetrisInBeakerPosition Tetris在Beaker中的坐标
     * @param tetrisMatris           tetris的逻辑矩阵
     * @return null：Square在Beaker的范围之外，notNull：square映射到Beaker上的索引
     */
    private Point squareMap2Beaker(Point squareInTetrisPosition, Point tetrisInBeakerPosition, int[][] tetrisMatris) {
        int beakerH;
        if (tetrisMatris.length != 1) {
            beakerH = tetrisInBeakerPosition.y - (tetrisMatris.length - 1 - squareInTetrisPosition.y);
        } else {
            beakerH = tetrisInBeakerPosition.y;
        }
        if (beakerH < 0) {//在Beaker上边界之上了
            return null;
        }
        int beakerV = tetrisInBeakerPosition.x + squareInTetrisPosition.x;
        return new Point(beakerV, beakerH);
    }

    /**
     * 根据Square在Tetris中的坐标、Tetris在Beaker中的坐标和Tetris的逻辑矩阵计算 Square映射到在Beaker的坐标
     * <p>
     * 注：Point.y 表示行索引，point.x 表示列索引
     *
     * @param squareInTetrisPosition square在Tetris中的坐标
     * @param tetrisInBeakerPosition Tetris在Beaker中的坐标
     * @param tetrisMatris           tetris的逻辑矩阵
     * @return 本方法无论如何都会返回一个Point对象，只是需要根据point.y判断这个映射到的索引是否可用，根据用途在做商议 ，[如果point.y<0说明映射到的square在Beaker上的索引是在Beaker范围之外的而且是在Beaker上边界之外]
     */
    private Point squareMap2BeakerQ(Point squareInTetrisPosition, Point tetrisInBeakerPosition, int[][] tetrisMatris) {
        int beakerH;
        if (tetrisMatris.length != 1) {//我们想排除的是横着的长条
            beakerH = tetrisInBeakerPosition.y - (tetrisMatris.length - 1 - squareInTetrisPosition.y);
        } else {
            beakerH = tetrisInBeakerPosition.y;
        }
        // TODO: 2017/3/10 注意了本方法无论如何都会返回一个Point对象，只是需要根据point.y判断这个映射到的索引是否可用，根据用途在做商议[如果point.y<0说明映射到的square在Beaker上的索引是在Beaker范围之外的而且是在Beaker上边界之外]
        /*if (beakerH < 0) {//在Beaker上边界之上了
            return null;
        }*/
        int beakerV = tetrisInBeakerPosition.x + squareInTetrisPosition.x;
        return new Point(beakerV, beakerH);
    }

    /**
     * 把tetris粘贴到烧杯矩阵上[本类内部调用]
     *
     * @param tetrisMatris:Tetris矩阵
     * @param point:tetris在beakerMatris中的位置，记录的是TetrisMatris中左下角那一块在beakerMatris中的位置
     * @return true粘贴成功，false粘贴失败，原因是俄罗斯方块超过了背景的顶部
     */
    private boolean tetrisPast2BeakerMatris(int[][] tetrisMatris, Point point) {
        //int[][] beakerMatris = BaseModel.getInstance().getBeakerMatris();
        int beakerH, beakerV;
        for (int tetirsH = 0; tetirsH < tetrisMatris.length; tetirsH++) {
            beakerH = point.y - (tetrisMatris.length - 1 - tetirsH);
            if (beakerH < 0) {//说明有一部分在顶部的背景之外
                return false;
            }
            for (int tetrisV = 0; tetrisV < tetrisMatris[0].length; tetrisV++) {
                if (tetrisMatris[tetirsH][tetrisV] == 1) {
                    beakerV = point.x + tetrisV;
                    BaseModel.getInstance().getBeakerMatris()[beakerH][beakerV] = 1;
                }
            }
        }
        return true;
    }

    /**
     * 调用真正的粘贴方法
     *
     * @return 如果粘贴成功：point.x = 所粘贴的Tetris所在的最底行的行数 point.y = Tetris本身的行数。
     * 粘贴失败 return null  ，说明Tetris有再Beaker之外的部分，游戏应该结束了
     */
    @Override
    public Point tetrisPast2BeakerMatris() {
        Point eliminateData = null;
        if (tetrisPast2BeakerMatris(getCurrentMatrix(), getTetrisInBeakerPos())) {
            eliminateData = new Point(this.getTetrisInBeakerPos().y, this.getCurrentMatrix().length);
        }
        this.setCurrentMatrix(null);
        this.setTetrisInBeakerPos(null);
        return eliminateData;
    }


    /**
     * 消除给定的行
     *
     * @param start  开始行，行的索引从上倒下增大，，我们给定的是最下方的行索引
     * @param length 总行数
     */
    @Override
    public int eliminate(int start, int length) {
        int eliminateNum = 0;//已经消除了的行数
        boolean eliminate = true;//消除？
        int[][] beakerMatris = BaseModel.getInstance().getBeakerMatris();
        for (int i = 0, h = start - i; i < length; i++, h--) {
            for (int v = 0; v < beakerMatris[0].length; v++) {
                if (beakerMatris[h][v] == 0) {
                    eliminate = false;
                    break;
                }
            }
            if (eliminate) {//消除这一行
                for (int v = 0; v < beakerMatris[0].length; v++) {
                    if (beakerMatris[h][v] != 0) {
                        beakerMatris[h][v] = 0;
                    }
                }
                //以上整体下移
                for (int mh = h; mh > 0; mh--) {
                    for (int v = 0; v < beakerMatris[0].length; v++) {
                        if (beakerMatris[mh][v] != beakerMatris[mh - 1][v]) {
                            beakerMatris[mh][v] = beakerMatris[mh - 1][v];
                        }
                    }
                }
                //顶行置空
                for (int v = 0; v < beakerMatris[0].length; v++) {
                    if (beakerMatris[0][v] != 0) {
                        beakerMatris[0][v] = 0;
                    }
                }
                //索引重置
                h++;
                length--;
                i--;
                //消除统计
                eliminateNum++;
                //LogUtil.e(LogUtil.msg() + "消除了：" + h);
            }
            eliminate = true;
        }
        return eliminateNum;
    }
    //endregion

    /**
     * 自定义获取控件高度，和原方法的getWidth相同，但是如果代码修改后就不一定了，注意修改
     */
    public int myGetHeight() {
        this.getCurrentMatrix();

        return this.currentMatrix.length * BaseModel.getInstance().getSideAddSpacePix();
    }
}
