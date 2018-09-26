package com.fittrack.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fittrack.App;

/**
 * Created by chandresh on 3/22/2016.
 */
public class SpinnerTextView extends TextView {

    public SpinnerTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public SpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public SpinnerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {

        setTypeface(App.app_font_regular);
    }
}
