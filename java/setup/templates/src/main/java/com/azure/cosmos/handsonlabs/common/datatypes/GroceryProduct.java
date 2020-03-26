// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

public class GroceryProduct {
    private String id;
    private String productName;
    private String company;
    private RetailPackage rpackage;

    public String getId() {
        return id;
    }

    public RetailPackage getRpackage() {
        return rpackage;
    }

    public void setRpackage(RetailPackage rpackage) {
        this.rpackage = rpackage;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setId(String id) {
        this.id = id;
    }

}