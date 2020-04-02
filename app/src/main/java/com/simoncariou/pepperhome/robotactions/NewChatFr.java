package com.simoncariou.pepperhome.robotactions;

import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatExecutor;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.locale.Language;
import com.aldebaran.qi.sdk.object.locale.Locale;
import com.aldebaran.qi.sdk.object.locale.Region;
import com.simoncariou.pepperhome.api.ApiClient;

import java.util.HashMap;
import java.util.Map;

public class NewChatFr {
    private QiContext mqiContext;
    private QiChatbot qiChatBot = null;
    public Chat chat = null;
    private String TAG = "PepperHome_Chat";
    private ApiCallExecutor apiCallExecutor = null;

    private String topicNameFr = null;
    private Locale localeFr = null;

    //constructor to get the context from the mainactivity
    public NewChatFr(QiContext qictxt, QiChatbot qiChatBot, ApiCallExecutor apiCallExecutor) {
        Log.i(TAG, "Creating the NewChat custom object");
        this.mqiContext = qictxt;

        this.qiChatBot = qiChatBot;
        this.apiCallExecutor = apiCallExecutor;

        //init topic names and locales; English and French
        this.topicNameFr = "topic_light_handling-fr.top";
        this.localeFr =  new Locale(Language.FRENCH, Region.FRANCE);


        //in english here
        initAndBuildChatFr();
    }

    private void initAndBuildChatFr(){
        Log.i(TAG, "Building the topic " + this.topicNameFr);
        Topic topLight = TopicBuilder.with(mqiContext)
                .withAsset(this.topicNameFr)
                .build();

        Log.i(TAG, "Building the qiChatBot");
        //qiChatBot is referenced in the mainactivity to prevent the garbage collector from deleting the associated remoteObject
        qiChatBot = QiChatbotBuilder.with(mqiContext)
                .withTopic(topLight)
                .build();

        Map<String, QiChatExecutor> executors = new HashMap<>();

        // Map the executor name from the topic to our qiChatExecutor
        //qichatexecutor referenced in the mainactivity to prevent the garbage collector from deleting the associated remoteObject
        executors.put("api_call_executor", this.apiCallExecutor);

        // Set the executors to the qiChatbot
        qiChatBot.setExecutors(executors);

        Log.i(TAG, "Building the chat");
        chat = ChatBuilder.with(mqiContext)
                .withChatbot(qiChatBot)
                .withLocale(this.localeFr)
                .build();
    }

    public Future<Void> run(){
        Log.i(TAG, "Chat running.");
        return chat.async().run();
    }

    public void cancelChat(Future<Void> chatToCancel){
        if(chatToCancel == null){
            Log.i(TAG, "No chat is running.");
        }
        Log.i(TAG, "Cancelling the chat.");
        chatToCancel.requestCancellation();
    }
}
