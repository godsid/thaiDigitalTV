package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by WhiteCrows on 9/10/2014.
 */
public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {
    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }
}
