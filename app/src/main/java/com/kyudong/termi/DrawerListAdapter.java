package com.kyudong.termi;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyudong on 16. 11. 20..
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerTitle> {

    private Context mContext;
    private int mResource;
    private ArrayList<DrawerTitle> mArrayList;
    private LayoutInflater mInflater;

    public DrawerListAdapter(Context context, int resource, ArrayList<DrawerTitle> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mArrayList = objects;
        this.mInflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        DrawerTitle drawerTitle = mArrayList.get(position);

        if(v==null){
            v = mInflater.inflate(R.layout.drawer_list_item,null);
        }
        if(drawerTitle !=null){
            TextView tv = (TextView)v.findViewById(R.id.drawerTitle);
            tv.setTextColor(Color.RED);
            //ImageView iv = (ImageView)v.findViewById(R.id.drawerIcon);
            tv.setText(drawerTitle.getTitle());
        }
        return v;
    }
}
