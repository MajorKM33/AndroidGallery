package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by 4ic1 on 2016-09-23.
 */
public class Prefs {

    private static String MY_PREF = "";
    private static String SERVER_URL = "https://javaphotoalbum.herokuapp.com/upload";
    private static String SERVER_URL_DOWN = "https://javaphotoalbum.herokuapp.com/download";
    private static byte[] TO_SEND;
    private static ArrayList<JsonData> jsonList;

    public static void setAList( ArrayList<JsonData> list ){ jsonList = list; };
    public static ArrayList<JsonData> getAList(){ return jsonList; };

    public static void setMyPref(String myPref) {
        MY_PREF = myPref;
    }

    public static String getMyPref() {
        return MY_PREF;
    }

    public static String getServerUrl() {
        return SERVER_URL;
    }
    public static String getServerUrlDown() {
        return SERVER_URL_DOWN;
    }

    public static void setToSend(byte[] bytes) {
        TO_SEND = bytes;
    }

    public static byte[] getToSend() {
        return TO_SEND;
    }
}
