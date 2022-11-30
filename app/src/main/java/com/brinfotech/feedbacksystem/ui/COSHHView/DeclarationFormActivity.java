package com.brinfotech.feedbacksystem.ui.COSHHView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.DataUtils;
import com.brinfotech.feedbacksystem.data.insertCOSHH.InsertCoshhRequestModel;
import com.brinfotech.feedbacksystem.data.insertCOSHH.InsertCoshhResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeDataModel;
import com.brinfotech.feedbacksystem.data.upload.UploadFileResponseModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.DateTimeUtils;
import com.brinfotech.feedbacksystem.helpers.UploadImageHelpers;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.ui.signatureView.SignatureViewActivity;

import java.io.File;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclarationFormActivity extends BaseActivity {

    private static final int REQUEST_SIGN_OUT_SIGNATURE = 1001;
    @BindView(R.id.edtFullName)
    EditText edtFullName;

    @BindView(R.id.edtDateCompleted)
    EditText edtDateCompleted;

    @BindView(R.id.edtLocation)
    EditText edtLocation;

    @BindView(R.id.edtPinNo)
    EditText edtPinNo;

    @BindView(R.id.txtSignature)
    TextView txtSignature;

    @BindView(R.id.imgSignature)
    ImageView imgSignature;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.chkAgree)
    CheckBox chkAgree;

    ScanQrCodeDataModel userModel;
    String signature1 = "";
    private File file1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgSignature.setOnClickListener(this::onClick);
        btnConfirm.setOnClickListener(this::onClick);
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            String jsonData = getIntent().getStringExtra(ConstantClass.EXTRAA_FORM_DATA);
            userModel = DataUtils.convertStringToUserObject(jsonData);
            if (userModel != null) {
                edtFullName.setText(userModel.getUser_name());
                edtLocation.setText(userModel.getSite_name());
                edtPinNo.setText(userModel.getUser_id());
                edtDateCompleted.setText(DateTimeUtils.getCurrentDate(DeclarationFormActivity.this));
            }

        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_declaration_form_activity;
    }

    @Override
    public void onClick(View view) {

        if (view == imgSignature) {
            Intent intent = new Intent(DeclarationFormActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_OUT_SIGNATURE);
        }

        if (view == btnConfirm) {
            if (file1 == null) {
                showToastMessage(getResources().getString(R.string.select_file));
            } else if (!chkAgree.isChecked()) {
                showToastMessage(getResources().getString(R.string.chk_agrrement));
            } else {
                uploadfile(file1);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_OUT_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");

            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imgSignature.setImageBitmap(bitmap);
            txtSignature.setVisibility(View.GONE);

            file1 = new File(filePath);
        }
    }

    private void uploadfile(File signature) {

        showProgressBar();

        MultipartBody.Part body = UploadImageHelpers.uploadImage(DeclarationFormActivity.this, signature,
                "signature");

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        Call<UploadFileResponseModel> call = apiService.uploadFile(body);
        call.enqueue(new Callback<UploadFileResponseModel>() {
            @Override
            public void onResponse(Call<UploadFileResponseModel> call, Response<UploadFileResponseModel> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    UploadFileResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        signature1 = responseModel.getSignature_name();
                        insertCOSHH(userModel.getUser_id(), signature1);
                    }
                } else {
                    showToastMessage(getResources().getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponseModel> call, Throwable t) {
                hideProgressBar();
                showToastMessage(getResources().getString(R.string.something_went_wrong));
                printLogMessage("uploadFailed", t.getMessage());
            }
        });
    }

    private void insertCOSHH(String userID, String userSignature) {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.insertCOSHH(getInsertCOSHHRequestData(userID, userSignature)).enqueue(new Callback<InsertCoshhResponseModel>() {
            @Override
            public void onResponse(Call<InsertCoshhResponseModel> call, Response<InsertCoshhResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    InsertCoshhResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().
                            equals(ConstantClass.RESPONSE_SUCCESS)) {
                        openThankYouActivity("3", userModel.getUser_name());
                        finish();
                    } else {
                        showToastMessage(getResources().getString(R.string.please_enter_valid_pin));
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertCoshhResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private InsertCoshhRequestModel getInsertCOSHHRequestData(String userID, String userSignature) {
        InsertCoshhRequestModel insertCoshhRequestModel = new InsertCoshhRequestModel();
        insertCoshhRequestModel.setUser_id(userID);
        insertCoshhRequestModel.setUser_signature(userSignature);
        return insertCoshhRequestModel;
    }

}
