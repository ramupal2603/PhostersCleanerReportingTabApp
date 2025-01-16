package com.brinfotech.feedbacksystem.data.pinLogin;

import com.brinfotech.feedbacksystem.data.signINOut.ScanQrCodeDataModel;

public class PinLoginResponseModel {

    String log_id;
    String status;
    ScanQrCodeDataModel visitor_details;


    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ScanQrCodeDataModel getVisitor_details() {
        return visitor_details;
    }

    public void setVisitor_details(ScanQrCodeDataModel visitor_details) {
        this.visitor_details = visitor_details;
    }
}
