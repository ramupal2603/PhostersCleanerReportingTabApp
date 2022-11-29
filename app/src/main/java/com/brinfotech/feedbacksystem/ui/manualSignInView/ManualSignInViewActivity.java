package com.brinfotech.feedbacksystem.ui.manualSignInView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brinfotech.feedbacksystem.R;
import com.brinfotech.feedbacksystem.baseClasses.BaseActivity;
import com.brinfotech.feedbacksystem.data.pinLogin.PinLoginRequestModel;
import com.brinfotech.feedbacksystem.data.pinLogin.PinLoginResponseModel;
import com.brinfotech.feedbacksystem.helpers.ConstantClass;
import com.brinfotech.feedbacksystem.helpers.PreferenceKeys;
import com.brinfotech.feedbacksystem.helpers.StringUtils;
import com.brinfotech.feedbacksystem.network.RetrofitClient;
import com.brinfotech.feedbacksystem.network.RetrofitInterface;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;
import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualSignInViewActivity extends BaseActivity {


    @BindView(R.id.imgBackToMainMenu)
    ImageView imgBackToMainMenu;

    @BindView(R.id.edtPinNo)
    EditText edtPinNo;

    @BindView(R.id.btnSignInOut)
    Button btnSignInOut;

    @BindView(R.id.loutConfirmView)
    LinearLayout loutConfirmView;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.txtCleanerName)
    TextView txtCleanerName;

    @BindView(R.id.txtCleanerInputID)
    TextView txtCleanerInputID;

    @BindView(R.id.loutPinView)
    LinearLayout loutPinView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toggleLayoutVisibility(loutPinView);
        imgBackToMainMenu.setOnClickListener(this::onClick);
        btnSignInOut.setOnClickListener(this::onClick);
        btnCancel.setOnClickListener(this::onClick);
        btnConfirm.setOnClickListener(this::onClick);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_manual_sign_in;
    }

    @Override
    public void onClick(View view) {
        if (view == imgBackToMainMenu) {
            finish();
        } else if (view == btnSignInOut) {
            validateCleanerByID();
        } else if (view == btnCancel) {
            clearConfirmView();
            toggleLayoutVisibility(loutPinView);
        } else if (view == btnConfirm) {
            callSignInOutPinNoAPI(txtCleanerInputID.getText().toString().trim());
        }
    }

    private void clearConfirmView() {
        txtCleanerName.setText("");
        txtCleanerInputID.setText("");
    }

    private void validateCleanerByID() {

        if (StringUtils.checkEmptyEditText(edtPinNo)) {
            showToastMessage(getResources().getString(R.string.please_enter_pin));
        } else {
            callValidatePinNoAPI(edtPinNo.getText().toString().trim());
        }

    }

    private void callValidatePinNoAPI(String edtPinNo) {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.pinLogin(getPinRequestData(edtPinNo)).enqueue(new Callback<PinLoginResponseModel>() {
            @Override
            public void onResponse(Call<PinLoginResponseModel> call, Response<PinLoginResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    PinLoginResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().
                            equals(ConstantClass.RESPONSE_SUCCESS_CONFIRM)) {
                        setConfirmViewData(responseModel.getVisitor_details().getUser_id(),
                                responseModel.getVisitor_details().getUser_name());
                        toggleLayoutVisibility(loutConfirmView);
                    }else{
                        showToastMessage(getResources().getString(R.string.please_enter_valid_pin));
                    }
                }
            }

            @Override
            public void onFailure(Call<PinLoginResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }


    private void callSignInOutPinNoAPI(String edtPinNo) {
        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.pinLogin(getPinRequestDataForSignInOut(edtPinNo)).enqueue(new Callback<PinLoginResponseModel>() {
            @Override
            public void onResponse(Call<PinLoginResponseModel> call, Response<PinLoginResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    PinLoginResponseModel responseModel = response.body();
                    if (responseModel != null &&
                            (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN) ||
                                    responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT))) {
                        Intent intent = new Intent();
                        intent.putExtra(ConstantClass.EXTRAA_FORM_NAME, responseModel.getVisitor_details().getUser_name());
                        intent.putExtra(ConstantClass.EXTRAA_FORM_DATA, responseModel.getStatus());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<PinLoginResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private PinLoginRequestModel getPinRequestDataForSignInOut(String edtPinNo) {
        PinLoginRequestModel pinLoginRequestModel = new PinLoginRequestModel();
        pinLoginRequestModel.setSite_id(Prefs.getString(PreferenceKeys.SITE_ID,"0"));
        pinLoginRequestModel.setLogin_confirm("0");
        pinLoginRequestModel.setVisitor_id(edtPinNo);

        return pinLoginRequestModel;
    }

    private void setConfirmViewData(String userID, String userName) {
        txtCleanerInputID.setText(userID);
        txtCleanerName.setText(userName);
    }

    private PinLoginRequestModel getPinRequestData(String edtPinNo) {
        PinLoginRequestModel pinLoginRequestModel = new PinLoginRequestModel();
        pinLoginRequestModel.setSite_id(Prefs.getString(PreferenceKeys.SITE_ID,"0"));
        pinLoginRequestModel.setLogin_confirm("1");
        pinLoginRequestModel.setVisitor_id(edtPinNo);

        return pinLoginRequestModel;

    }


    private void toggleLayoutVisibility(LinearLayout loutVisible) {
        loutPinView.setVisibility(View.GONE);
        loutConfirmView.setVisibility(View.GONE);

        loutVisible.setVisibility(View.VISIBLE);
    }
}
