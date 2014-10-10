package com.webmanagement.thaidigitaltv;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 12/9/2557.
 */

public class ListTab2Adapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ItemGroupTab2> groups;

    AQuery aq;

    Typeface TF_font;
    String frontPath = "fonts/RSU_BOLD.ttf";

    public ListTab2Adapter(Context context, ArrayList<ItemGroupTab2> groups) {
        this.context = context;
        this.groups = groups;
        aq = new AQuery(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ItemChildTab2> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ItemChildTab2 child = (ItemChildTab2) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_child_lv_tab_2, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_child_lv_tab2);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_child_lv_tab2);


        tv.setText(child.getName().toString());
        aq.id(iv).image(child.getImage());
        //iv.setImageResource(child.getImage());

        //aq.id(itemView.place).text("at " + jsonItem.getString("title"));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ItemChildTab2> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        ItemGroupTab2 group = (ItemGroupTab2) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.item_group_lv_tab_2, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_iv_group_lv_tab2);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_iv_group_lv_tab2);


        tv.setText(group.getName());
        aq.id(iv).image(group.getImage());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
