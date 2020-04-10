// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import com.github.javafaker.Faker;

public class CardGeo {
    private double lat;
    private double lng;

    public CardGeo(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public CardGeo() {
        this(0.0,0.0);

        Faker faker = new Faker();

        this.lat = new Double(faker.address().latitude());
        this.lng = new Double(faker.address().longitude());
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}