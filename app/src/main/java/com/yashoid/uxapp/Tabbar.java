package com.yashoid.uxapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class Tabbar extends ConstraintLayout implements View.OnClickListener, Tabs {

    private View mButtonHome;
    private View mButtonCampaign;
    private View mButtonRapidTransaction;
    private View mButtonReports;
    private View mButtonMessages;

    private int mTabSelectedColor;
    private int mTabUnselectedColor;

    private int mTab;

    public Tabbar(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public Tabbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public Tabbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mTabSelectedColor = ContextCompat.getColor(context, R.color.tab_selected);
        mTabUnselectedColor = ContextCompat.getColor(context, R.color.tab_unselected);

        Resources res = getResources();

        ViewCompat.setElevation(this, res.getDimension(R.dimen.elevation));
//        setBackgroundColor(ContextCompat.getColor(context, R.color.windowBackground));
        setBackgroundColor(0xffffffff);
        setPadding(0, res.getDimensionPixelSize(R.dimen.tabbar_padding_top), 0, res.getDimensionPixelSize(R.dimen.tabbar_padding_bottom));

        LayoutInflater.from(context).inflate(R.layout.tabbar, this, true);

        mButtonHome = findViewById(R.id.button_home);
        mButtonCampaign = findViewById(R.id.button_campaign);
        mButtonRapidTransaction = findViewById(R.id.button_rapid_transactions);
        mButtonReports = findViewById(R.id.button_reports);
        mButtonMessages = findViewById(R.id.button_messages);

        mButtonHome.setOnClickListener(this);
        mButtonCampaign.setOnClickListener(this);
        mButtonRapidTransaction.setOnClickListener(this);
        mButtonReports.setOnClickListener(this);
        mButtonMessages.setOnClickListener(this);

        onTabChanged(TAB_HOME);

        ((ImageView) mButtonHome.findViewById(R.id.image_icon)).setImageResource(R.drawable.tab_home);
        ((ImageView) mButtonCampaign.findViewById(R.id.image_icon)).setImageResource(R.drawable.tab_campaign);
        ((ImageView) mButtonRapidTransaction.findViewById(R.id.image_icon)).setImageResource(R.drawable.tab_rapid_transaction);
        ((ImageView) mButtonReports.findViewById(R.id.image_icon)).setImageResource(R.drawable.tab_reports);
        ((ImageView) mButtonMessages.findViewById(R.id.image_icon)).setImageResource(R.drawable.tab_messages);

        ((AppCompatTextView) mButtonHome.findViewById(R.id.text_name)).setText("خانه");
        ((AppCompatTextView) mButtonCampaign.findViewById(R.id.text_name)).setText("جایزه و کمپین");
        ((AppCompatTextView) mButtonRapidTransaction.findViewById(R.id.text_name)).setText("تکرار تراکنش");
        ((AppCompatTextView) mButtonReports.findViewById(R.id.text_name)).setText("گزارش ها");
        ((AppCompatTextView) mButtonMessages.findViewById(R.id.text_name)).setText("ارتباط با ما");
    }

    private void notifyTabChanged() {
        ((TabChangeReceiver) getContext()).onTabChanged(mTab);
    }

    @Override
    public void onClick(View v) {
        int tab = 0;

        switch (v.getId()) {
            case R.id.button_home:
                tab = TAB_HOME;
                break;
            case R.id.button_campaign:
                tab = TAB_CAMPAIGN;
                break;
            case R.id.button_rapid_transactions:
                tab = TAB_RAPID_TRANSACTION;
                break;
            case R.id.button_reports:
                tab = TAB_REPORTS;
                break;
            case R.id.button_messages:
                tab = TAB_MESSAGES;
                break;
        }

        if (tab == getTab()) {
            return;
        }

        onTabChanged(tab);
        notifyTabChanged();
    }

    public int getTab() {
        return mTab;
    }

    private void onTabChanged(int tab) {
        mTab = tab;

        setUnselected(mButtonHome);
        setUnselected(mButtonCampaign);
        setUnselected(mButtonRapidTransaction);
        setUnselected(mButtonReports);
        setUnselected(mButtonMessages);

        switch (tab) {
            case TAB_HOME:
                setSelected(mButtonHome);
                return;
            case TAB_CAMPAIGN:
                setSelected(mButtonCampaign);
                return;
            case TAB_RAPID_TRANSACTION:
                setSelected(mButtonRapidTransaction);
                return;
            case TAB_REPORTS:
                setSelected(mButtonReports);
                return;
            case TAB_MESSAGES:
                setSelected(mButtonMessages);
                return;
        }
    }

    private void setSelected(View button) {
        ((ImageView) button.findViewById(R.id.image_icon)).setColorFilter(mTabSelectedColor, PorterDuff.Mode.SRC_IN);
        ((AppCompatTextView) button.findViewById(R.id.text_name)).setTextColor(mTabSelectedColor);
    }

    private void setUnselected(View button) {
        ((ImageView) button.findViewById(R.id.image_icon)).setColorFilter(mTabUnselectedColor, PorterDuff.Mode.SRC_IN);
        ((AppCompatTextView) button.findViewById(R.id.text_name)).setTextColor(mTabUnselectedColor);
    }

}
