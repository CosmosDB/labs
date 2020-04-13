// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import com.github.javafaker.Faker;

public class CardCompany {
    private String name;
    private String catchPhrase;
    private String bs;

    public CardCompany(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public CardCompany() {
        this("","","");

        Faker faker = new Faker();

        this.name = faker.company().name();
        this.catchPhrase = faker.company().catchPhrase();
        this.bs = faker.company().bs();
    }

    public String getName() {
        return name;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public void setName(String name) {
        this.name = name;
    }
}