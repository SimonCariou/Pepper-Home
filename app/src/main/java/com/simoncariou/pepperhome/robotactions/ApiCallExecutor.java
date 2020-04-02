package com.simoncariou.pepperhome.robotactions;

import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.BaseQiChatExecutor;

import com.simoncariou.pepperhome.R;
import com.simoncariou.pepperhome.api.ApiClient;

import java.util.List;

public class ApiCallExecutor extends BaseQiChatExecutor {
    private QiContext mqiContext;
    private ApiClient apiclient;
    private Boolean lightStatus = false;
    private String TAG = "PepperHome_ApiCallExecutor";
    private Animate animateDab;

    //flag to say if the animation have already been built
    private Boolean objBuilt = false;

    public ApiCallExecutor(QiContext qictxt, ApiClient apiclient) {
        super(qictxt);
        Log.d(TAG, "ApiCallExecutor constructor");
        this.mqiContext = qictxt;
        this.apiclient = apiclient;
        if(!objBuilt){
            this.animateDab = buildAnimate(this.mqiContext);
        }
    }

    private Animate buildAnimate(QiContext qiCtxt){
        Animate _animate = null;
        Animation _animation = null;

        _animation = AnimationBuilder.with(qiCtxt)
                .withResources(R.raw.dab)
                .build();

        // Build the action.
        _animate = AnimateBuilder.with(qiCtxt)
                .withAnimation(_animation)
                .build();
        return _animate;
    }
    @Override
    public void runWith(List<String> params) {
        Log.d(TAG, "runWith: "+ params);

        //Run the action asynchronously.
        this.animateDab.async().run();

        if(params.get(0).toLowerCase().equals("on") || params.get(0).toLowerCase().equals("allume")) {
            lightStatus = true;
        }

        else if (params.get(0).toLowerCase().equals("off") || params.get(0).toLowerCase().contains("tein")) {
            lightStatus = false;
        }
        //executing the api call to the hue bridge
        apiclient.execute(lightStatus);
    }

    @Override
    public void stop() {

    }
}
