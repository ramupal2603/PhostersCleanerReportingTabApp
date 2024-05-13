package com.brinfotech.feedbacksystem.data.siteList;

import java.util.ArrayList;

public class SiteListResponseModel {

    String status;
    ArrayList<SiteListResponseDataModel> site_details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SiteListResponseDataModel> getData() {
        return site_details;
    }

    public void setData(ArrayList<SiteListResponseDataModel> data) {
        this.site_details = data;
    }
}
