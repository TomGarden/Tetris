package c508.yangming.teris.Logic;

import c508.yangming.teris.CustomView.TetrisBeaker;
import c508.yangming.teris.Util.Global;
import c508.yangming.teris.Util.TomException;
import c508.yangming.teris.Util._Log;

/**
 * 杨铭 Created by kys_8 on 16/9/8,0008. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 * <p/>
 * 逻辑俄罗斯方块烧杯   我们的这个项目是一个游戏不是一个控件，所以我们要把视图层和逻辑层做清晰明了的分界
 */
public class LogicTetrisBeaker {
    /**
     * 容器矩阵
     */
    private int[][] beakerMatris = null;
    //这是一些从现实中抽象出来的内容便于我们操作
    //绘制区域和控件边缘的距离****
    private int paddingLeft = -1;
    private int paddingTop = -1;
    //像素为单位的格子尺寸***
    private int paneWidthPix = -1;
    private int paneHeightPix = -1;
    /**
     * （teries的左下角）当前teries所在的行索引 初始位置没有争议是 0
     */
    private int tetrisRowIndex = 0;
    /**
     * （teries的左下角）当前teries所在列的索引，这个初始位置，是随着不同的控件大小和格子大小而改变的，但是我们可以初始化它的值
     */
    private int tetrisColumnIndex = -1;
    /**
     * 俄罗斯方块视图
     */
    private TetrisBeaker tetrisBeaker;
    /**
     * 游戏主线程
     */
    private Thread gameThread;
    /**
     * 每移动一格后所等待的时间
     */
    private final int waitOnePane = 300;
    /**
     * 当前是否为快速滑动
     */
    private boolean isFastMove = false;

    public enum Move {
        left, right, bottom, fastLeft, fastRight, fastBottom
    }

    public LogicTetrisBeaker(TetrisBeaker tetrisBeaker) {
        this.tetrisBeaker = tetrisBeaker;
    }

    public int[][] getBeakerMatris() {
        return beakerMatris;
    }

