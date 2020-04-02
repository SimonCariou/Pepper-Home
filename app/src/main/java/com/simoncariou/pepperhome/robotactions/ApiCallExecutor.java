package com.simoncariou.pepperhome.robotactions;

import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import com.simoncariou.pepperhome.api.ApiClient;

import java.util.List;

public class ApiCallExecutor extends BaseQiChatExecutor {
    private QiContext mqiContext;
    private ApiClient apiclient;
    private Boolean lightStatus = false;
    private String TAG = "PepperHome_ApiCallExecutor";

    public ApiCallExecutor(QiContext qictxt, ApiClient apiclient) {
        super(qictxt);
        Log.d(TAG, "ApiCallExecutor constructor");
        this.mqiContext = qictxt;
        this.apiclient = apiclient;
    }

    @Override
    public void runWith(List<String> params) {
        Log.d(TAG, "runWith: "+ params);

        if(params.get(0).toLowerCase().equals("on")) {
            lightStatus = true;
        }

        else if (params.get(0).toLowerCase().equals("off")) {
            lightStatus = false;
        }
        //executing the api call to the hue bridge
        apiclient.execute(lightStatus);
    }

    @Override
    public void stop() {

    }
}
