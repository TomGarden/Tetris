package com.farbox.androidbyeleven.Controller.V2M.SuperServer;

/**
 * Created by Tom on 2016/11/8.
 * <p>Email:771365380@qq.com</p>
 * <p>Mobile phone:15133350726</p>
 */
public interface ITetrisGetterService extends IDataGetter {
    /**
     * 获取本控件对应的矩阵的String类型对象//保存内容的格式：行数:列数：矩阵内容String
     */
    String getMatris2Str();
}
