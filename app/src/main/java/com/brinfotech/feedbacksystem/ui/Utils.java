package com.brinfotech.feedbacksystem.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;

public class Utils {

    public static boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().equals("");
    }

    public static void autoHideKeyboard(final Activity activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hideKeyBoard(activity);
            }
        }, ConstantClass.AUTO_HIDE_KEYBOARD_TIMER);
    }

    public static void hideKeyBoard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showFileChooser(Activity activity, int requestCode) {
        Intent intent4 = new Intent(activity, NormalFilePickActivity.class);
        intent4.putExtra(Constant.MAX_NUMBER, 1);
        intent4.putExtra(IS_NEED_FOLDER_LIST, true);
        intent4.putExtra(NormalFilePickActivity.SUFFIX,
                new String[]{"jpg", "png", "gif", "dOcX", ".pptx", "pdf"});
        activity.startActivityForResult(intent4, requestCode);
    }
}
