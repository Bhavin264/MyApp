package com.fittrack.View;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.fittrack.App;


public class RegularFontButton extends Button {
    public RegularFontButton(Context context) {
        super(context);
        this.setTypeface(App.app_font_regular);
    }

    public RegularFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(App.app_font_regular);
    }

    public RegularFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(App.app_font_regular);
    }
}
