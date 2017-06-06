package com.gigaworks.bloodbankbeta;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Arch on 17-04-2017.
 */

public class ClientSubscription {

    private String bloodgroup;

    public ClientSubscription(String bloodgroup){
        this.bloodgroup=bloodgroup;
    }

    public ClientSubscription(){
    }

    public String  generateTopic(String topicBlood){

        switch (bloodgroup){
            case "A+": topicBlood="APositive";
                break;
            case "A-": topicBlood="ANegative";
                break;
            case "B+": topicBlood="BPositive";
                break;
            case "B-": topicBlood="BNegative";
                break;
            case "AB+": topicBlood="ABPositive";
                break;
            case "AB-": topicBlood="ABNegative";
                break;
            case "O+": topicBlood="OPositive";
                break;
            case "O-": topicBlood="ONegative";
                break;
            default: topicBlood="Ambiguous";
                break;
        }
        return topicBlood;
    }

    public void subscribeUser(){
        if(bloodgroup.equals("O-")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("A+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("A-"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("B+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("B-"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB-"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("O+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("O-"));
        }
        if(bloodgroup.equals("O+")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("A+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("O+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("B+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
        }
        if(bloodgroup.equals("A-")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("A+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("A-"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB-"));
        }
        if(bloodgroup.equals("A+")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("A+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
        }
        if(bloodgroup.equals("B-")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("B+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("B-"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB-"));
        }
        if(bloodgroup.equals("B+")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("B+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
        }
        if(bloodgroup.equals("AB-")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB-"));
        }
        if(bloodgroup.equals("AB+")){
            FirebaseMessaging.getInstance().subscribeToTopic(generateTopic("AB+"));
        }
    }

}
