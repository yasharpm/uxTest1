package com.yashoid.uxapp;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Random;

public class RapidTransactionItem extends RecyclerView.ViewHolder {

    private static final String[][] INFOS = {
            {"بسته اینترنت", "بسته 3 ماهه 12 گیگ", "09124152410", ""},
            {"خرید شارژ", "شارژ مستقیم", "2 هزار تومانی", "09124152410"},
            {"پرداخت قبض", "ش.ق. 4132131241", "قبض برق", ""},
            {"کارت به کارت", "به کارت بانک سامان", "به نام شروین حسینی", "مبلغ 500،000 ریال"},
            {"یاری رسانی", "شیرخوارگاه آمنه", "مبلغ 300،000 ریال", ""}
    };

    public static RapidTransactionItem newInstance(ViewGroup parent) {
        Random rand = new Random();

        String[] info = INFOS[rand.nextInt(INFOS.length)];

        return newInstance(parent, info[0], info[1], info[2], info[3]);
    }

    public static RapidTransactionItem newInstance(ViewGroup parent, String title, String line1, String line2, String line3) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rapidtransaction, parent, false);

        return new RapidTransactionItem(view, new Info(title, line1, line2, line3));
    }

    private TextView mTextTitle;
    private TextView mTextInfo;

    private RapidTransactionItem(View itemView, Info info) {
        super(itemView);

        mTextTitle = itemView.findViewById(R.id.text_title);
        mTextInfo = itemView.findViewById(R.id.text_info);

        if (info != null) {
            setInfo(info.title, info.line1, info.line2, info.line3);
        }
    }

    public void setInfo(String title, String line1, String line2, String line3) {
        mTextTitle.setText(title);

        if (TextUtils.isEmpty(line1)) {
            line1 = "";
        }

        if (TextUtils.isEmpty(line2)) {
            line2 = "";
        }

        if (TextUtils.isEmpty(line3)) {
            line3 = "";
        }

        mTextInfo.setText(line1 + "\n" + line2 + "\n" + line3);
    }

    public static class Info {

        public String title;
        public String line1;
        public String line2;
        public String line3;

        public Object[] bundle;

        public Info(String title, String line1, String line2, String line3) {
            this.title = title;
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!(obj instanceof Info)) {
                return false;
            }

            return Arrays.equals(bundle, ((Info) obj).bundle);
        }

        @Override
        public String toString() {
            return title + "\t\t\t\t\t\t\t\t" + line1 + "\n" + line2 + "\t\t" + line3;
        }

    }

}
