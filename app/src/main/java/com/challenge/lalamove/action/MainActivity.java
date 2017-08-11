package com.challenge.lalamove.action;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.challenge.lalamove.R;
import com.challenge.lalamove.model.DeliverModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.challenge.lalamove.R.id.map;

/**
 * Created by ricky on 10/8/2017.
 */

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {
    private GoogleMap mMap;
    private Marker mLastMapMarker;
    private LatLng lastLatLng;
    private Boolean clickMap;

    private ArrayList deliverList;

    private RelativeLayout infoView;
    private ImageView thumbnailImg;
    private TextView infoContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        /*FirebaseCrash Testing Code*/
        //FirebaseCrash.report(new Exception("Lalamove Android non-fatal error"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        //
        clickMap = false;
        //
        infoView = (RelativeLayout) findViewById(R.id.info_cover);
        thumbnailImg = (ImageView) findViewById(R.id.thumbnail_img);
        infoContent = (TextView) findViewById(R.id.info_content);
    }

    private void setDeliveries(){
        deliverList =  MainApplication.deliverList;
        //
        String deliverTitle;
        LatLng deliverLatLng;
        //
        for (int i = 0; i < deliverList.size(); i++) {
            DeliverModel deliverData = (DeliverModel) deliverList.get(i);
            //
            deliverTitle = deliverData.getTitleToDisplay();
            deliverLatLng = deliverData.getLatLng();
            //
            MarkerOptions marker = new MarkerOptions().position(deliverLatLng).title(deliverTitle);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_marker));
            //
            mMap.addMarker(marker).setTag(i);
            //
            if(i==deliverList.size()-1){
                lastLatLng = deliverLatLng;
            }
        }
        //Default move to last result of Location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getFocusedBuilding();
        mMap.setOnMarkerClickListener(this);
        //
        setDeliveries();
    }

    /* Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        mLastMapMarker = marker;
        DeliverModel deliverData = (DeliverModel) deliverList.get((Integer) marker.getTag());

        lastLatLng = deliverData.getLatLng();
        Picasso mPicasso = Picasso.with(this);
        mPicasso.load(deliverData.getThumbToDisplay()).into(thumbnailImg);
        infoContent.setText(deliverData.getDescriptionToDisplay());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 18));
        //
        float dest = 1;
        ObjectAnimator viewAnimation = ObjectAnimator.ofFloat(infoView,
                "alpha", dest);
        viewAnimation.setDuration(1000);
        viewAnimation.start();

        /*
         Set clicked action
          */
        clickMap = true;

         /*
         Return false to indicate that we have not consumed the event and that we wish
         for the default behavior to occur (which is for the camera to move such that the
         marker is centered and for the marker's info window to open, if it has one).
         */

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(clickMap){
            clickMap = false;
            //
            mLastMapMarker.hideInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,12));
            //
            float dest = 0;
            ObjectAnimator viewAnimation = ObjectAnimator.ofFloat(infoView,
                    "alpha", dest);
            viewAnimation.setDuration(500);
            viewAnimation.start();
        }else{
            super.onBackPressed();
        }
    }
}
