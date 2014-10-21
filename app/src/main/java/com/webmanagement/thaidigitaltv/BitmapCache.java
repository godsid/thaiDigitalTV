package com.webmanagement.thaidigitaltv;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by SystemDLL on 20/10/2557.
 */
public class BitmapCache extends LinkedHashMap<String, Bitmap>
{
    private static final long serialVersionUID = -3816559757724495319L;

    int mCapacity = 0;

    public BitmapCache( int capacity )
    {
        super( capacity, 0.5f, true );
        mCapacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry( java.util.Map.Entry<String, Bitmap> eldest )
    {
        if( size() > mCapacity )
        {
            Bitmap bitmap = eldest.getValue();
            if( bitmap != null )
            {
                //bitmap.recycle();
                bitmap = null;
            }
            return true;
        }
        return false;
    }

    static private Map<String, Bitmap> mCache = new BitmapCache( 50 ); // store maximum 50 images.
    static
    {
        mCache = Collections.synchronizedMap( mCache );
    }

    @Override
    public Bitmap put(String key, Bitmap value)
    {
        // TODO Auto-generated method stub
        Bitmap mBitMap = super.put(key, value);

        if(mBitMap != null)
        {
            //mBitMap.recycle();
            mBitMap = null;
        }

        return value;
    }

    @Override
    public Bitmap get( Object key )
    {
        try
        {
            Bitmap bitmap = super.get( key );
            if( bitmap != null && bitmap.isRecycled())
            {
                // already recycled bitmap.
                remove( key );
                bitmap = null;
                return null;
            }

            return super.get( key );
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static Map<String, Bitmap> getBitmapCache()
    {
        return mCache;
    }

}// end of class
