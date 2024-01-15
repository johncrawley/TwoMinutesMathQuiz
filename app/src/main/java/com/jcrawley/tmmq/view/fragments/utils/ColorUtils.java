package com.jcrawley.tmmq.view.fragments.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

public class ColorUtils {


    public static int getColorFromAttribute(int attr, Context context){
        if (context == null){
            return Color.BLACK;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }


    public static void animateTextColor(TextView textView, int startColor, int endColor, int startDelay, int duration){
        ObjectAnimator animateBack = ObjectAnimator.ofInt(textView,
                "textColor",
                startColor,
                endColor);
        animateBack.setEvaluator(new ArgbEvaluator());
        animateBack.setDuration(duration);
        animateBack.setStartDelay(startDelay);
        animateBack.start();
    }


}
