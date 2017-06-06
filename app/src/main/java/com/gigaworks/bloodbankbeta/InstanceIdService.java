package com.gigaworks.bloodbankbeta;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Arch on 31-03-2017.
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    final static String TAG="Archit";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("bloodbank.pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",refreshedToken);
        editor.commit();

    }
}
