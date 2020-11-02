package com.example.cegepaas.Model;

import com.google.gson.annotations.SerializedName;

public class FCMPojo {
    @SerializedName("multicast_id")
    private String multicast_id;

    @SerializedName("success")
    private String success;

    @SerializedName("failure")
    private String failure;

    public String getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(String multicast_id) {
        this.multicast_id = multicast_id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }
}
