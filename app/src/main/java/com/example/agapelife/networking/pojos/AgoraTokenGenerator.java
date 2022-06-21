package com.example.agapelife.networking.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgoraTokenGenerator {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("channel_name")
    @Expose
    private String channelName;
    @SerializedName("error")
    @Expose
    private String error;

    public AgoraTokenGenerator() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
