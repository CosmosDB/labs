// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

public class ViewMap implements IInteraction
{
    String id;
    int minutesViewed;
    String type;

    public String getId(){return this.id;};
    public void setId(String id){this.id=id;}
    public int getMinutesViewed(){return this.minutesViewed;}
    public void setMinutesViewed(int minutesViewed){
        this.minutesViewed=minutesViewed;
    }
    public String getType(){return this.type;}
    public void setType(String type){this.type=type;}
}