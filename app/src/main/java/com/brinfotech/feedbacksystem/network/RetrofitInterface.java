package com.brinfotech.feedbacksystem.network;


import com.brinfotech.feedbacksystem.data.insertCOSHH.InsertCoshhRequestModel;
import com.brinfotech.feedbacksystem.data.insertCOSHH.InsertCoshhResponseModel;
import com.brinfotech.feedbacksystem.data.pinLogin.PinLoginRequestModel;
import com.brinfotech.feedbacksystem.data.pinLogin.PinLoginResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeResponseModel;
import com.brinfotech.feedbacksystem.data.signINOut.SignInOutRequestModel;
import com.brinfotech.feedbacksystem.data.siteList.SiteListResponseModel;
import com.brinfotech.feedbacksystem.data.upload.UploadFileResponseModel;
import com.brinfotech.feedbacksystem.network.utils.WebApiHelper;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {


    @POST(WebApiHelper.SIGN_IN_OUT_QRCODE)
    Call<ScanQrCodeResponseModel> scanQRCodeSignInOut(@Body SignInOutRequestModel requestModel);

    @POST(WebApiHelper.PIN_LOGIN)
    Call<PinLoginResponseModel> pinLogin(@Body PinLoginRequestModel requestModel);

    @POST(WebApiHelper.GET_SITE_LIST)
    Call<SiteListResponseModel> getSiteList();

    @Multipart
    @POST(WebApiHelper.UPLOAD_SIGNATURE)
    Call<UploadFileResponseModel> uploadFile(@Part MultipartBody.Part signature1);

    @POST(WebApiHelper.INSERT_COSHH)
    Call<InsertCoshhResponseModel> insertCOSHH(@Body InsertCoshhRequestModel requestModel);

}
