package com.kyudong.termi.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.kyudong.termi.InBox.Inbox;
import com.kyudong.termi.OutBox.Outbox;

/**
 * Created by Gangho on 2016-11-19.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;
    private Inbox inbox;
    private Outbox outbox;
    SparseArray<Fragment> registeredFragment = new SparseArray<>();


    public TabPagerAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Inbox();
            case 1:
                return new Outbox();
            default:
                return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    //    @Override
//    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                Inbox recieved_mail = new Inbox();
//                return recieved_mail;
//            case 1:
//                Outbox inbox = new Outbox();
//                return inbox;
//            default:
//                return null;
//        }
//
//    }

    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                inbox = (Inbox) createdFragment;
                break;
            case 1:
                outbox = (Outbox) createdFragment;
                break;
        }
        return createdFragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
