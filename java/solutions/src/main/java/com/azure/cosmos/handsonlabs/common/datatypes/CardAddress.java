// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import com.github.javafaker.Faker;

public class CardAddress {
    private String street;
    private String suite;
    private String city;
    private String state;
    private String zipCode;
    private CardGeo geo;

    public CardAddress(String street, String suite, String city, String state, String zipCode, CardGeo geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.geo = geo;
    }

    public CardAddress() {
        this("","","","","",new CardGeo());

        Faker faker = new Faker();

        this.street = faker.name().firstName();
        this.suite = faker.address().buildingNumber();
        this.city = faker.address().city();
        this.state = faker.address().state();
        this.zipCode = faker.address().zipCode();
        //geo is taken care of
    }

    public String getStreet() {
        return street;
    }

    public CardGeo getGeo() {
        return geo;
    }

    public void setGeo(CardGeo geo) {
        this.geo = geo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}