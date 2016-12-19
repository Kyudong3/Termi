package com.kyudong.termi.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.kyudong.termi.InBox.Inbox;
import com.kyudong.termi.OutBox.Outbox;

/**
 * Created by Kyudong on 16. 12. 14..
 */
public class ExplainViewPagerAdapter extends FragmentPagerAdapter {

    private ExplainFragment1 explainFragment1;
    private ExplainFragment2 explainFragment2;
    private ExplainFragment3 explainFragment3;

    public ExplainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ExplainFragment1();
            case 1:
                return new ExplainFragment2();
            case 2:
                return new ExplainFragment3();
            default:
                return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                explainFragment1 = (ExplainFragment1) createdFragment;
                break;
            case 1:
                explainFragment2 = (ExplainFragment2) createdFragment;
                break;
            case 2:
                explainFragment3 = (ExplainFragment3) createdFragment;
        }
        return createdFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
