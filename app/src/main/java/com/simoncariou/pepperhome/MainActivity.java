package com.simoncariou.pepperhome;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayPosition;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;

import com.simoncariou.pepperhome.api.*;

import com.simoncariou.pepperhome.robotactions.ApiCallExecutor;
import com.simoncariou.pepperhome.robotactions.NewChatEn;
import com.simoncariou.pepperhome.robotactions.NewChatFr;

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

    //to have the ability to cancel them when we need
    NewChatEn chatEn = null;
    NewChatFr chatFr = null;

    Future<Void> chatEnFut = null;
    Future<Void> chatFrFut = null;

    //log tag
    private static final String TAG = "PepperHome_MainActivity";

    //UI COMPONENTS
    private Button btnEnglishChat = null;
    private Button btnFrenchChat = null;
    private TextView tvChatLanguageStatus = null;


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

        //ini the buttons and their listeners
        btnEnglishChat = findViewById(R.id.btnEnglish);
        btnFrenchChat = findViewById(R.id.btnFrench);

        //init the textView to say the langauge of the chat that is running
        tvChatLanguageStatus = findViewById(R.id.tvInfoChatLanguage);

        btnEnglishChat.setOnClickListener(v -> {
            chatEnFut = chatEn.run();
            tvChatLanguageStatus.setText("Chat running in English.\nSay for example: \"Turn on/off the lights\"");
            //disable the other button to show the language
            btnFrenchChat.setClickable(false);
            btnFrenchChat.setAlpha(0.5f);
        });

        btnFrenchChat.setOnClickListener(v -> {
            chatFrFut = chatFr.run();
            tvChatLanguageStatus.setText("Chat en Français.\nDites par exemple: \"Allume/éteins la lumière\"");
            //disable the other button to show the language
            btnEnglishChat.setClickable(false);
            btnEnglishChat.setAlpha(0.5f);
        });

        //deactivate the buttons avant que les objets chat ne soient buildés, dans onRobotFocusGained:
        btnEnglishChat.setClickable(false);
        btnEnglishChat.setAlpha(0.5f);

        btnFrenchChat.setClickable(false);
        btnFrenchChat.setAlpha(0.5f);
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

        chatEn = new NewChatEn(mqiContext, qiChatBot, apicallexecutor);
        chatFr = new NewChatFr(mqiContext, qiChatBot, apicallexecutor);

        btnEnglishChat.setClickable(true);
        btnEnglishChat.setAlpha(1f);

        btnFrenchChat.setClickable(true);
        btnFrenchChat.setAlpha(1f);

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