package com.yahia.healthysiabires.partage.view.floatingactionbutton;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.yahia.healthysiabires.partage.Helper;
import com.github.clans.fab.FloatingActionButton;

public class FloatingActionButtonusin {

    private static final String TAG = FloatingActionButtonusin.class.getSimpleName();

    public static FloatingActionButton createFloatingActionButton(Context context, String text, @DrawableRes int imageResourceId, @ColorInt int color) {
        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab.setLabelText(text);
        try {
            fab.setImageResource(imageResourceId);
        } catch (Resources.NotFoundException exception) {
            Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Resources.NotFoundException: " + text);
        }
        fab.setColorNormal(color);
        float brighteningPercentage = color == Color.WHITE ? .9f : 1.2f;
        int colorHighlight = Helper.colorBrighten(color, brighteningPercentage);
        fab.setColorPressed(colorHighlight);
        fab.setColorRipple(colorHighlight);
        return fab;
    }
}
