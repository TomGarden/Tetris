package com.farbox.androidbyeleven.Controller;

import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.farbox.androidbyeleven.Model.BeakerModel;
import com.farbox.androidbyeleven.Model.IBeakerModelGetData;
import com.farbox.androidbyeleven.R;
import com.farbox.androidbyeleven.View.IBeakerGetData;
import com.farbox.androidbyeleven.View.TetrisBeaker;

/**
 * 杨铭 Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */
public class Controller implements IBeakerGetData, IBeakerModelGetData {
    /**
     * 来自MainActivity
     */
    private Activity activity = null;
    private BeakerModel beakerModel = null;
    private TetrisBeaker tetrisBeaker = null;
    private LinearLayout linearLayout = null;
    private FrameLayout frameLayout = null;
    private Switch mSwitch = null;

    /**
     * 构造方法
     * <p>
     * 注意：本方法中首先find TetrisBeaker，然后 new Model 这个顺序是不能颠倒的，因为Model需要从初始化的TetrisBeaker中获取参数来初始化自身
     */
    public Controller(Activity activity) {
        this.activity = activity;

        this.linearLayout = (LinearLayout) activity.findViewById(R.id.ll_widget);
        this.frameLayout = (FrameLayout) activity.findViewById(R.id.fl_viewGroup);

        //①find 也就是调用构造方法
        this.tetrisBeaker = (TetrisBeaker) activity.findViewById(R.id.tetrisBeaker);
        //②设置回调本类
        this.tetrisBeaker.setDataInterface(this);
        //③在①②的基础上才能初始化Model
        this.beakerModel = new BeakerModel(activity.getApplicationContext(), this);
    }

    //region IBeakerGetData

    /**
     * 获取烧杯数组
     */
    @Override
    public int[][] getBeakerMatris() {
        return this.beakerModel.getBeakerMatris();
    }

    /**
     * 获取垂直页边距
     */
    @Override
    public int getMarginVertical_Pix() {
        return this.beakerModel.getMarginVertical_Pix();
    }

    /**
     * 获取水平页边距
     */
    @Override
    public int getMarginHorizontal_Pix() {
        return this.beakerModel.getMarginHorizontal_Pix();
    }

    /**
     * 获取每一个格子的像素数
     */
    @Override
    public int getSquareSide_Pix() {
        return this.beakerModel.getSquareSide_Pix();
    }

    /**
     * 这是一个边长和两个格子间隔一半的和
     */
    @Override
    public int getSideAddSpace_Pix() {
        return this.beakerModel.getSideAddSpace_Pix();
    }

    /**
     * TetrisBeaker初始化尺寸完成了getWidth和getHeight可以获取到正确的值了
     */
    @Override
    public void initSizeOver() {
        this.beakerModel.init_Matris_Margins(this.tetrisBeaker.getWidth(), this.tetrisBeaker.getHeight());
    }
    //endregion

    //region IModelGetData

    /**
     * 从控件中获取用户(默认)设定的每一个俄罗斯方块组成块的边长，单位英寸
     */
    @Override
    public float getSquareSide_Inch() {
        return this.tetrisBeaker.getSquareSide_Inch();
    }

    /**
     * 从控件中获取用户(默认)设定的每一个俄罗斯方块之间的间距，单位像素
     */
    @Override
    public int getSquareSpace_Pix() {
        return this.tetrisBeaker.getSquareSpace_Pix();
    }
    //endregion
}
