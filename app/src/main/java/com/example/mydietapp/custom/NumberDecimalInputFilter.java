package com.example.mydietapp.custom;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Pattern;

public class NumberDecimalInputFilter implements InputFilter {
    double min = 0;
    double max = 200;
    Pattern pattern;

    public NumberDecimalInputFilter(double min, double max, int decimalCount) {
        this.min = min;
        this.max = max;
        pattern = Pattern.compile("[0-9]+((\\.[0-9]{0," + decimalCount + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Log.d("filter", source + ", " + start + ", " + end + ", " + dest + ", " + dstart + ", " + dend);

        try {
            // 입력된 문자와 기존 문자 조합
            StringBuilder sb = new StringBuilder(dest.toString());
            sb.insert(dstart, source);
            String strVal = sb.toString();

            Log.d("filter", "val = " + strVal);

            // 형식 검사
            if (!pattern.matcher(strVal).matches())
                return "";

            if ("0".equals(strVal) || "0.".equals(strVal))
                return null;

            // 범위 검사
            double val = Double.parseDouble(strVal);
            if (val > min && val <= max)
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}