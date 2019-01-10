package com.yashoid.uxapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {

    private Input mInputMobile;
    private Input mInputOperator;
    private Input mInputType;
    private Input mInputAmount;
    private RecyclerView mList;
    private View mButtonDone;

    private String mMobile;
    private Integer mOperator;
    private Integer mType;
    private Integer mAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        mInputMobile = findViewById(R.id.input_mobile);
        mInputOperator = findViewById(R.id.input_operator);
        mInputType = findViewById(R.id.input_chargetype);
        mInputAmount = findViewById(R.id.input_amount);
        mList = findViewById(R.id.list);
        mButtonDone = findViewById(R.id.button_done);

        mList.setLayoutManager(new GridLayoutManager(this, 2));
        setAdapter();

        mButtonDone.setOnClickListener(this);
    }

    private void setAdapter() {
        final ArrayList<RapidTransactionItem.Info> infoList = InfoList.getInstance(this).getInfoList();

        mList.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return RapidTransactionItem.newInstance(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                final RapidTransactionItem.Info info = infoList.get(position);
                ((RapidTransactionItem) holder).setInfo(info.title, info.line1, info.line2, info.line3);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInputMobile.setValue((String) info.bundle[0]);
                        mInputOperator.setValueIndex((Integer) info.bundle[1]);
                        mInputType.setValueIndex((Integer) info.bundle[2]);
                        mInputAmount.setValueIndex((Integer) info.bundle[3]);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return infoList.size();
            }
        });
    }

    private void readValues() {
        mMobile = mInputMobile.getValue();
        mOperator = (Integer) mInputOperator.getTag();
        mType = (Integer) mInputType.getTag();
        mAmount = (Integer) mInputAmount.getTag();
    }

    private boolean validateValues() {
        if (TextUtils.isEmpty(mMobile)) {
            Toast.makeText(this, "شماره موبایل به درستی وارد نشده است.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mOperator == null) {
            Toast.makeText(this, "اپراتور انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mType == null) {
            Toast.makeText(this, "نوع شارژ انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mAmount == null) {
            Toast.makeText(this, "مبلغ شارژ انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        readValues();

        if (validateValues()) {
            RapidTransactionItem.Info info = InfoList.getInstance(this).add(mMobile, mOperator, mType, mAmount);

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("info", info.toString());
            startActivity(intent);

            finish();
        }
    }

}
