// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import java.math.BigDecimal;
import java.util.UUID;

import com.github.javafaker.Faker;

public class Transaction {
    private String id;
    private BigDecimal amount;
    private boolean processed;
    private String paidBy;
    private String costCenter;

    public Transaction(String id, BigDecimal amount, boolean processed, String paidBy, String costCenter) {
        this.id=id;
        this.amount=amount;
        this.processed=processed;
        this.paidBy=paidBy;
        this.costCenter=costCenter;
    }

    public Transaction() {
        this("",new BigDecimal("0.00"),false,"","");

        Faker faker = new Faker();

        this.id = UUID.randomUUID().toString();
        this.amount = new BigDecimal(faker.commerce().price());
        this.processed = faker.bool().bool();
        this.costCenter = faker.address().cityName();
    }

    public String getId() {
        return id;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setId(String id) {
        this.id = id;
    }
}