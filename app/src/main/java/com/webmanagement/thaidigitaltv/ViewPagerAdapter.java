package com.webmanagement.thaidigitaltv;

/**
 * Created by SystemDLL on 24/11/2557.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "ช่องทีวี", "หมวดหมู่", "ประเภทรายการ" };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
               // Log.d("run","P "+position);
                return new ListTab1();

            // Open FragmentTab2.java
            case 1:
               // Log.d("run","P "+position);
                return new ListTab2();

            case 2:
                //Log.d("run","P "+position);
                return new ListTab3();
        }
        Log.d("run","P "+position);
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}

