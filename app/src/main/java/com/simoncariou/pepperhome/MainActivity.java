package com.simoncariou.pepperhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.aldebaran.qi.sdk.Qi;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    /********************
    **CLASS VARIABLES**
    ********************/
    //global reference to the qicontext
    private QiContext mqiContext = null;

    //log tag
    private static final String TAG = "PepperHome_MainActivity";

    //retrofit object built in the RetrofitInstance class
    private static Retrofit retrofitClient = null;

    //Will be initialized after.
    private String API_KEY = "";

    //used in the API REST calls
    int statusCode = 0;

    //interface which the API calls are based on, handles all the GET, POST, PUT, PATCH req.
    LightService lService = null;

    //object that will be used when using the PUT req. to the hue bridge.
    private Action lightStatus = null;

    /*******************************
     **ANDROID LIFECYCLE CALLBACKS**
     *******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QiSDK.register(this,this);
        //@TODO doesn't work when using string gotten via a function in the api call
        API_KEY = retrieveApiKeyFromFile();
        //create the retrofit instance and the API via the interface
        createInstances();
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
        
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    /********************
     **FUNCTIONS**
     ********************/
        
    //get the api key as a string from a file located in the raw resources folder
    private String retrieveApiKeyFromFile(){
        String key = "";
        try {
            key = FileParser.getApiKey(getResources().openRawResource(R.raw.apikey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
    // This method create an instance of Retrofit
    // set the base url
    public void createInstances(){
        if (retrofitClient == null) {
            retrofitClient = RetrofitInstance.getClient();
            //retrofit = retrofitClient.create(RetrofitInstance.class);
        }
        lService = retrofitClient.create(LightService.class);

        //initialize with the off state
        //@TODO intialize with the current state. Implement the GET req in the LightService
        lightStatus = new Action(false, 254, 8402, 140);
        execute();
    }

    public void execute(){
        //turn off the lights. false
        prepareLightsStatusOn(true);
        //workaround to better api key management. Hopefully some day I'll be able to not have it in clear text on github.
        Call<List<ResponseBody>> putDataToHueBridgeCall = lService.turnLightsOnOff("xxx", "1", lightStatus);
        putDataToHueBridgeCall.enqueue(new Callback<List<ResponseBody>>() {
            @Override
            public void onResponse(Call<List<ResponseBody>> call, Response<List<ResponseBody>> response) {
                statusCode = response.code();
                Log.i(TAG, "Put request success. \nResponse code: " + statusCode);
            }

            @Override
            public void onFailure(Call<List<ResponseBody>> call, Throwable t) {
                Log.e(TAG, "Put request failed, " + t.toString());
            }
        });
    }

    //modification of the object that will be sent to the server.
    //must be done before using the retrofit Call
    //false  <=> lights off
    //true <=> lights on
    public void prepareLightsStatusOn(Boolean status){
        lightStatus.setOn(status);
    }

}