package com.brinfotech.feedbacksystem.data;

import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeDataModel;
import com.google.gson.Gson;

public class DataUtils {

    public static String convertDataToString(Object myObj) {
        Gson gson = new Gson();
        return gson.toJson(myObj);
    }

    public static ScanQrCodeDataModel convertStringToUserObject(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, ScanQrCodeDataModel.class);
    }
}
