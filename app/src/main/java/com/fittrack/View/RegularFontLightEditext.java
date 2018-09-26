package com.fittrack.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.fittrack.App;


/**
 * Created by Umesh on 9/10/2016.
 */
public class RegularFontLightEditext extends EditText {


    public RegularFontLightEditext(Context context) {
        super(context);
        this.setTypeface(App.app_font_light);
    }

    public RegularFontLightEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(App.app_font_light);
    }

    public RegularFontLightEditext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(App.app_font_light);
    }
}