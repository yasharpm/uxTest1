package com.yashoid.shifter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class TextStyle {

    public static TextStyle parseTextStyle(String styleString) {
        TextStyle textStyle = new TextStyle();

        styleString = styleString.trim();

        if (styleString.isEmpty()) {
            return textStyle;
        }

        String[] parts = styleString.split(";");

        if (parts.length > 0) {
            String sizeString = parts[0].trim();

            if (!sizeString.isEmpty()) {
                int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;

                textStyle.mTextSize = Integer.parseInt(sizeString) / 360f * widthPixels;
            }
        }

        if (parts.length > 1) {
            String colorString = parts[1].trim();

            if (!colorString.isEmpty()) {
                textStyle.mTextColor = Color.parseColor(parts[1].trim());
            }
        }

        if (parts.length > 2) {
            String fontString = parts[2].trim();

            if (!fontString.isEmpty()) {
                textStyle.mTypefaceName = parts[2].trim();
            }
        }

        return textStyle;
    }

    private float mTextSize = -1;
    private int mTextColor = 0xff000000;
    private String mTypefaceName = null;

    private TextStyle() {

    }

    public float getTextSize() {
        return mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public String getTypefaceName() {
        return mTypefaceName;
    }

    public void apply(TextView textView) {
        if (mTextSize > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }

        textView.setTextColor(mTextColor);

        if (mTypefaceName != null) {
            Context context = textView.getContext();

            int fontRestId = context.getResources().getIdentifier(mTypefaceName,
                    "font", context.getPackageName());

            if (fontRestId != 0) {
                textView.setTypeface(ResourcesCompat.getFont(context, fontRestId));
            }
        }
    }

}
