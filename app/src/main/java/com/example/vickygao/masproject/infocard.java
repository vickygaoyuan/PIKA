package com.example.vickygao.masproject;

import java.util.List;

import android.R;

public class infocard {

    public int id;
    public int userid;
    private String status;
    private String name;
    private String requestTime;
    private String driver;
    private String pickupAddress;
    private String deliverAddress;
    private float fee;
    private String giver;
    private String driverNum;
    private String giverNum;
    private String username;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void put(String Name, String giver ,String status,float price) {
        this.name = Name;
        this.giver = giver;
        this.status=status;
        this.fee=price;
    }

    public String getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getName() {
        return name;
    }

    public String getDriver(){
        return driver;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }
    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }
    public float getFee() {
        return fee;
    }
    public void setFee(float fee) {
        this.fee = fee;
    }
    public String getGiver() {
        return giver;
    }
    public void setGiver(String giver) {
        this.giver = giver;
    }
    public String getGiverNum() {
        return giverNum;
    }
    public void setGiverNum(String giverNum) {
        this.giverNum = giverNum;
    }
    public String getDriverNum() {
        return driverNum;
    }
    public void setgetDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }
    public String getUsername() {
        return username;
    }
    public void setUserName(String userName) {
        this.username = userName;
    }
}

