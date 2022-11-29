package com.brinfotech.feedbacksystem.network;


import com.brinfotech.feedbacksystem.data.pinLogin.PinLoginRequestModel;
import com.brinfotech.feedbacksystem.data.pinLogin.PinLoginResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.data.siteList.SiteListResponseModel;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {


    @POST(WebApiHelper.SIGN_IN_OUT_QRCODE)
    Call<ScanQrCodeResponseModel> scanQRCodeSignInOut(@Body SignInOutRequestModel requestModel);

    @POST(WebApiHelper.PIN_LOGIN)
    Call<PinLoginResponseModel> pinLogin(@Body PinLoginRequestModel requestModel);

    @POST(WebApiHelper.GET_SITE_LIST)
    Call<SiteListResponseModel> getSiteList();

}
