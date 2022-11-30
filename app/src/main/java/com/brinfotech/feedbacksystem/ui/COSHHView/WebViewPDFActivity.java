package com.brinfotech.feedbacksystem.ui.COSHHView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;

import butterknife.BindView;

public class WebViewPDFActivity extends BaseActivity {


    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    String jsonData;
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

        btnConfirm.setOnClickListener(this::onClick);
        getData();

    }

    private void getData() {
        if (getIntent()!= null) {
            jsonData = getIntent().getStringExtra(ConstantClass.EXTRAA_FORM_DATA);
        }

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
        if (view == btnConfirm) {
            Intent intent = new Intent(WebViewPDFActivity.this, DeclarationFormActivity.class);
            intent.putExtra(ConstantClass.EXTRAA_FORM_DATA, jsonData);
            startActivity(intent);
            finish();
        }
    }
}