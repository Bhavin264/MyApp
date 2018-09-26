package com.fittrack.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fittrack.App;


public class BoldFontTextView extends TextView {
    public BoldFontTextView(Context context) {
        super(context);
        this.setTypeface(App.app_font_bold, Typeface.BOLD);
    }

    public BoldFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(App.app_font_bold, Typeface.BOLD);
    }

    public BoldFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(App.app_font_bold, Typeface.BOLD);
    }
}
