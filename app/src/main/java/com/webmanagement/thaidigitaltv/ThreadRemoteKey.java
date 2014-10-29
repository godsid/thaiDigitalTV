package com.webmanagement.thaidigitaltv;

import android.util.Log;

import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.control.TVController.RemoteKey;

public class ThreadRemoteKey implements Runnable {

    private TVController mTVController = null;

    RemoteKey rmk1,rmk2;
    public ThreadRemoteKey(RemoteKey rmk1, RemoteKey rmk2) {
        this.rmk1 = rmk1;
        this.rmk2 = rmk2;
    }

    public void run() {
            try {

                mTVController = (TVController)GlobalVariable.getCurrentDevice();
                    Thread.sleep(100);
                mTVController.sendRemoteKey(rmk1);
                    Thread.sleep(1000);
                mTVController.sendRemoteKey(rmk2);

            } catch (Exception e) {
                System.out.println(e);
            }

    }
}
