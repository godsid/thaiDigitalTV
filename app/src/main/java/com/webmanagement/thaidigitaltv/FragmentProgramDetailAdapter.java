package com.webmanagement.thaidigitaltv;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by SystemDLL on 23/12/2557.
 */
public class FragmentProgramDetailAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = GlobalVariable.getMAX_DAY_OF_MONTH();

    public FragmentProgramDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
    Log.d("run","arg0 "+arg0);
        return new FragmentProgramDetail(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_COUNT;
    }




}
