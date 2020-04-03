package com.simoncariou.pepperhome.robotactions;

import android.util.Log;

import com.aldebaran.qi.AnyObject;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.UserTokenAuthenticator;

import java.util.concurrent.ExecutionException;

public class AutomaticLanguageSelector {
    private String TAG = "PepperHome_AutomaticLanguageSelector";
    private Session session = null;

    public AutomaticLanguageSelector(){
        Log.i(TAG, "AutomaticLanguageSelector constructor");
        this.session = initSession();
    }

    public String getRobotLanguage(){
        String language = "";
        AnyObject dialog = instantiateService("ALDialog");
        try {
            language = dialog.call("getLanguage").get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "RobotLanguage = " + language);
        return  language;
    }

    private Session initSession(){
        Log.d(TAG, "Initializing the qi Session ");
        Session session = new Session();

        //Personal robot password, changes in function of the robot you have.
        UserTokenAuthenticator authenticator = new UserTokenAuthenticator("nao", "nao");
        session.setClientAuthenticator(authenticator);

        try {
            session.connect("tcps://198.18.0.1:9503").get();
        } catch (ExecutionException e) {
            Log.e(TAG, "session connection failed", e);
        }
        //returns a session to be used to create proxies for example in instanciateService
        return session;
    }

    private AnyObject instantiateService (String serviceName) {
        AnyObject service = null;
        try {
            service = this.session.service(serviceName).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Creating the proxy for " + serviceName);
        return service;
    }
}
