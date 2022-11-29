package com.brinfotech.feedbacksystem.data.pinLogin;

public class PinLoginRequestModel {
    String visitor_id;
    String site_id;
    String login_confirm;

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getLogin_confirm() {
        return login_confirm;
    }

    public void setLogin_confirm(String login_confirm) {
        this.login_confirm = login_confirm;
    }
}
