// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

public class Tag {
    private String name;

    public Tag() {}
    public Tag(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}