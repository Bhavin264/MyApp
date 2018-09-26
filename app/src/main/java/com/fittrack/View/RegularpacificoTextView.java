package com.fittrack.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fittrack.App;

/**
 * Created by Umesh on 8/1/2017.
 */
public class RegularpacificoTextView extends TextView {
    public RegularpacificoTextView(Context context) {
        super(context);
        this.setTypeface(App.app_pacifico_font);
    }

    public RegularpacificoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(App.app_pacifico_font);
    }

    public RegularpacificoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(App.app_pacifico_font);
    }
}

