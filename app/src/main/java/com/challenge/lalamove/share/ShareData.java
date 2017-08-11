package com.challenge.lalamove.share;

import android.content.Context;
import android.content.SharedPreferences;

import com.challenge.lalamove.action.MainApplication;

/**
 * Created by ricky on 11/8/2017.
 */

public class ShareData {
    private Context context;
    //
    /*
    Share Preferences Name
     */
    public static final String ALL_VERSION = "all_version";
    public static final String DELIVER_DATA = "deliver_data";

    public ShareData(Context context) {
        this.context = context;
    }

    public static String getDeliverData() {
        SharedPreferences appSharedPrefs = MainApplication.instance.getSharedPreferences(ALL_VERSION,
                Context.MODE_PRIVATE);
        String DeliverData = appSharedPrefs.getString(DELIVER_DATA, "");
        //
        return DeliverData;
    }

    public static void setDeliverData(String data) {
        /*
        Storing object in preferences
         */
        SharedPreferences appSharedPrefs = MainApplication.instance.getSharedPreferences(ALL_VERSION,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putString(DELIVER_DATA, data);
        prefsEditor.commit();
    }
}
