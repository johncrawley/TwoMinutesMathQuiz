package com.jcrawley.tmmq.view.fragments.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

public class ColorUtils {


    public static int getColorFromAttribute(int attr, Context context){
        if (context == null){
            return Color.BLACK;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
