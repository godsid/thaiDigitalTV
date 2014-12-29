package com.webmanagement.thaidigitaltv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;


public class SplashScreenActivity extends Activity {

    Context context;
    private DatabaseAction dbAction;
    int versionFromLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = SplashScreenActivity.this;
        dbAction = new DatabaseAction(context);
        openFirst();

    }

    private void getVersionFromLocal() {
        SQLiteCursor cur = (SQLiteCursor) dbAction.readVersion();
        if(cur.getCount() == 0) {
            versionFromLocal = 0;
        } else {
            versionFromLocal = cur.getInt(0);
        }
        Log.d("run", "Ver From Local : " + versionFromLocal);
        cur.close();
    }

    private void openFirst() {
        chkConnectToInternet();
        getVersionFromLocal();
        if (!GlobalVariable.isOnlineMode() && versionFromLocal == 0) {
            showAlertDialog();
        } else{
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }


    private void showAlertDialog() {
        String s = "กรุณาเชื่อมต่อกับเครือข่ายในครั้งแรก";
        AlertDialog.Builder builder = GlobalVariable.simpleDialogTemplate(context, "เกิดข้อผิดพลาด", s);
        builder.setNegativeButton("ลองอีกครั้ง",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        openFirst();
                    }
                });
        builder.setPositiveButton("ออก",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.show();

    }


    public void chkConnectToInternet() {
        boolean b = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        b = true;
                    }

        } else {
            b = false;
        }
        GlobalVariable.setOnlineMode(b);
    }

    @Override
    protected void onDestroy() {
        Log.d("run", "onDestroy : SplashScreenActivity");
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
     //   handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {


    }
}