    public void setBeakerMatris(int[][] beakerMatris) {
        this.beakerMatris = beakerMatris;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaneHeightPix() {
        return paneHeightPix;
    }

    public void setPaneHeightPix(int paneHeightPix) {
        this.paneHeightPix = paneHeightPix;
    }

    public int getPaneWidthPix() {
        return paneWidthPix;
    }

    public void setPaneWidthPix(int paneWidthPix) {
        this.paneWidthPix = paneWidthPix;
    }

    public int getTetrisColumnIndex() {
        return tetrisColumnIndex;
    }

    public void setTetrisColumnIndex(int tetrisColumnIndex) {
        this.tetrisColumnIndex = tetrisColumnIndex;
    }

    public int getTetrisRowIndex() {
        return tetrisRowIndex;
    }

    public void setTetrisRowIndex(int tetrisRowIndex) {
        this.tetrisRowIndex = tetrisRowIndex;
    }

    /**
     * 移动俄罗斯方块
     */
    public void tetrisMove(Move move) {
        switch (move) {
            case left:
                if (this.canMove(Move.left)) {
                    this.tetrisColumnIndex--;
                } else {
                    _Log.e(_Log.msg() + "已经到达最左侧，不能继续左移了");
                }
                return;
            case right:
                if (this.canMove(Move.right)) {
                    this.tetrisColumnIndex++;
                } else {
                    _Log.e(_Log.msg() + "已经到达最you侧，不能继续左移了");
                }
                return;
            case bottom:
                if (this.canMove(Move.bottom)) {
                    this.tetrisRowIndex++;
                }
                return;
            case fastLeft:
                this.isFastMove = true;
                while (this.canMove(Move.left)) {
                    this.tetrisColumnIndex--;
                }
                return;
            case fastRight:
                this.isFastMove = true;
                while (this.canMove(Move.right)) {
                    this.tetrisColumnIndex++;
                }
                return;
            case fastBottom:
                this.isFastMove = true;
                while (this.canMove(Move.bottom)) {
                    this.tetrisRowIndex++;
                }
                return;
        }
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        if (this.gameThread != null && this.gameThread.getState() == Thread.State.RUNNABLE) {
            _Log.i(_Log.msg() + "游戏已经开始运行了，这个操作什么都没做");
        }
        if (this.gameThread == null || this.gameThread.getState() == Thread.State.TERMINATED) {
            tetrisRowIndex = 0;
            this.gameThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            while (canMove(Move.bottom)) {
                                tetrisRowIndex++;
                                tetrisBeaker.postInvalidate();
                                Thread.sleep(waitOnePane);
                            }
                            if (isFastMove) {
                                isFastMove = false;
                            } else {
                                Thread.sleep(waitOnePane);
                            }
                            //每次运行到这里就是说一个俄罗斯方块不动了
                            recordTetris();
                            //region 测试数据
                            int[][] array = tetrisBeaker.getLogicTetris().getCurrentLogicTeris();
                            int num = 0;
                            for (int i = 0; i < array.length; i++) {
                                for (int j = 0; j < array[0].length; j++) {
                                    if (array[i][j] == 1) {
                                        num++;
                                    }
                                }
                            }
                            if (num == 2) {
                                num = 0;
                                Global.localizeLog.write2FileEnter(
                                        "=====================================================");
                                _Log.e(_Log.msg() + "===============================");
                            }
                            //endregion
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tetrisBeaker.getLogicTetris().createNextTetris();
                        tetrisRowIndex = 0;
                        setTetrisColumnIndex(
                                getBeakerMatris()[0].length / 2 - tetrisBeaker.getLogicTetris()
                                        .getCurrentLogicTeris()[0].length / 2);
                    }
                }
            });
            this.gameThread.start();
        }
    }

    /**
     * 判断是否可以右移
     *
     * @param currentLogicTeris 即将被移动的俄罗斯方块
     * @return
     */
    private boolean canMove2Right(int[][] currentLogicTeris) {
        if (this.tetrisColumnIndex + 1 <= getBeakerMatris()[0].length - this.tetrisBeaker
                .getLogicTetris().getCurrentLogicTeris()[0].length)//如果还没有到达最右侧
        {
            for (int vIndex = 0; vIndex < currentLogicTeris[0].length; vIndex++) {
                for (int hIndex = currentLogicTeris.length - 1; hIndex >= 0; hIndex--) {
                    int beakerHIndex, beakerVIndex;//映射到烧杯中的索引
                    beakerHIndex = this.tetrisRowIndex - (currentLogicTeris.length - 1 - hIndex);
                    if (beakerHIndex < 0) {
                        break;
                    }

                    //确定这是最右侧的点
                    boolean isRightPoint = true;
                    for (int vIndex_2 = vIndex + 1; vIndex_2 < currentLogicTeris[0].length;
                         vIndex_2++) {
                        if (currentLogicTeris[hIndex][vIndex_2] == 1) {
                            isRightPoint = false;
                            break;
                        }
                    }
                    if (isRightPoint) {
                        beakerVIndex = this.tetrisColumnIndex + vIndex + 1;
                        switch (getBeakerMatris()[beakerHIndex][beakerVIndex]) {
                            case 0://可以移动我们判断下一个点
                                break;
                            case 1:
                                return false;
                            default:
                                new TomException("运行到这里说明有异常");
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否可以左移
     *
     * @param currentLogicTeris 即将被移动的俄罗斯方块
     * @return
     */
    private boolean canMove2Left(int[][] currentLogicTeris) {
        if (this.tetrisColumnIndex - 1 >= 0)//还没有到达最左侧
        {
            for (int vIndex = 0; vIndex < currentLogicTeris[0].length; vIndex++) {
                for (int hIndex = currentLogicTeris.length - 1; hIndex >= 0; hIndex--) {
                    int beakerHIndex, beakerVIndex;//映射到烧杯中的索引
                    beakerHIndex = this.tetrisRowIndex - (currentLogicTeris.length - 1 - hIndex);
                    if (beakerHIndex < 0) {
                        break;
                    }

                    //确定这是最左侧的点
                    boolean isLeftPoint = true;
                    for (int vIndex_2 = vIndex - 1; vIndex_2 >= 0; vIndex_2--) {
                        if (currentLogicTeris[hIndex][vIndex_2] == 1) {
                            isLeftPoint = false;
                            break;
                        }
                    }
                    if (isLeftPoint) {
                        beakerVIndex = this.tetrisColumnIndex + vIndex - 1;
                        switch (getBeakerMatris()[beakerHIndex][beakerVIndex]) {
                            case 0://可以移动我们判断下一个点
                                break;
                            case 1:
                                return false;
                            default:
                                new TomException("运行到这里说明有异常");
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否可以下移
     *
     * @param currentLogicTeris 即将被移动的俄罗斯方块
     * @return
     */
    private boolean canMove2Bottom(int[][] currentLogicTeris) {
        if (tetrisRowIndex < getBeakerMatris().length - 1)//还没有到底
        {
            //避免碰撞已经存在的Tetris 思路
            //这个俄罗斯方块中的这一个点是本列最底部的点，并且这个点映射到烧杯中的对应点的下方的一个点尚空缺，才可以下移
            for (int hIndex = currentLogicTeris.length - 1; hIndex >= 0; hIndex--) {
                int beakerHIndex, beakerVIndex;
                beakerHIndex = this.tetrisRowIndex - (currentLogicTeris.length - 1 - hIndex) + 1;
                if (beakerHIndex < 0) {
                    break;
                }
                for (int vIndex = 0; vIndex < currentLogicTeris[0].length; vIndex++) {
                    /**tetris数组和Baker数组重合部分求和的结果，根据结果判断是否继续移动tetris*/
                    if (currentLogicTeris[hIndex][vIndex] == 1)//这就是从做到又从下到上的其中一个点
                    {
                        //确定他是本列最底部的点
                        boolean isBottomPoint = true;
                        for (int hIndex_2 = hIndex + 1; hIndex_2 < currentLogicTeris.length;
                             hIndex_2++) {
                            if (currentLogicTeris[hIndex_2][vIndex] == 1) {
                                isBottomPoint = false;
                                break;
                            }
                        }
                        //如果是本列对底部的点，就和烧杯映射点的下一个点做推测
                        if (isBottomPoint) {
                            /**俄罗斯方块中的点映射到烧杯的索引，我们要看的是下方一个点，所以要*/
                            beakerVIndex = this.tetrisColumnIndex + vIndex;
                            /**对在最右侧旋转的修正*/
                            while (beakerVIndex > this.beakerMatris[0].length - 1) {
                                beakerVIndex--;
                                this.tetrisColumnIndex--;
                            }
                            switch (this.getBeakerMatris()[beakerHIndex][beakerVIndex]) {
                                case 0://可以移动我们判断下一个点
                                    break;
                                case 1:
                                    return false;
                                default:
                                    new TomException("运行到这里说明有异常");
                            }
                        }
                    }
                }
            }
            return true;
        } else {//已经到达底部不能移动了
            //_Log.e(_Log.msg() + "到底了");
            return false;
        }
    }

    /**
     * 判断时候可以移动
     *
     * @param move 想要移动的方向
     * @return
     */
    public boolean canMove(Move move) {
        int[][] currentLogicTeris = this.tetrisBeaker.getLogicTetris().getCurrentLogicTeris();
        switch (move) {
            case bottom:
                return this.canMove2Bottom(currentLogicTeris);
            case right:
                return this.canMove2Right(currentLogicTeris);
            case left:
                return this.canMove2Left(currentLogicTeris);
            default:
                new TomException();
                return false;
        }

    }

    /**
     * 记录下俄罗斯方块在烧杯中的位置
     */
    private void recordTetris() {
        int[][] currentLogicTetris = tetrisBeaker.getLogicTetris().getCurrentLogicTeris();
        _Log.i(_Log.msg() + "即将写入的俄罗斯方块");
        this.printArray(currentLogicTetris);
        for (int hIndex = currentLogicTetris.length - 1; hIndex >= 0; hIndex--) {
            int beakerHIndex, beakerVIndex;
            beakerHIndex = this.tetrisRowIndex - (currentLogicTetris.length - 1 - hIndex);
            _Log.w(_Log.msg() + "this.tetrisRowIndex=" + this.tetrisRowIndex +
                    "\tcurrentLogicTetris.length=" + currentLogicTetris.length +
                    "\thIndex=" + hIndex + "\tbeakerHIndex=" + beakerHIndex);
            if (beakerHIndex < 0) {
                new TomException("Game Over");
                break;
            }
            for (int vIndex = 0; vIndex < currentLogicTetris[0].length; vIndex++) {
                _Log.i(_Log.msg() + _Log.likeCoordinate("[[hIndex][vIndex]]", hIndex, vIndex));
                /**tetris数组和Baker数组重合部分求和的结果，根据结果判断是否继续移动tetris*/
                if (currentLogicTetris[hIndex][vIndex] == 1)//这就是从做到又从下到上的其中一个点
                {
                    //标记
                    /**烧杯的索引*/
                    beakerVIndex = this.tetrisColumnIndex + vIndex;
                    if (this.getBeakerMatris()[beakerHIndex][beakerVIndex] == 1) {
                        _Log.e(_Log.msg() + "当前tetrisRowIndex=" + tetrisRowIndex);
                        new TomException("这个位置不应该是1的，是1说明是我们的逻辑有异常");
                    } else {
                        this.getBeakerMatris()[beakerHIndex][beakerVIndex] = 1;
                        if (this.dispelBeakerLine(beakerHIndex))//如果烧杯删除了进行Tetris的删除才有必要
                        {
                            this.dispeTetrisLine(currentLogicTetris, hIndex);
                            hIndex++;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 消除烧杯中的行
     *
     * @param beakerHIndex 打算判断的烧杯的行索引
     * @return 返回值表示，这一行是否可以消除，如果不可以消除返回false，如果可以消除在消除成功后返回true
     */
    private boolean dispelBeakerLine(int beakerHIndex) {
        //判断是否符合消除条件
        for (int beakerVIndex = 0; beakerVIndex < getBeakerMatris()[0].length; beakerVIndex++) {
            if (getBeakerMatris()[beakerHIndex][beakerVIndex] == 0)//如果存在没有填充的格子，直接结束
            {
                return false;
            }
        }
        //能运行到这里说明可以消除==所谓消除就是数组从某一行开始集体下移
        for (int beakerHIndexMove = beakerHIndex - 1; beakerHIndexMove >= 0; beakerHIndexMove--) {
            for (int beakerVIndex = 0; beakerVIndex < getBeakerMatris()[0].length; beakerVIndex++) {
                getBeakerMatris()[beakerHIndexMove + 1][beakerVIndex] = getBeakerMatris()
                        [beakerHIndexMove][beakerVIndex];
            }
        }
        for (int beakerVIndex = 0; beakerVIndex < getBeakerMatris()[0].length; beakerVIndex++) {
            if (getBeakerMatris()[0][beakerVIndex] == 1) {
                getBeakerMatris()[0][beakerVIndex] = 0;
            }
        }
        return true;
    }

    /**
     * 消除俄罗斯方块中的行
     */
    private void dispeTetrisLine(int[][] currentLogicTetris, int hIndex) {
        //能运行到这里意味着hIndex在烧杯中已经被消除了
        for (int tetrisHIndex = hIndex - 1; tetrisHIndex >= 0; tetrisHIndex--) {
            for (int tetrisVIndex = 0; tetrisVIndex < currentLogicTetris[0].length; tetrisVIndex++) {
                currentLogicTetris[tetrisHIndex + 1][tetrisVIndex] =
                        currentLogicTetris[tetrisHIndex][tetrisVIndex];
            }
        }
        for (int tetrisVIndex = 0; tetrisVIndex < currentLogicTetris[0].length; tetrisVIndex++) {
            if (currentLogicTetris[0][tetrisVIndex] == 1) {
                currentLogicTetris[0][tetrisVIndex] = 0;
            }
        }

    }

    /**
     * 返回游戏线程的状态
     */
    public Thread.State getGameState() {
        if (this.gameThread == null) {
            return null;
        }
        return this.gameThread.getState();
    }

    public void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(" " + array[i][j]);
            }
            System.out.println();
        }
    }
}
