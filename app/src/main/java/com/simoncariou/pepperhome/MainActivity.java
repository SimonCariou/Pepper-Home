package com.simoncariou.pepperhome;

import android.os.Bundle;
import android.util.Log;
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
import com.simoncariou.pepperhome.robotactions.AutomaticLanguageSelector;
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
    private TextView tvChatLanguageStatus = null;

    //robot language gotten via a qi session
    private AutomaticLanguageSelector languageSelector = null;
    public String robotLanguage = "";


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

        //init the textView to say the langauge of the chat that is running
        tvChatLanguageStatus = findViewById(R.id.tvInfoChatLanguage);

    }

    @Override
    protected void onResume() {
        this.languageSelector = new AutomaticLanguageSelector();
        super.onResume();
    }


        @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
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

        //set the robot language for the entire App
        if(this.languageSelector != null){
            this.robotLanguage = this.languageSelector.getRobotLanguage();
        }

        //check if the chat objects needs to be re-instantiated
        if (this.robotLanguage.equals("French") && this.chatFr == null){
            Log.d(TAG, "this.chatFr == null");
            this.chatFr = createNewInstanceChatFr();
        }
        if (this.robotLanguage.equals("English") && this.chatEn == null){
            Log.d(TAG, "this.chatEn == null");
            this.chatEn = createNewInstanceChatEn();
        }

        //run the appropriate chat based on the language
        if (this.robotLanguage.equals("French")){
            Log.d(TAG, "robotLanguage = " + this.robotLanguage);
            this.chatFrFut = this.chatFr.run();
            runOnUiThread(()->tvChatLanguageStatus.setText("Chat en Français.\nDites par exemple: \"Allume/éteins la lumière\""));
        } else if (this.robotLanguage.equals("English")){
            Log.d(TAG, "robotLanguage = " + this.robotLanguage);
            this.chatEnFut = this.chatEn.run();
            runOnUiThread(()->tvChatLanguageStatus.setText("Chat running in English.\nSay for example: \"Turn on/off the lights\""));
        }//default behavior: english installed by default on the robot.
        else {
            Log.d(TAG, "robotLanguage = " + this.robotLanguage);
            this.chatEnFut = this.chatEn.run();
            runOnUiThread(()->tvChatLanguageStatus.setText("Chat running in English.\nSay for example: \"Turn on/off the lights\""));
        }
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

    private NewChatEn createNewInstanceChatEn(){
        NewChatEn chatEn_temp = new NewChatEn(mqiContext, qiChatBot, apicallexecutor);
        return chatEn_temp;
    }

    private NewChatFr createNewInstanceChatFr(){
        NewChatFr chatFr_temp = new NewChatFr(mqiContext, qiChatBot, apicallexecutor);
        return chatFr_temp;
    }
}