package com.yashoid.uxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class NotificationsFragment extends BaseFragment {

    private Message[] mMessages = new Message[] {
            new Message("فرصت ویژه افزایش شانس", "1397/05/20", "کاربر گرامی شما 10 شانس در قرعه کشی 500 میلیونی هف هشتاد دارید. تا شنبه شب فرصت افزایش شانس چند برابر دارید. هر خرید شارژ، بسته اینترنت و پرداخت قبض 20 شانس در قرعه کشی. فقط 11 روز مانده", null),
            new Message("روز هف هشتادی", "1397/05/03", "همراهان عزیز اپلیکیشن هف هشتاد، تا امشب فرصت دارید با هر تراکنش (خرید شارژ، بسته اینترنت، پرداخت قبض) 5 شانس در قرعه کشی 500 میلیون تومان بگیرید.", null),
            new Message("اینترنت رایتل", "1397/01/14", "هف هشتادی عزیز، امکان خرید بسته های اینترنت اپراتور رایتل در اپلیکیشن هف هشتاد، از این لحظه در دسترس میباشد.", "بسته اینترنتی"),
            new Message("جایزه ویژه", "1396/11/29", "هف هشتادی های عزیز، با امید سپری کردن تعطیلاتی سرشار از شادی، به اطلاع میرساند تا پایان امشب با هر تراکنش (خرید شارژ، بسته اینترنت، پرداخت قبض) 5 شانس در قرعه کشی 500 میلیون تومان میگیرید. روزهای عادی با ثبت نام هر 100 امتیاز، یک شانس در قرعه کشی میگیرید.", null)
    };

    private ListView mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mList = view.findViewById(R.id.list);

        mList.setAdapter(mAdapter);
    }

    private ListAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return 2 + mMessages.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                case 0:
                    return getLayoutInflater().inflate(R.layout.item_newversion, parent, false);
                case 1:
                    return getLayoutInflater().inflate(R.layout.item_invite, parent, false);
            }

            View v = getLayoutInflater().inflate(R.layout.item_message, parent, false);
            mMessages[position - 2].setup(v);

            return v;
        }

    };

    private class Message {

        private String title;
        private String date;
        private String text;
        private String buttonText = null;

        public Message(String title, String date, String text, String buttonText) {
            this.title = title;
            this.date = date;
            this.text = text;
            this.buttonText = buttonText;
        }

        private void setup(View v) {
            ((TextView) v.findViewById(R.id.text_title)).setText(title);
            ((TextView) v.findViewById(R.id.text_date)).setText(date);
            ((TextView) v.findViewById(R.id.text)).setText(text);

            TextView button = v.findViewById(R.id.button);

            if (buttonText == null) {
                button.setVisibility(View.GONE);
            }
            else {
                button.setVisibility(View.VISIBLE);
                button.setText(buttonText);
            }
        }

    }

}
