package com.yashoid.uxapp;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bahram on 10/28/17.
 */

public class StringUtils {

    private static final String TAG = "StringUtils";

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;

    public static String[] persianNumbers =
        new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};

    private static String[] englishNumbers =
            new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    static {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    }

    public static boolean isPersian(String text) {
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                return false;
            }
        }
        return true;
    }
    public static String toPersianNumber(float number) {
        return toPersianNumber(number + "");
    }
    public static String toPersianNumber(long number) {
        return toPersianNumber(number + "");
    }

    public static String toPersianNumber(int number) {
        return toPersianNumber(String.valueOf(number));
    }
    public static String toPersianNumber(String number , boolean needThousandSeparator) {
        if (needThousandSeparator)
            return addThousandSeparator(toPersianNumber(number));
        else
            return toPersianNumber(String.valueOf(number));
    }
    public static String toPersianNumber(int number , boolean needThousandSeparator) {
        if (needThousandSeparator)
            return addThousandSeparator(toPersianNumber(String.valueOf(number)));
        else
            return toPersianNumber(String.valueOf(number));
    }
    public static String toPersianNumber(long number , boolean needThousandSeparator) {
        if (needThousandSeparator)
            return addThousandSeparator(toPersianNumber(String.valueOf(number)));
        else
            return toPersianNumber(String.valueOf(number));
    }
    public static String toPersianNumber(String text) {
        if (text.length() == 0)
            return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            }
            else
                out += c;
        }
        return out;
    }

    public static String toEnglishNumber(int number) {
        return toEnglishNumber(String.valueOf(number));
    }

    public static String toEnglishNumber(String text) {
        if (text.length() == 0)
            return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('۰' <= c && c <= '۹') {
                int number = Integer.parseInt(String.valueOf(c));
                out += englishNumbers[number];
            } else if (c == '٫' || c == '،')
                out += ',';
            else
                out += c;
        }
        return out;
    }

//    public static SpannableString getSpannable(Context context, CharSequence title) {
//        SpannableString span = new SpannableString(title);
//        Object typeFaceSpan = new TypefaceSpan(FontHolder.getInstance(context).getFont(FontHolder.TESHRIN));
//
//        span.setSpan(typeFaceSpan, 0, span.length(), Spannable.SPAN_PARAGRAPH);
//
//        return span;
//    }

    @NonNull
    public static String addThousandSeparator(int s) {
        return addThousandSeparator(String.valueOf(s));
    }

    @NonNull
    public static String addThousandSeparator(long s) {
        return addThousandSeparator(String.valueOf(s));
    }

    @NonNull
    public static String addThousandSeparator(String s) {
        if (s == null) return "";
        boolean isNegative = false;
        if (s.startsWith("-")) {
            isNegative = true;
        }
        StringBuilder f = new StringBuilder();
        String temp = s.replaceAll("[^0-9۰-۹]+", "");
        for (int i = 0; i < temp.length(); ++i) {
            f.append(temp.charAt(i));
            if (((temp.length() - 1) - i) % 3 == 0 && ((temp.length() - 1) - i) != 0) {
                f.append(",");
            }
        }
        String result = f.toString();
        if (isNegative) {
            result = result + "-";
        }
        return toPersianNumber(result);
    }

    public static ArrayList<String> getNumbers(String s) {
        ArrayList<String> numbers = new ArrayList<>(12);

        StringBuilder sb = null;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean characterIsDigit = c >= '0' && c <= '9';

            if (sb == null) {
                if (!characterIsDigit) {
                    continue;
                }

                sb = new StringBuilder();
                sb.append(c);
            } else {
                if (characterIsDigit) {
                    sb.append(c);
                } else {
                    numbers.add(sb.toString());
                    sb = null;
                }
            }
        }

        return numbers;
    }


    @NonNull
    public static String removeLeadingZeros(String number) {
        if (null == number || number.length() == 0)
            return "";
        int leadingZeroCount = 0;
        for (int i = 0; i < number.length(); i++) {
            if ('0' == number.charAt(i) || '۰' == number.charAt(i))
                leadingZeroCount++;
            else
                break;
        }
        return number.substring(leadingZeroCount, number.length());
    }

    public static boolean validateNationId(String nationId) {
        if (nationId.equals(""))
            return true;
        if (nationId.length() != 10)
            return false;
        if (nationId.equals("1111111111") || nationId.equals("2222222222") || nationId.equals("3333333333")
                || nationId.equals("4444444444") || nationId.equals("5555555555") || nationId.equals("6666666666")
                || nationId.equals("7777777777") || nationId.equals("8888888888") || nationId.equals("9999999999"))
            return false;
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(nationId.charAt(i)) * (10 - i);
        }
        int remain = sum % 11;
        int checkDigit = -1;
        if (remain < 2) {
            checkDigit = remain;
        } else {
            checkDigit = 11 - remain;
        }
        return Character.getNumericValue(nationId.charAt(9)) == checkDigit;
    }

    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private static String formatToMilitary(int i) {
        return i < 9 ? "0" + i : String.valueOf(i);
    }

    @NonNull
    public static String getCommaSeparatedValue(List<String> list) {
        StringBuilder csvBuilder = new StringBuilder();
        String separator = ", ";

        for (String city : list) {
            csvBuilder.append(city);
            csvBuilder.append(separator);
        }

        String csv = csvBuilder.toString();

        //Remove last comma
        csv = csv.substring(0, csv.length() - separator.length());

        return csv;
    }

}
