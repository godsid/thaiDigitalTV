package com.webmanagement.thaidigitaltv;

import com.sec.android.allshare.Device;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by SystemDLL on 20/10/2557.
 */
public class DeviceListAdapter  extends ArrayAdapter<Device>
{


    public DeviceListAdapter( Context context )
    {
        super( context, R.layout.item_device_list );

    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {

        TextView tv;
        TextView model;
        TextView Ip;
        ImageView iv;

        if( convertView == null )
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate( R.layout.item_device_list, null );


        }

        tv = (TextView)convertView.findViewById( R.id.title );
        iv = (ImageView)convertView.findViewById( R.id.icon );
        model = (TextView)convertView.findViewById( R.id.model );
        Ip = (TextView)convertView.findViewById( R.id.ip);

        Device device = getItem( position );
        tv.setText( "[" + device.getNIC() + "]" + device.getName() );
        model.setText( device.getModelName() );
        Ip.setText( device.getIPAddress() );

        // check cache.
        Uri uri = device.getIcon();

        if( uri == null )
        {
            // no thumbnail.
            iv.setImageDrawable( new ColorDrawable( Color.TRANSPARENT ) );
            return convertView;
        }

        Bitmap bitmap = BitmapCache.getBitmapCache().get( uri.toString() );
        DownloadThumbnailTask downloader = (DownloadThumbnailTask)iv.getTag();

        if( bitmap == null || bitmap.isRecycled() )
        {
            // if there is already assigned job for this image view, cancel it.
            if( downloader != null )
            {
                if( downloader.getUri() != null && downloader.getUri().equals( uri ) )
                {
                    // already downloaded image.
                    // do nothing.
                }
                else
                {
                    iv.setImageDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                    downloader.cancel( true );
                    downloader = new DownloadThumbnailTask( iv, uri );
                    downloader.execute();
                    iv.setTag( downloader );
                }
            }
            else
            {
                iv.setImageDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                downloader = new DownloadThumbnailTask( iv, uri );
                downloader.execute();
                iv.setTag( downloader );
            }
        }
        else
        {
            if( downloader != null )
            {
                downloader.cancel( true );
            }
            iv.setImageBitmap( bitmap );
        }

        return convertView;
    }

}
