package com.yashoid.uxapp;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    protected Toolbar getToolbar() {
        return ((MainActivity) getActivity()).getToolbar();
    }

}
