package com.farbox.androidbyeleven.Model.RunModel;

/**
 * describe: 用来统计用户得分。
 * time: 2017/3/14 7:48
 * email: tom.work@foxmail.com
 */
public class HiScore {

    //region 单例 getInstance();
    private static volatile HiScore instance = null;

    private HiScore() {
        //do something
    }

    public static HiScore getInstance() {
        if (instance == null) {
            synchronized (BaseModel.class) {
                if (instance == null) {
                    instance = new HiScore();
                }
            }
        }
        return instance;
    }

    //endregion

    /**
     * 游戏分数
     */
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 消除之后负责加分
     */
    public void eliminateOne() {
        this.score++;
    }

    /**
     * 消除之后负责加分
     */
    public void eliminateNum(int eliminateNum) {
        this.score += eliminateNum;
    }
}
