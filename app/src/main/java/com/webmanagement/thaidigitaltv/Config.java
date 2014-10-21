package com.webmanagement.thaidigitaltv;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Item;
import com.sec.android.allshare.ServiceProvider;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Banpot.S on 8/22/14 AD.
 */
public class Config {


    static public final String googleAnalyticID = "UA-40209680-3";

    static void reset()
    {
        mItem = null;
        mFile = null;
        mPositionSecond = 0;
        clearItemList();
    }
    static File mFile = null;

    static void setFile(File file)
    {
        mFile = file;
    }

    public static File getFile()
    {
        return mFile;
    }

    static Item mItem = null;

    static void setItem( Item item )
    {
        mItem = item;
    }

    public static Item getItem()
    {
        return mItem;
    }

    static ArrayList<Item> mItemList = new ArrayList<Item>();


    public static void clearItemList()
    {
        mItemList.clear();
    }

    static String mProviderIface = null;

    static void setProviderInterface( String iface )
    {
        mProviderIface = iface;
    }

    public static String getProviderInterface()
    {
        return mProviderIface;
    }

    static int mPositionSecond = 0;

    static void setCurrentPosition( int currentPositionSecond )
    {
        mPositionSecond = currentPositionSecond;
    }

    public static int getPosition()
    {
        return mPositionSecond;
    }

    static ServiceProvider mServiceProvider = null;

    static void setServiceProvider( ServiceProvider serviceProvider )
    {
        mServiceProvider = serviceProvider;
    }

    static public ServiceProvider getServiceProvider()
    {
        return mServiceProvider;
    }

    static Device mDevice = null;

    static void setDevice( Device device )
    {
        mDevice = device;
    }

    static public Device getDevice()
    {
        return mDevice;
    }

    private Config()
    {}




}
