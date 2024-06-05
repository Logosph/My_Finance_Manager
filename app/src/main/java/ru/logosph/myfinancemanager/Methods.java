package ru.logosph.myfinancemanager;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;

public class Methods {
    public static float dpToPx(int dp, Context context) {
        return ((float) dp) * context.getResources().getDisplayMetrics().density;
    }

    public static float spToPx(int sp, Context context) {
        return ((float) sp) * context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static void draw(StaticLayout layout, Canvas canvas, float x, float y) {
        canvas.save();
        canvas.translate(x, y);
        layout.draw(canvas);
        canvas.restore();
    }
}
