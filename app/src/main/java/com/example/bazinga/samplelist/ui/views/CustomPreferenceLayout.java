package com.example.bazinga.samplelist.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.bazinga.samplelist.R;
import com.example.bazinga.samplelist.utils.AppUtils;

/**
 * Created by Bazinga on 7/16/2017.
 */

public class CustomPreferenceLayout extends LinearLayout {

    Switch mToggleSwitch;

    public CustomPreferenceLayout(Context context) {
        super(context);
    }

    public CustomPreferenceLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPreferenceLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("techno", " on key down");
        boolean isRTL = AppUtils.isRTL(getContext());
        boolean handled = false;

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!isRTL && mToggleSwitch.isChecked()) {
                    mToggleSwitch.setChecked(false);
                    handled = true;
                } else if (isRTL && !mToggleSwitch.isChecked()) {
                    mToggleSwitch.setChecked(true);
                    handled = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (!isRTL && !mToggleSwitch.isChecked()) {
                    mToggleSwitch.setChecked(true);
                    handled = true;
                } else if (isRTL && mToggleSwitch.isChecked()) {
                    mToggleSwitch.setChecked(false);
                    handled = true;
                }
                break;
        }

        return handled;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        switch (child.getId()) {
            case R.id.custom_preference_layout_content_id:
                mToggleSwitch = (Switch) child.findViewById(R.id.custom_preference_switch);
                break;
        }
    }
}

