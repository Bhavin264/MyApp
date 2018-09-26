package com.fittrack.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fittrack.App;


public class RegularFontTextView extends TextView {
    public RegularFontTextView(Context context) {
        super(context);
        this.setTypeface(App.app_font_regular);
    }

    public RegularFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(App.app_font_regular);
    }

    public RegularFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(App.app_font_regular);
    }
}
