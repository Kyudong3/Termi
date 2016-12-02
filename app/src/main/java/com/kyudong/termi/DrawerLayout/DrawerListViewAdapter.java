package com.kyudong.termi.DrawerLayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kyudong.termi.R;

import java.util.ArrayList;

/**
 * Created by Kyudong on 16. 12. 1..
 */
public class DrawerListViewAdapter extends BaseAdapter {

    private ArrayList<DrawerLVItem> drawerLVItemArrayList = new ArrayList<>();

    @Override
    public int getCount() {
        return drawerLVItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerLVItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);

            viewHolder.menuTxv = (TextView) convertView.findViewById(R.id.listviewTxv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.menuTxv.setText(drawerLVItemArrayList.get(position).menu);

        return convertView;

    }

    public void addItem(String mStr) {
        DrawerLVItem item = new DrawerLVItem();

        item.menu = mStr;

        drawerLVItemArrayList.add(item);
    }

    public class ViewHolder {
        public TextView menuTxv;
    }
}
