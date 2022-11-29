package com.brinfotech.feedbacksystem.baseClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.customClasses.ProgressLoader;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.DateTimeUtils;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.ui.Utils;
import com.brinfotech.feedbacksystem.ui.manualSignInView.ManualSignInViewActivity;
import com.brinfotech.feedbacksystem.ui.siteSelectionView.SiteSelectionViewActivity;
import com.brinfotech.feedbacksystem.ui.thankYouPage.ThankYouScreen;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Unbinder unbinder = null;


    private ProgressLoader loader;

    @Nullable
    @BindView(R.id.txtTime)
    TextView txtTime;

    CountDownTimer newTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        unbinder = ButterKnife.bind(this);
        Utils.hideKeyBoard(getActivity());

        showTime();

        if (txtTime != null) {
            txtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), SiteSelectionViewActivity.class));
                    finish();
                }
            });
        }

    }

    private void showTime() {
        newTimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (txtTime != null) {
                    txtTime.setText(String.format("Time: %s", DateTimeUtils.getCurrentTime(getContext())));
                }
            }

            public void onFinish() {

            }
        };
        newTimer.start();
    }


    protected abstract int getLayoutResource();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }

        if (newTimer != null) {
            newTimer.cancel();
        }
    }

    public void showToastMessage(String errorMessage) {
        try {
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(15);
            toast.show();
        } catch (Exception e) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }

    public void printLogMessage(String tag, String errorMessage) {
        Log.e(tag, errorMessage);
    }

    public void showProgressBar() {

        //Check if Activity is null then close activity.
        if (getActivity() == null) {
            return;
        } else {
            //If loader instance is null then re-create object.
            if (loader == null) {
                loader = new ProgressLoader(getActivity());
            }

            //If progress bar is not showing then show progress bar.
            if (!loader.isShowing()) {
                loader.show();
            }
        }

    }

    public void hideProgressBar() {

        if (loader != null && loader.isShowing()) {
            loader.dismiss();
        }
    }

    public void showAlertDialog(Context context, String message) {

        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage(message);
            builder1.setTitle(context.getResources().getString(R.string.app_name));
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {

        }

    }

    public void showErrorMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.something_went_wrong));
    }

    public void showNoNetworkMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.no_internet_connection));
    }

    public boolean isUserLoggedIn() {
        return Prefs.getBoolean(PreferenceKeys.USER_LOGGED_IN, false);
    }

    public void openThankYouActivity(String status, String userName) {
        Intent intent = new Intent(getActivity(), ThankYouScreen.class);
        intent.putExtra(ConstantClass.EXTRAA_FORM_DATA, status);
        intent.putExtra(ConstantClass.EXTRAA_FORM_NAME, userName);
        startActivity(intent);
    }

    public void openManualSignInView() {
        Intent intent = new Intent(getActivity(), ManualSignInViewActivity.class);
        startActivityForResult(intent, ConstantClass.REQUEST_CODE_MANUAL);
    }

    public Context getContext() {
        return BaseActivity.this;
    }

    public BaseActivity getActivity() {
        return BaseActivity.this;
    }

}

