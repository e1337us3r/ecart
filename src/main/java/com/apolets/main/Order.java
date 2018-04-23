package com.apolets.main;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.json.JSONObject;

public class Order extends RecursiveTreeObject<Order> {

    private int id;
    private int listing_id;
    private String listing_name;
    private String date;
    private int quantity;
    private double price;
    private double cost;
    private String status;
    private String trackingCode;


    public Order(JSONObject params) {
        this.id = params.getInt("id");
        this.listing_id = params.getInt("listing_id");
        this.listing_name = params.getString("listing_name");
        this.date = params.getString("date");
        this.quantity = params.getInt("quantity");
        this.price = params.getDouble("price");
        this.cost = params.getDouble("cost");
        this.status = params.getString("status");
        this.trackingCode = params.getString("tracking_code");
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public int getId() {
        return id;
    }

    public int getListing_id() {
        return listing_id;
    }

    public String getListing_name() {
        return listing_name;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
