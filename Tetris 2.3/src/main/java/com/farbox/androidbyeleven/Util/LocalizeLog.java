package com.farbox.androidbyeleven.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 杨铭 Created by kys_8 on 16/9/13,0013. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 <p/>
 本地化日志(为了某种需求要把日志记录到本地文件中。)
 */
public class LocalizeLog
{
    public final String logDirectorie = "/logDirectory";

    /**
     创建日志文件
     */
    public void createLogFile(String path)
    {
        File file = new File(path);
        if (!file.exists())
        {
            if (!file.getParentFile().exists())
            {
                File parent = file.getParentFile();
                if (!parent.mkdirs())
                {
                    _Log.i(_Log.msg() + "创建路径失败");
                }
            }
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /** 获取系统当前时间 */
    private String getCurrentTime()
    {
        Date date = new Date();
        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        return format.format(date) + "h.txt";
    }

    /**
     得到当前的日志文件目录

     @param sonPath 子文件夹名字  类似于  /qqq/www/eee

     @return
     */
    public String getLogFilePath(String sonPath)
    {
        //得到存储器路径
        List<GetMountPoint.MountPoint> mountedPoint = GetMountPoint
                .GetMountPointInstance(Global.applicationContext).getMountedPoint();

        String path = mountedPoint.get(mountedPoint.size() - 1).getFilePath();

        path = path + (sonPath == null ? this.logDirectorie : sonPath) + "/" + getCurrentTime();

        if (!(new File(path)).exists())
        {
            createLogFile(path);
        }
        return path;
    }

    public String getLogFilePath()
    {
        return getLogFilePath(null);
    }

    /** 向文件写入内容 */
    public void write2File(String str)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(getLogFilePath(), true);

            fos.write(str.getBytes("UTF-8"));
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /** 写完一句话后自动加回车 */
    public void write2FileEnter(String str)
    {
        write2File(str + "\n");
    }

    /** 单独写入回车 */
    public void write2FileEnter()
    {
        write2File("\n");
    }
}
