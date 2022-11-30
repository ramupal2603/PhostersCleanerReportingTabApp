package com.brinfotech.feedbacksystem.ui.COSHHView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;

public class WebViewPDFActivity extends BaseActivity {

    private int i = 0;

    private WebView pdfView;
    private ProgressBar progress;
    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";

    private String[] strArray = {
            "https://www.legislation.gov.uk/uksi/2002/2677/pdfs/uksi_20022677_en.pdf",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pdfView = findViewById(R.id.pdfView);
        progress = findViewById(R.id.progress);

        showPdfFile(strArray[i]);

       /* imageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i != 0) {
                    showPdfFile(strArray[i--]);
                } else {
                    Toast.makeText(getApplicationContext(), "You're on first PDF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i != strArray.length) {
                    showPdfFile(strArray[i++]);
                } else {
                    Toast.makeText(getApplicationContext(), "Going to first PDF", Toast.LENGTH_SHORT).show();
                    showPdfFile(strArray[0]);
                }
            }
        });*/

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pdf_view;
    }

    private void showPdfFile(final String imageString) {
        showProgress();
        pdfView.invalidate();
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().setSupportZoom(true);
        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + imageString);
        pdfView.setWebViewClient(new WebViewClient() {
            boolean checkOnPageStartedCalled = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkOnPageStartedCalled = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkOnPageStartedCalled) {
                    pdfView.loadUrl(removePdfTopIcon);
                    hideProgress();
                } else {
                    showPdfFile(imageString);
                }
            }
        });
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

    }
}