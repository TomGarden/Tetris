package c508.yangming.teris.Logic;

import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import c508.yangming.teris.CustomView.TetrisBeaker;
import c508.yangming.teris.Util.TomException;
import c508.yangming.teris.Util._Log;

/**
 * 杨铭 Created by kys_8 on 16/9/8,0008. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */
public class MGestureDetector
        implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    /**
     * 累计手指滑动的像素数为移动作指导
     */
    private float left = 0, right = 0, bottom = 0;
    private TetrisBeaker tetrisBeaker;
    //region 关于延时触发滑动从而带来更好的体验
    /**
     * 计时器
     */
    private Timer timer = new Timer();
    /**
     * 下次响应触摸事件所需要等待的时间
     */
    private final int nextMoveWait = 50;
    /**
     * 是否可以滑动的标志
     */
    private boolean canMove = true;

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    fixCanMove(true);
                }
            }).start();
        }
    }

    /**
     * 用来修改canMove的值
     */
    private void fixCanMove(boolean value) {
        this.canMove = value;
    }

    //endregion
    //region 判断快速滑动的常量
    /**
     * 判断是否快速滑动的滑动速度边界值
     */
    private final int speedBoundary = 3000;
    /**
     * X轴方向和Y轴方向滑动的倍数比值
     */
    private final int xyTimes = 4;
    //endregion

    public MGestureDetector(TetrisBeaker tetrisBeaker) {
        this.tetrisBeaker = tetrisBeaker;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public void setRight(float right) {
        this.right = right;
    }


    /**
     * 解释：Touch down时触发
     */
    @Override
    public boolean onDown(MotionEvent e) {
        // ACTION_DOWN
        //_Log.w(_Log.msg() + "按下onDown");
        return true;
    }

    /**
     * 　　解释：Touch了还没有滑动时触发 　　
     * （与onDown，onLongPress比较 　　onDown只要Touch down一定立刻触发。
     * 　　而Touchdown后过一会没有滑动先触发onShowPress再是onLongPress。
     * 　　所以Touchdown后一直不滑动，onDown->onShowPress->onLongPress这个顺序触发。）
     * <p/>
     * ACTION_DOWN 、短按不移动
     */
    @Override
    public void onShowPress(MotionEvent e) {
        // _Log.w(_Log.msg() + "Touch了还没有滑动时触发onShowPress");
    }

    /**
     * 解释：Touch了不移动一直Touch down时触发
     * <p/>
     * ACTION_DOWN 、长按不滑动
     */
    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 解释：是在touchdown后又没有滑动（onScroll），又没有长按（onLongPress），然后Touchup时触发。
     * 点击一下非常快的（不滑动）Touchup：
     * 　　onDown->onSingleTapUp->onSingleTapConfirmed
     * 点击一下稍微慢点的（不滑动）Touchup：
     * 　　onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
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
        _Log.e(_Log.msg() + "我们当做onClick使用");
        if (this.tetrisBeaker.getLogicTetrisBeaker().getGameState() == null) {
            _Log.e(_Log.msg() + "这里是启动游戏");
            this.tetrisBeaker.getLogicTetrisBeaker().startGame();
        } else {
            _Log.e(_Log.msg() + "应该旋转的");
            switch (this.tetrisBeaker.getLogicTetrisBeaker().getGameState()) {
                case RUNNABLE:
                case TIMED_WAITING:
                    this.tetrisBeaker.getLogicTetris().rotateMatrix();
                    break;
                default:
                    _Log.e(_Log.msg() + "居然是另外的状态：" + this.tetrisBeaker.getLogicTetrisBeaker()
                            .getGameState());
                    new TomException();
            }
        }

        return false;
    }

    /**
     * 解释：Touch了滑动时触发。
     * <p/>
     * // ACTION_DOWN 、慢滑动
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!this.canMove) {
            return false;
        }

        //_Log.v(_Log.msg() + "XY方向移动的距离[X,Y]=" + distanceX + "\t" + distanceY);
        //首先判断滑动方向
        /**敏感度*/
        final int sen_1 = 2;
        final int sen_2 = 2;

        if (Math.abs(distanceX / distanceY) > sen_1) {
            if (distanceX < 0) {
                this.left = this.bottom = 0;
                this.right += -distanceX * sen_2;
                if (this.right >= this.tetrisBeaker.getLogicTetrisBeaker().getPaneWidthPix()) {
                    _Log.e(_Log.msg() + "水平滑动-->向右");
                    this.right -= this.tetrisBeaker.getLogicTetrisBeaker().getPaneWidthPix();
                    this.tetrisBeaker.getLogicTetrisBeaker()
                            .tetrisMove(LogicTetrisBeaker.Move.right);
                }
            } else if (distanceX > 0) {
                this.right = this.bottom = 0;
                this.left += distanceX * sen_2;
                if (this.left >= this.tetrisBeaker.getLogicTetrisBeaker().getPaneWidthPix()) {
                    _Log.e(_Log.msg() + "水平滑动-->向左");
                    this.left -= this.tetrisBeaker.getLogicTetrisBeaker().getPaneWidthPix();
                    this.tetrisBeaker.getLogicTetrisBeaker()
                            .tetrisMove(LogicTetrisBeaker.Move.left);
                }
            }
        } else if (Math.abs(distanceY / distanceX) > sen_1) {
            if (distanceY > 0) {
                _Log.e(_Log.msg() + "水平滑动-->向上");
            } else if (distanceY < 0) {

                this.left = this.right = 0;
                this.bottom += -distanceY * sen_2;
                if (this.bottom >= this.tetrisBeaker.getLogicTetrisBeaker().getPaneHeightPix()) {
                    _Log.e(_Log.msg() + "水平滑动-->向下");
                    this.bottom -= this.tetrisBeaker.getLogicTetrisBeaker().getPaneHeightPix();

                    this.tetrisBeaker.getLogicTetrisBeaker()
                            .tetrisMove(LogicTetrisBeaker.Move.bottom);
                }
            }
        } else {
            //无效滑动
            _Log.w(_Log.msg() + "无效滑动");
        }

        fixCanMove(false);
        this.timer.schedule(new MyTimerTask(), this.nextMoveWait);
        return false;
    }


    /**
     * 解释：Touch了滑动一点距离后，up时触发。
     * <p/>
     * // ACTION_DOWN 、快滑动、 ACTION_UP
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        _Log.e(_Log.msg() + _Log.likeCoordinate("快速滑动xy方向的速度[X,Y]=", velocityX, velocityY));
        //region 一方速度大于speedBoundary并且是另一方的xyTimes倍以上我们认为是快速滑动了
        if (Math.max(Math.abs(velocityX), Math.abs(velocityY)) < this.speedBoundary)//这不是快速滑动
        {
            return false;
        }
        if (!(Math.abs(velocityX) / Math.abs(velocityY) > xyTimes || Math.abs(velocityY) / Math
                .abs(velocityX) > xyTimes)) {
            return false;
        }
        //endregion
        if (Math.abs(velocityX) > Math.abs(velocityY))//水平方向速滑
        {
            if (velocityX > this.speedBoundary)//向右速滑
            {
                this.tetrisBeaker.getLogicTetrisBeaker()
                        .tetrisMove(LogicTetrisBeaker.Move.fastRight);
            } else//向左滑动
            {
                this.tetrisBeaker.getLogicTetrisBeaker()
                        .tetrisMove(LogicTetrisBeaker.Move.fastLeft);
            }
        } else {
            if (velocityY > this.speedBoundary) {
                this.tetrisBeaker.getLogicTetrisBeaker()
                        .tetrisMove(LogicTetrisBeaker.Move.fastBottom);
            }
        }
        return false;
    }

    /**
     * 　解释：双击的第二下Touch down时触发
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    /**
     * 解释：双击的第二下Touch down和up都会触发，可用e.getAction()区分。
     */
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

}
