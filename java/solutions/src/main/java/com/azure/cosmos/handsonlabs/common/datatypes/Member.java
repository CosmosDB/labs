// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import java.util.UUID;

public class Member {
    private String id;
    private Person accountHolder;
    private Family relatives;

    public Member(String id, Person accountHolder, Family relatives) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.relatives = relatives;
    }

    public Member() {
        this("",new Person(),new Family());
        
        this.id = UUID.randomUUID().toString();
        //Person and Family are taken care of
    }

    public Member(Person accountHolder) {
        this("",accountHolder,new Family());

        this.id = UUID.randomUUID().toString();        
    }

    public String getId() {
        return id;
    }

    public Family getRelatives() {
        return relatives;
    }

    public void setRelatives(Family relatives) {
        this.relatives = relatives;
    }

    public Person getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(Person accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setId(String id) {
        this.id = id;
    }
}