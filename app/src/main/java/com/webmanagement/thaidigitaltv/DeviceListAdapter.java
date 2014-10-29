package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 25/9/2557.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sec.android.allshare.Device;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Banpot.S on 9/4/14 AD.
 */
public class DeviceListAdapter extends BaseAdapter {

    ArrayList<DataCustomDeviceListAdapter> arrayList = new ArrayList<DataCustomDeviceListAdapter>();
    private LayoutInflater mInflater;

    private Device device;


    public DeviceListAdapter(Context context, ArrayList<DataCustomDeviceListAdapter> arrayList) {
        this.arrayList = arrayList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_device_list, null);
        }

        ImageView IC_device = (ImageView) convertView.findViewById(R.id.ic_device);
        TextView TV_device_name = (TextView) convertView.findViewById(R.id.tv_device_name);
        TextView TV_device_model = (TextView) convertView.findViewById(R.id.tv_device_model);
        TextView TV_device_ip = (TextView) convertView.findViewById(R.id.tv_device_ip);

        Uri icon = arrayList.get(position).icon;
        TV_device_name.setText(arrayList.get(position).name);
        TV_device_model.setText(arrayList.get(position).model);
        TV_device_ip.setText(arrayList.get(position).ip);

        if( icon == null )
        {
            // no thumbnail.
            IC_device.setImageResource(R.drawable.ic_tv);
            return convertView;
        }

        Bitmap bitmap = BitmapCache.getBitmapCache().get( icon.toString() );
        DownloadThumbnailTask downloader = (DownloadThumbnailTask)IC_device.getTag();

        if( bitmap == null || bitmap.isRecycled() )
        {
            // if there is already assigned job for this image view, cancel it.
            if( downloader != null )
            {
                if( downloader.getUri() != null && downloader.getUri().equals( icon ) )
                {
                    // already downloaded image.
                    // do nothing.
                }
                else
                {
                    IC_device.setImageDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                    downloader.cancel( true );
                    downloader = new DownloadThumbnailTask( IC_device, icon );
                    downloader.execute();
                    IC_device.setTag( downloader );
                }
            }
            else
            {
                IC_device.setImageDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                downloader = new DownloadThumbnailTask( IC_device, icon );
                downloader.execute();
                IC_device.setTag( downloader );
            }
        }
        else
        {
            if( downloader != null )
            {
                downloader.cancel( true );
            }
            IC_device.setImageBitmap(bitmap);
        }



        return convertView;
    }


}


class DataCustomDeviceListAdapter {
    String  name, model,ip;
    Uri icon;

    public DataCustomDeviceListAdapter(Uri icon, String name, String model, String ip) {
        this.icon = icon;
        this.name = name;
        this.model = model;
        this.ip = ip;
    }


}




