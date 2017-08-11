package com.challenge.lalamove.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ricky on 11/8/2017.
 */

public class DeliverModel {
    private String thumbToDisplay;
    private String descriptionToDisplay;
    private String titleToDisplay;
    private LatLng latLngToDisplay;

    public DeliverModel(String thumbUrl, String description, String title, LatLng latLng) {
        super();

        this.thumbToDisplay = thumbUrl;
        this.descriptionToDisplay = description;
        this.titleToDisplay = title;
        this.latLngToDisplay = latLng;
    }

    public String getThumbToDisplay() {
        return thumbToDisplay;
    }

    public String getDescriptionToDisplay() {
        return descriptionToDisplay;
    }

    public String getTitleToDisplay() {
        return titleToDisplay;
    }

    public LatLng getLatLng() {
        return latLngToDisplay;
    }
}

