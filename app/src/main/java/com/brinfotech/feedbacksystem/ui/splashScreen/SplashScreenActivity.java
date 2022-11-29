package com.brinfotech.feedbacksystem.ui.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.ui.qrCodeScannerView.QrCodeScannerViewActivity;
import com.brinfotech.feedbacksystem.ui.siteSelectionView.SiteSelectionViewActivity;
import com.pixplicity.easyprefs.library.Prefs;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        String siteId = Prefs.getString(PreferenceKeys.SITE_ID, "");
        if (siteId != null && !siteId.isEmpty()) {
            redirectDashboardActivity();
        } else {
            redirectSiteSelectionActivity();
        }

    }

    private void redirectDashboardActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), QrCodeScannerViewActivity.class));
                finish();
            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }

    private void redirectSiteSelectionActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), SiteSelectionViewActivity.class));
                finish();
            }
        }, ConstantClass.REDIRECTION_INTERVAL);
    }
}
