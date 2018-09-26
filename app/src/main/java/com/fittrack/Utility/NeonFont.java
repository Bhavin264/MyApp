package com.fittrack.Utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Umesh on 8/1/2017.
 */
public class NeonFont {

    public Typeface app_font_neon;

    public NeonFont(Context context) {

        app_font_neon = Typeface.createFromAsset(context.getAssets(),
                "Neon.ttf");
    }

}
