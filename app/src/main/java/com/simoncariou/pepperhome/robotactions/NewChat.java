package com.simoncariou.pepperhome.robotactions;

import android.util.Log;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.simoncariou.pepperhome.R;
import com.simoncariou.pepperhome.api.ApiClient;

public class NewChat {
    private QiContext mqiContext;
    private QiChatbot qiChatBot = null;
    private Topic topLight = null;
    public Chat chat = null;
    private String TAG = "PepperHome_Chat";
    private ApiClient apiclient = null;

    //constructor to get the context from the mainactivity
    public NewChat(QiContext qictxt, ApiClient apiclient) {
        Log.i(TAG, "Creating the NewChat custom object");
        this.mqiContext = qictxt;
        this.apiclient = apiclient;
        initAndBuildChat();
    }

    private void initAndBuildChat(){
        Log.i(TAG, "Building the topic");
        topLight = TopicBuilder.with(mqiContext)
                .withResource(R.raw.topic_light_handling)
                .build();

        Log.i(TAG, "Building the qiChatBot");
        qiChatBot = QiChatbotBuilder.with(mqiContext)
                .withTopic(topLight)
                .build();

        Log.i(TAG, "Building the chat");
        chat = ChatBuilder.with(mqiContext)
                .withChatbot(qiChatBot)
                .build();
    }

    public Future<Void> run(){
        Log.i(TAG, "Chat running.");
        apiclient.execute(true);
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
