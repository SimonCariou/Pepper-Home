package com.simoncariou.pepperhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity{
    /********************
    **CLASS VARIABLES**
    ********************/

    //log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    //retrofit object built in the RetrofitIntance class
    private static Retrofit retrofitClient = null;

    //@TODO remove from there, not security compliant.
    private final static String API_KEY = "sike";

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
        createInstances();
    }

    /*********************
     **ROBOTIC CALLBACKS**
     *********************/

    //@TODO make the app into a robotic app using the qiSDK


    /********************
     **FUNCTIONS**
     ********************/
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
        prepareLightsStatusOn(false);
        Call<List<ResponseBody>> putDataToHueBridgeCall = lService.turnLightsOnOff(API_KEY, "1", lightStatus);
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