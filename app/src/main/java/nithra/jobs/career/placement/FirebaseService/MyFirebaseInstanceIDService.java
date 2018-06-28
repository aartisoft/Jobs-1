package nithra.jobs.career.placement.FirebaseService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.htmlcleaner.Utils;

import nithra.jobs.career.placement.Config;
import nithra.jobs.career.placement.ServerUtilities;
import nithra.jobs.career.placement.utills.SharedPreference;
import nithra.jobs.career.placement.utills.U;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        System.out.println("sendRegistrationToServer: " + token);
        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.putString(this,"token",token);
        ServerUtilities.gcmpost(token, U.getAndroidId(this), U.versionname_get(this), U.versioncode_get(this), this);
        sharedPreference.putInt(this, "fcm_update", U.versioncode_get(this));
    }

    private void storeRegIdInPref(String token) {
        SharedPreference pref = new SharedPreference();
        pref.putString(this,"regId", token);
    }

}