package com.simoncariou.pepperhome;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.simoncariou.pepperhome.api.*;

import com.simoncariou.pepperhome.robotactions.ApiCallExecutor;
import com.simoncariou.pepperhome.robotactions.NewChat;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    /********************
    **CLASS VARIABLES**
    ********************/
    //global reference to the qicontext
    private QiContext mqiContext = null;
    private ApiClient apiclient = null;
    //to have a referecne to them  in the running activity. WA call request could not be handled.
    public QiChatbot qiChatBot = null;
    public ApiCallExecutor apicallexecutor = null;

    //log tag
    private static final String TAG = "PepperHome_MainActivity";


    /*******************************
     **ANDROID LIFECYCLE CALLBACKS**
     *******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QiSDK.register(this,this);
        setSpeechBar();
        this.apiclient = new ApiClient(this);
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this);
        super.onDestroy();
    }

    /*********************
     **ROBOTIC CALLBACKS**
     *********************/

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Log.d(TAG, "onRobotFocusGained");
        mqiContext = qiContext;
        //instantiating the reference to give to the chat.
        apicallexecutor = new ApiCallExecutor(this.mqiContext, this.apiclient);

        //passing the apiclient as the rest req will be executed via the chat.
        NewChat chat = new NewChat(mqiContext, apiclient, qiChatBot, apicallexecutor);
        chat.run();
    }

    @Override
    public void onRobotFocusLost() {
        Log.d(TAG, "onRobotFocusLost");
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        Log.d(TAG, "onRobotFocusRefused");
    }

    /********************
     **CUSTOM FUNCTIONS**
     ********************/

    private void setSpeechBar(){
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);
        setSpeechBarDisplayPosition(SpeechBarDisplayPosition.BOTTOM);
    }
}