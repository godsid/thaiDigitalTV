/**
 *
 * Sample source code for AllShare Framework SDK
 *
 * Copyright (C) 2012 Samsung Electronics Co., Ltd.
 * All Rights Reserved.
 *
 * @file SampleLauncher.java
 * @date May 10, 2012
 *
 */

package com.webmanagement.thaidigitaltv;

import java.util.Comparator;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceConnector.IServiceConnectEventListener;
import com.sec.android.allshare.ServiceConnector.ServiceState;
import com.sec.android.allshare.ServiceProvider;

/**
 * @brief default sample launcher activity.
 */
public class SampleLauncher extends ListActivity
{

	/** Called when the activity is first created. */

	private static final int WAITING_PROGRESS = 102;
	private static final int FRAMEWORK_NOT_INSTALLED = 103;
	private final TreeMap<String, Intent> mPracticeMap = new TreeMap<String, Intent>();

	private void loadGroupActivities()
	{

	//	mPracticeMap.put(getString(R.string.group4_tv_control), new Intent(this,Group1_1_DeviceContentBrowser_Activity.class).setAction(SampleMediaShareIntent.ACTION_REQUEST_CONTROLLER));
	//	mPracticeMap.put(getString(R.string.group5_PlayShare), new Intent(this,Group5_PlayShare_Activity.class));


	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);


		// first of all, try to connect allshare framework.
		// and waiting till receiving ServiceState.ENABLED event.
        showDialog(WAITING_PROGRESS);
        ERROR error = ServiceConnector.createServiceProvider(this, new IServiceConnectEventListener() {

            @Override
            public void onCreated(ServiceProvider sprovider, ServiceState state) {

                // now we can use allshare service components..
                removeDialog(WAITING_PROGRESS);
                Config.setServiceProvider(sprovider);
            }

            @Override
            public void onDeleted(ServiceProvider sprovider) {

                removeDialog(WAITING_PROGRESS);
                // fail to connect allshare service.
                if (!SampleLauncher.this.isFinishing())
                    Toast.makeText(SampleLauncher.this, getString(R.string.fail_to_connect_allshare_service), Toast.LENGTH_SHORT)
                            .show();
            }
        });
        if(error == ERROR.FRAMEWORK_NOT_INSTALLED){
            removeDialog(WAITING_PROGRESS);
            showDialog(FRAMEWORK_NOT_INSTALLED);
        }
        loadGroupActivities();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
																android.R.layout.simple_list_item_1);
		for (String s : mPracticeMap.keySet())
			adapter.add(s);

		setListAdapter(adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                String value = (String) getListAdapter().getItem(position);
                Intent intent = mPracticeMap.get(value);
                if (intent == null) return;

                Bundle bundle = new Bundle();
                if (position == 3) {
                    bundle.putString("tv_control", "tv_control");
                    intent.putExtras(bundle);
                }

                startActivity(intent);

            }
        });
	}

	@Override
	protected void onDestroy()
	{

		if (Config.mServiceProvider != null && isFinishing() == true)
			ServiceConnector.deleteServiceProvider(Config.mServiceProvider);

		BitmapCache.getBitmapCache().clear();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{

		super.onStart();
		Config.reset();
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{

		switch (id)
		{
			case WAITING_PROGRESS:
			{
				ProgressDialog pd = new ProgressDialog(this);
				pd.setMessage(getString(R.string.connecting_to_allsahre_service));
				pd.setCancelable(false);
				pd.setButton(Dialog.BUTTON_NEUTRAL, getString(R.string.cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{

						Toast.makeText(SampleLauncher.this, getString(R.string.fail_to_connect_allshare_service), Toast.LENGTH_SHORT)
							 .show();
						removeDialog(WAITING_PROGRESS);
						finish();
					}
				});
				return pd;
			}
			case FRAMEWORK_NOT_INSTALLED:
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                alertDialog.setTitle(R.string.framework_not_installed_title);
                alertDialog.setMessage(R.string.framework_not_installed_message);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton(R.string.framework_not_installed_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

				return alertDialog.create();
			}
			default:
				return super.onCreateDialog(id);
		}
	}
}// end of class
