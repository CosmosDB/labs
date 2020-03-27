// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

public class GeneralInteraction implements IInteraction
{
    String id;
    String type;

    public String getId() {return this.id;}
    public void setId(String id) {this.id=id;}
    public String getType() {return this.type;}
    public void setType(String type){this.type=type;}
}