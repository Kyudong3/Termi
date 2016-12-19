package com.kyudong.termi.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyudong.termi.R;

/**
 * Created by Kyudong on 16. 12. 14..
 */
public class ExplainFragment1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.explain_fragment1,container,false);

        return v;
    }
}
