package com.azure.cosmos.handsonlabs.common.datatypes;

import java.math.BigDecimal;

public class PurchaseFoodOrBeverage implements IInteraction
{
    String id;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
    int quantity;
    String type;
    
    public String getId(){return this.id;}
    public void setId(String id){this.id=id;}
    public BigDecimal getUnitPrice(){
        return new BigDecimal(this.unitPrice.toString());
    }
    public void setUnitPrice(BigDecimal unitPrice){
        this.unitPrice=new BigDecimal(unitPrice.toString());
    }
    public BigDecimal getTotalPrice(){
        return new BigDecimal(this.totalPrice.toString());
    }
    public void setTotalPrice(BigDecimal totalPrice){
        this.totalPrice=new BigDecimal(totalPrice.toString());
    }
    public int getQuantity(){return this.quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public String getType(){return this.type;}
    public void setType(String type){this.type=type;}
}