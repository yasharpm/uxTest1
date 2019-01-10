package com.yashoid.uxapp;

import android.content.Context;

import java.util.ArrayList;

public class InfoList {

    private static InfoList mInstance = null;

    public static InfoList getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new InfoList(context);
        }

        return mInstance;
    }

    private Context mContext;

    private ArrayList<RapidTransactionItem.Info> mInfoList = new ArrayList<>();

    private InfoList(Context context) {
        mContext = context;
    }

    public RapidTransactionItem.Info add(String mobile, int operator, int type, int amount) {
        mobile = StringUtils.toPersianNumber(mobile);
        String sType = mContext.getResources().getStringArray(R.array.options_chargetype)[type];
        String sAmount = mContext.getResources().getStringArray(R.array.options_amount)[amount];

        RapidTransactionItem.Info info = new RapidTransactionItem.Info("خرید شارژ", sType, sAmount, mobile);

        info.bundle = new Object[] { mobile, operator, type, amount };

        if (!mInfoList.contains(info)) {
            mInfoList.add(0, info);
        }

        return info;
    }

    public ArrayList<RapidTransactionItem.Info> getInfoList() {
        return mInfoList;
    }

}
