package com.brinfotech.feedbacksystem.ui.qrCodeScannerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.NetworkUtils;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeScannerViewActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {


    @BindView(R.id.txtClickHere)
    TextView txtClickHere;

    @BindView(R.id.txtWelcomeText)
    TextView txtWelcomeText;

    @BindView(R.id.scannerView)
    CodeScannerView qrCodeScanner;
    CodeScanner mCodeScanner;


    private static final String[] CAMERA_AND_STORAGE =
            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

    List<BarcodeFormat> arrFormatList = new ArrayList<>();
    private static final int MY_CAMERA_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeScannerView();

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleResult(result);
                    }
                });
            }
        });


        txtWelcomeText.setText(Prefs.getString(PreferenceKeys.SITE_MESSAGE, getDefaultWelcomeText()));
        txtClickHere.setOnClickListener(this::onClick);
    }

    private String getDefaultWelcomeText() {
        return getResources().getString(R.string.welcome_text);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EasyPermissions.hasPermissions(QrCodeScannerViewActivity.this, CAMERA_AND_STORAGE)) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    MY_CAMERA_REQUEST_CODE,
                    CAMERA_AND_STORAGE);
        } else {
            mCodeScanner.releaseResources();
            mCodeScanner.stopPreview();
            mCodeScanner.startPreview();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeScannerView() {
        arrFormatList.add(BarcodeFormat.QR_CODE);

        mCodeScanner = new CodeScanner(this, qrCodeScanner);

        // Parameters (default values)
        mCodeScanner.setCamera(CodeScanner.CAMERA_FRONT);// or CAMERA_FRONT or specific camera id
        mCodeScanner.setFormats(arrFormatList);// ex. listOf(BarcodeFormat.QR_CODE)
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scan_qr_code;
    }

    @Override
    public void onClick(View view) {
        if (view == txtClickHere) {
            openManualSignInView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCodeScanner.stopPreview();
    }

    public void handleResult(Result result) {
        String scannedId = result.getText();
        if (!scannedId.isEmpty()) {
            if (NetworkUtils.isNetworkConnected(getContext())) {
                mCodeScanner.releaseResources();
                mCodeScanner.stopPreview();
                callSignInOutMethod(scannedId);
            } else {
                showNoNetworkMessage();
            }

        }
    }

    private void callSignInOutMethod(String scannedId) {
        printLogMessage("userID", "" + scannedId);

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.scanQRCodeSignInOut(getSignInOutRequest(scannedId)).enqueue(new Callback<ScanQrCodeResponseModel>() {
            @Override
            public void onResponse(Call<ScanQrCodeResponseModel> call, Response<ScanQrCodeResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    ScanQrCodeResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)
                                || responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {
                            openThankYouActivity(responseModel.getStatus(),
                                    responseModel.getVisitor_details().getUser_name());
                        } else {
                            showInvalidQrCodeMessage();
                            resumeCameraPreview();
                        }
                    } else {
                        showInvalidQrCodeMessage();
                        resumeCameraPreview();
                    }

                } else {
                    showErrorMessage();
                    resumeCameraPreview();
                }

            }

            @Override
            public void onFailure(Call<ScanQrCodeResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
                showErrorMessage();
                resumeCameraPreview();
            }
        });
    }

    private void showInvalidQrCodeMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.qr_code_not_valid_for_sign_in_out));
    }

    private void resumeCameraPreview() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mCodeScanner.startPreview();
            }
        }, 5000);

    }

    private SignInOutRequestModel getSignInOutRequest(String scannedId) {

        SignInOutRequestModel requestModel = new SignInOutRequestModel();
        requestModel.setUser_id(scannedId);
        requestModel.setDevice_type(WebApiHelper.DEVICE_TYPE_TAB);
        requestModel.setSite_id(Prefs.getString(PreferenceKeys.SITE_ID, "0"));
        return requestModel;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_CODE_MANUAL && resultCode == RESULT_OK) {
            if (data != null) {
                String userName = data.getStringExtra(ConstantClass.EXTRAA_FORM_NAME);
                String status = data.getStringExtra(ConstantClass.EXTRAA_FORM_DATA);
                openThankYouActivity(status, userName);
            }
        }
    }
}
