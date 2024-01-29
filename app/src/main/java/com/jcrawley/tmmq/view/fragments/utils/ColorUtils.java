package com.jcrawley.tmmq.view.fragments.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.TypedValue;
import android.widget.TextView;

import com.jcrawley.tmmq.R;

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


    public static void addGradientTo(TextView textView, Context context, boolean isInLandscapeMode){
        float height = isInLandscapeMode ? textView.getTextSize()/1.5f : textView.getTextSize();
        Shader textShader = new LinearGradient(0, textView.getY(), 0, textView.getY() + height,
                new int[]{
                        getColorFromAttribute(R.attr.title_text_color_1, context),
                        getColorFromAttribute(R.attr.title_text_color_2, context),
                        getColorFromAttribute(R.attr.title_text_color_3, context),
                        getColorFromAttribute(R.attr.title_text_color_4, context)
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);
    }


    public static void addGradientTo(TextView textView, Context context){
       addGradientTo(textView, context, false);
    }

}
