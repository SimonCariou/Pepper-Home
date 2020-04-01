package com.simoncariou.pepperhome.api;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.simoncariou.pepperhome.MainActivity;
import com.simoncariou.pepperhome.R;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiClient {

    private final String TAG = "PepperHome_ApiClient";

    //Will be initialized after.
    private String API_KEY = "";

    //needed to access asset files when getting the api key
    private Context context;

    public ApiClient(Context context){
        //context needed to access the assets files, ie apikey
        Log.i(TAG, "ApiClient constructor");
        this.context = context;

        //getting and setting the api key via file parsing.
        setApiKey(retrieveApiKeyFromFile());

        createInstances();
    }

    //setter for the api key
    private void setApiKey(String apikey){
        this.API_KEY = apikey;
    }

    //retrofit object built in the RetrofitInstance class
    private static Retrofit retrofitClient = null;

    //used in the API REST calls
    int statusCode = 0;

    //interface which the API calls are based on, handles all the GET, POST, PUT, PATCH req.
    LightService lService = null;

    //object that will be used when using the PUT req. to the hue bridge.
    private Action lightStatus = null;

    //get the api key as a string from a file located in the raw resources folder
    private String retrieveApiKeyFromFile(){
        FileParser fp = new FileParser(this.context);
        return fp.getApiKey();
    }

    // This method create an instance of Retrofit
    // set the base url
    private void createInstances(){
        Log.i(TAG, "Instantiating retrofit and api light service");
        if (retrofitClient == null) {
            retrofitClient = RetrofitInstance.getClient();
        }
        lService = retrofitClient.create(LightService.class);

        //initialize with the off state
        //@TODO intialize with the current state. Implement the GET req in the LightService
        lightStatus = new Action(false, 2, 2, 2);
    }

    //execute the request with true or false:
    //false  <=> lights off
    //true <=> lights on
    // execute(true) will turn on the lights
    // execute(false) will turn the off.

    public void execute(Boolean status){
        //turn off the lights. false
        prepareLightsStatusOn(status);
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
    private void prepareLightsStatusOn(Boolean status){
        lightStatus.setOn(status);
    }

}
