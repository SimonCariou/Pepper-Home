package com.simoncariou.pepperhome.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileParser {
    private String TAG = "PepperHome_FileParser";
    private Context context;
    private AssetManager assetManager;

    public FileParser(Context ctxt){
        Log.i(TAG, "FileParser constructor");
        this.context = ctxt;
        this.assetManager = this.context.getAssets();
    }

    public String getApiKey() {
        // To load text file
        String key = "";
        InputStream input = null;
        try {
            input = this.assetManager.open("apikey");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            key = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
        }
    }

