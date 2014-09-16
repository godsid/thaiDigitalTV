package com.webmanagement.thaidigitaltv;

import android.content.Context;
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

public class ExpandableListAdapter_Right extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<GroupExpLeft> groups;

    AQuery aq;

    public ExpandableListAdapter_Right(Context context, ArrayList<GroupExpLeft> groups) {
        this.context = context;
        this.groups = groups;
        aq = new AQuery(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ItemExpLeft> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ItemExpLeft child = (ItemExpLeft) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exp_left_list_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_list_item_left);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_list_item_left);


        tv.setText(child.getName().toString());
        aq.id(iv).image(child.getImage());
        //iv.setImageResource(child.getImage());

        //aq.id(itemView.place).text("at " + jsonItem.getString("title"));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ItemExpLeft> chList = groups.get(groupPosition).getItems();
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


        GroupExpLeft group = (GroupExpLeft)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.exp_left_list_group, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_list_group_left);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_list_group_left);


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
