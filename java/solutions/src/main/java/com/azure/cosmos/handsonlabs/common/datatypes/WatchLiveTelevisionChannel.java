package com.azure.cosmos.handsonlabs.common.datatypes;

public class WatchLiveTelevisionChannel implements IInteraction
{
    String id;
    String channelName;
    int minutesViewed;
    String type;

    public String getId(){return this.id;};
    public void setId(String id){this.id=id;}
    public String getChannelName(){return this.channelName;}
    public void setChannelName(String channelName){this.channelName=channelName;}
    public int getMinutesViewed(){return this.minutesViewed;}
    public void setMinutesViewed(int minutesViewed){
        this.minutesViewed=minutesViewed;
    }    
    public String getType(){return this.type;}
    public void setType(String type){this.type=type;}
}