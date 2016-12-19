package com.kyudong.termi;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyudong.termi.Home.ExplainViewPagerAdapter;

/**
 * Created by Kyudong on 16. 12. 14..
 */
public class ExplainCustomDialog extends android.support.v4.app.DialogFragment {

    ViewPager viewpager;
    ExplainViewPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.explain_custom_dialog, container, false);

        viewpager = (ViewPager) v.findViewById(R.id.explainViewPager);
        adapter = new ExplainViewPagerAdapter(getChildFragmentManager());

        viewpager.setAdapter(adapter);

        return v;
    }
}
