package com.brinfotech.feedbacksystem.customClasses;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

public class FontsTextView extends AppCompatTextView {

    public FontsTextView(Context context) {
        super(context);
        init();
    }

    public FontsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_medium.ttf");
        this.setTypeface(face);
    }
}
