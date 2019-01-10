package com.yashoid.uxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessagesFragment extends BaseFragment {

    private TabHost mTabHost;
    private ViewPager mPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTabHost = view.findViewById(R.id.tabhost);
        mPager = view.findViewById(R.id.pager);

        mPager.setAdapter(new Adapter(getChildFragmentManager()));

        mTabHost.setupPager(mPager);
        mPager.setCurrentItem(1);
    }

    @Override
    public void onStart() {
        super.onStart();

        getToolbar().setVisibility(View.GONE);
    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ContactFragment();
                case 1:
                    return new NotificationsFragment();
            }

            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ارتباط با ما";
                case 1:
                    return "پیام ها";
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
