package com.fittrack.Utility;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFont {
    public Typeface font_bold, font_medium, font_light, font_regular;

    public CustomFont(Context context) {

        font_regular = Typeface.createFromAsset(context.getAssets(),
                "Lato-Regular.ttf");
        font_bold = Typeface.createFromAsset(context.getAssets(),
                "Lato-Bold.ttf");
        font_light = Typeface.createFromAsset(context.getAssets(),
                "Lato-Light.ttf");

//
//        font_regular = Typeface.createFromAsset(context.getAssets(),
//                "Raleway-Regular.ttf");
//        font_bold = Typeface.createFromAsset(context.getAssets(),
//                "Raleway-Bold.ttf");
//        font_light = Typeface.createFromAsset(context.getAssets(),
//                "Raleway-Light.ttf");

//        font_regular = Typeface.createFromAsset(context.getAssets(),
//                "Quicksand-Regular.ttf");
//        font_bold = Typeface.createFromAsset(context.getAssets(),
//                "Quicksand-Bold.ttf");
//        font_light = Typeface.createFromAsset(context.getAssets(),
//                "Quicksand-Light.ttf");

    }


}
