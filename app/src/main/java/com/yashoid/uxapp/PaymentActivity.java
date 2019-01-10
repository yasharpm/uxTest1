package com.yashoid.uxapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yashoid.inputformatter.FormattableText;
import com.yashoid.inputformatter.Formatter;
import com.yashoid.inputformatter.InputFormatter;
import com.yashoid.inputformatter.PanInputFormatter;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextDetails;
    private Input mInputCardNumber;
    private Input mInputPass;
    private Input mInputCvv2;
    private Input mInputExpDate;
    private View mButtonPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mTextDetails = findViewById(R.id.text_details);
        mInputCardNumber = findViewById(R.id.input_cardnumber);
        mInputPass = findViewById(R.id.input_pass);
        mInputCvv2 = findViewById(R.id.input_cvv2);
        mInputExpDate = findViewById(R.id.input_expdate);
        mButtonPay = findViewById(R.id.button_done);

        String info = getIntent().getStringExtra("info");
        mTextDetails.setText(StringUtils.toPersianNumber(info));

        mInputCardNumber.getInnerEditText().addTextChangedListener(new InputFormatter(mPanFormatter));
        mInputPass.getInnerEditText().addTextChangedListener(new InputFormatter(mFarsiDigitFormatter));
        mInputCvv2.getInnerEditText().addTextChangedListener(new InputFormatter(mFarsiDigitFormatter));
        mInputExpDate.getInnerEditText().addTextChangedListener(new InputFormatter(mFarsiDigitFormatter));

        mInputCardNumber.getInnerEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(16)});
//        mInputExpDate.getInnerEditText().setFilters(new InputFilter[] { new InputFilter.LengthFilter(5)});

//        setupExpDateEditText();

        mButtonPay.setOnClickListener(this);
    }

    private Formatter mPanFormatter = new Formatter() {

        Formatter panFormatter = makePanFormatter(' ');

        @Override
        public void format(FormattableText text) {
            panFormatter.format(text);
            mFarsiDigitFormatter.format(text);
        }

    };

    private Formatter mFarsiDigitFormatter = new Formatter() {
        @Override
        public void format(FormattableText text) {
            if (text.length() == 0) {
                return;
            }

            for (char c = '0'; c <= '9'; c++) {
                text.replaceAll(c, StringUtils.toPersianNumber("" + c).charAt(0));
            }
        }
    };

    private void setupExpDateEditText() {
        final EditText expEdit = mInputExpDate.getInnerEditText();

        expEdit.addTextChangedListener(new TextWatcher() {

            boolean changedBySelf = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s) && !expEdit.hasFocus()) {
                    return;
                }

                if (changedBySelf) {
                    changedBySelf = false;
                    return;
                }

                changedBySelf = true;

                String mFormattedText = format(s.toString());
                expEdit.setText(mFormattedText);

                String trimmed = mFormattedText.replaceAll("[^0-9۰-۹]", "").trim();

                if (trimmed.length() > 0) {
                    if (!TextUtils.isEmpty(mFormattedText)) {
                        int length = mFormattedText.replaceAll("-", "").length();
                        if (length < 4 && mFormattedText.contains("/")) {
                            length--;
                        }
                        expEdit.setSelection(length);
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

                Toast toast = Toast.makeText(PaymentActivity.this, "تراکنش با موفقیت انجام شد.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                finish();
            }
        }, 3000);

        dialog.show();
    }


    public static String format(String input) {
        input = input.replaceAll("[^0-9۰-۹]", "");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i <= 3; i++) {

            if (i == 2) {
                builder.append("/");
            }

            if (i < input.length()) {
                builder.append(input.charAt(i));
            }
            else {
                builder.append("_");
            }
        }
        return StringUtils.toPersianNumber(builder.toString());
    }

    private static Formatter makePanFormatter(final char separator) {
        return new Formatter() {

            @Override
            public void format(FormattableText text) {
                int len = text.length();

                if (len > 12) {
                    text.insert(12, "" + separator);
                }

                if (len > 8) {
                    text.insert(8, "" + separator);
                }

                if (len > 4) {
                    text.insert(4, "" + separator);
                }
            }

        };
    }

}
