package com.yashoid.shifter.tabbar;

import android.net.Uri;

public class Tab {

    private String mId;
    private String mTitle;
    private int mIconResId;
    private Uri mActionUri;

    public Tab(String id, String title, int iconResId, String action) {
        mId = id;
        mTitle = title;
        mIconResId = iconResId;
        mActionUri = Uri.parse(action);
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getIconResId() {
        return mIconResId;
    }

}
