package com.fittrack.Utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Umesh on 8/1/2017.
 */
public class PacificoFont {

    public Typeface app_font_pacifico;

    public PacificoFont(Context context) {

        app_font_pacifico = Typeface.createFromAsset(context.getAssets(),
                "Pacifico.ttf");
    }
}
