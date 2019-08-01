package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 4ic1 on 2016-12-02.
 */
public class Networking {

    public boolean checkConn(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        } else {
            return true;
        }

    }
}
