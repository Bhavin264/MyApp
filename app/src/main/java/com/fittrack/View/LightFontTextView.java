package com.fittrack.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fittrack.App;


public class LightFontTextView extends TextView {
    public LightFontTextView(Context context) {
        super(context);
        this.setTypeface(App.app_font_light);
    }

    public LightFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(App.app_font_light);
    }

    public LightFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(App.app_font_light);
    }
}
