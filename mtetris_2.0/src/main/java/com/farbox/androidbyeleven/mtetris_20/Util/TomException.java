package com.farbox.androidbyeleven.mtetris_20.Util;

/**
 杨铭 Created by kys_8 on 16/9/8,0008. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */
public class TomException
{
    /**
     直接抛出一个异常
     */
    public TomException(String sth)
    {
        try
        {
            throw new Exception("这种设置就想开玩笑一样，编译器不接受，并向您抛出了一个异常:" + sth);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public TomException()
    {
        try
        {
            throw new Exception("这种设置就想开玩笑一样，编译器不接受，并向您抛出了一个异常:");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
