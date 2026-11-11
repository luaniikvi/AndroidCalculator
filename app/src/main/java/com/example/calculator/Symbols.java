package com.example.calculator;

import android.content.Context;

public class Symbols {
    public static final String DIVIDE = getContext().getString(R.string.symbol_divide);
    public static final String MULTIPLY = getContext().getString(R.string.symbol_multiply);
    public static final String PERCENT = getContext().getString(R.string.symbol_percent);
    public static final String PLUS = getContext().getString(R.string.symbol_plus);
    public static final String MINUS = getContext().getString(R.string.symbol_minus);
    public static final String CLEAR = getContext().getString(R.string.btn_clear);
    public static final String ALL_CLEAR = getContext().getString(R.string.btn_all_clear);
    public static final String DELETE = getContext().getString(R.string.btn_delete);
    public static final String DOT = getContext().getString(R.string.symbol_dot);
    public static final String EQUAL = getContext().getString(R.string.symbol_equal);



    static Context getContext() {
        return MyApp.getAppContext();
    }
    static String getAllOp(){
        return MULTIPLY +
                DIVIDE +
                PLUS +
                MINUS +
                PERCENT;
    }

    static String getAllOpIgnorePercent(){
        return MULTIPLY +
                DIVIDE +
                PLUS +
                MINUS;
    }
}
