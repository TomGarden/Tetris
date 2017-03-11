package com.farbox.androidbyeleven.Model;

/**
 * describe:Model模块中的共享的get手段。
 * time: 2017/3/4 16:10
 * email: tom.work@foxmail.com
 */
public interface IModelGet {
    /**
     * 获取边长和格子间空隙的和[边长   +  格子间空隙÷2  +  格子间空隙÷2   ]
     */
    int getSideAddSpacePix();

    /**
     * 代表squareLineWidth宽度笔触所画出来的线所占像素宽度值的一半，虽然Beaker和Square都会调用本方法但是却有实质上的区别，因为他们的线的宽度不同。
     * 详细请了解：http://androidbyeleven.farbox.com/post/e-luo-si-fang-kuai/2016-09-01-guan-yu-paintbi-hong-de-liao-jie
     *
     * @return 笔触宽度/2 单位像素
     */
    int getHalfBeakerBGLineWidthPix();

    /**
     * 获取Square之间间距的一半
     *
     * @return 像素距离
     */
    int getHalfSquareSpacePix();

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数
     */
    int getTetrisLineWidthPix();

    /**
     * 获取绘制古典俄罗斯方块的最外层线的笔触宽度像素数÷2
     */
    int getHalfTetrisLineWidthPix();
}
