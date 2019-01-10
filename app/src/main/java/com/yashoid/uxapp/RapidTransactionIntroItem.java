package com.yashoid.uxapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RapidTransactionIntroItem extends RecyclerView.ViewHolder {

    public static RapidTransactionIntroItem newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rapidtransactions_intro, parent, false);

        return new RapidTransactionIntroItem(view);
    }

    private RapidTransactionIntroItem(View itemView) {
        super(itemView);
    }
}
