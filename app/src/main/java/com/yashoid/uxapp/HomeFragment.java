package com.yashoid.uxapp;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final int[] SERVICE_IDS = {
            R.id.service_1, R.id.service_2,
            R.id.service_3, R.id.service_4,
            R.id.service_5, R.id.service_6,
            R.id.service_7, R.id.service_8,
            R.id.service_9, R.id.service_10
    };

    private static final int[] SERVICE_IMAGES = {
            R.drawable.s_balance, R.drawable.s_bill,
            R.drawable.s_car, R.drawable.s_charge,
            R.drawable.s_charity, R.drawable.s_compound,
            R.drawable.s_transfer, R.drawable.s_ticket,
            R.drawable.s_internet, R.drawable.s_insurance
    };

    private static final String[] SERVICE_NAMES = {
            "موجودی", "پرداخت قبض",
            "خودرو", "شارژ سیم کارت",
            "نیکوکاری", "بسته ترکیبی",
            "کارت به کارت", "خرید بلیت",
            "بسته اینترنتی", "بیمه"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        for (int i = 0; i < SERVICE_IDS.length; i++) {
            View v = view.findViewById(SERVICE_IDS[i]);
            loadItem(v, i);
        }
    }

    private void loadItem(View v, int index) {
        ((TextView) v.findViewById(R.id.text_name)).setText(SERVICE_NAMES[index]);
        ((ImageView) v.findViewById(R.id.image_icon)).setImageResource(SERVICE_IMAGES[index]);
        v.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        getToolbar().showSettings();
        getToolbar().showGame();
    }

    @Override
    public void onClick(View v) {
        int service = getServiceForId(v.getId());

        switch (service) {
            case R.drawable.s_charge:
                startActivity(new Intent(getContext(), ChargeActivity.class));
                return;
        }
    }

    private int getServiceForId(int id) {
        for (int i = 0; i < SERVICE_IDS.length; i++) {
            if (SERVICE_IDS[i] == id) {
                return SERVICE_IMAGES[i];
            }
        }

        return 0;
    }

}
