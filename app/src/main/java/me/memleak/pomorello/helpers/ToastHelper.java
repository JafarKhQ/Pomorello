package me.memleak.pomorello.helpers;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 memleak.me. All rights reserved.
 */
public class ToastHelper {

    public static void top(Context context, int textResId) {
        top(context, context.getString(textResId));
    }

    public static void top(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 100);
        toast.show();
    }

    public static void bottom(Context context, int textResId) {
        bottom(context, context.getString(textResId));
    }

    public static void bottom(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    public static void center(Context context, int textResId) {
        center(context, context.getString(textResId));
    }

    public static void center(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
