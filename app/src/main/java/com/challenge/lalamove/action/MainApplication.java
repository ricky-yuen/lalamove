package com.challenge.lalamove.action;

import android.app.Application;

import com.challenge.lalamove.model.DeliverModel;

import java.util.ArrayList;

/**
 * Created by ricky on 11/8/2017.
 */

public class MainApplication  extends Application {
    public static ArrayList deliverList;
    public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        //
        deliverList = new ArrayList<DeliverModel>();
        instance = this;
    }
}
