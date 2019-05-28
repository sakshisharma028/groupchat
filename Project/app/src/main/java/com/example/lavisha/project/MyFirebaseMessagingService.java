package com.example.lavisha.project;

import android.annotation.SuppressLint;

import com.google.firebase.messaging.FirebaseMessagingService;

@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendRegistrationtoServer(s);
    }

    private void sendRegistrationtoServer(String s) {
    }
}
