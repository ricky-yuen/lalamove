package com.challenge.lalamove.action;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.challenge.lalamove.R;
import com.challenge.lalamove.config.AppConfig;
import com.challenge.lalamove.model.DeliverModel;
import com.challenge.lalamove.share.ShareData;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ricky on 11/8/2017.
 */

public class LandingActivity extends Activity {
    private LatLng setLatLng;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        //
        /*
        Check Network Connenected or NOT, if connected wil call API to get deliver data else use Cached data
        **/
        if(isNetworkConnected()) {
            getDeliveries();
        }else {
            checkInternetConnection();
            if(ShareData.getDeliverData()!=null) {
                initDeliveries(ShareData.getDeliverData());
            }else{
                checkInternetConnection();
            }
        }
    }

    /*
     Call API to get deliveries data
     **/
    private void getDeliveries(){
        final String apiUrl = AppConfig.getApi();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String deliveriesResponseJson = response.body().string();
                //
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        Save delivers result on Cache
                         */
                        ShareData.setDeliverData(deliveriesResponseJson);
                        //
                        /*
                        Initialization delivers result
                         */
                        initDeliveries(deliveriesResponseJson);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                /*
                Handle request failure
                */
            }
        });
    }

    private boolean initDeliveries(String jsonResult){
        try {
            JSONArray jsonDeliveriesArray = new JSONArray(jsonResult);
            //
            int totalDeliveries = jsonDeliveriesArray.length();
            //
            String deliverDescription;
            String deliverImageUrl;
            JSONObject jsonDeliveriesLocationObj;
            String deliverLocationLat;
            String deliverLocationLng;
            String deliverLocationTitle;
            //
            for (int i = 0; i < totalDeliveries; i++) {
                JSONObject jsonEachDeliveriesObj = jsonDeliveriesArray.getJSONObject(i);
                //
                deliverDescription = jsonEachDeliveriesObj.getString("description");
                deliverImageUrl = jsonEachDeliveriesObj.getString("imageUrl");
                jsonDeliveriesLocationObj = jsonEachDeliveriesObj.getJSONObject("location");
                deliverLocationLat = jsonDeliveriesLocationObj.getString("lat");
                deliverLocationLng = jsonDeliveriesLocationObj.getString("lng");
                deliverLocationTitle = jsonDeliveriesLocationObj.getString("address");
                //
                setLatLng = new LatLng(Double.parseDouble(deliverLocationLat), Double.parseDouble(deliverLocationLng));
                MainApplication.deliverList.add(new DeliverModel(deliverImageUrl, deliverDescription, deliverLocationTitle, setLatLng));
            }
            //
            toMainActivity();
        } catch (JSONException e) {
            e.printStackTrace();
            //
            return false;
        }

        return true;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void checkInternetConnection() {
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle extras = intent.getExtras();
                    NetworkInfo info = (NetworkInfo) extras
                            .getParcelable("networkInfo");
                    NetworkInfo.State state = info.getState();
                    if (state == NetworkInfo.State.CONNECTED) {
                        getDeliveries();
                    }
                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver((BroadcastReceiver) broadcastReceiver, intentFilter);
        }
    }

    private void toMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy (){
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

    }
}
