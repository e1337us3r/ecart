package com.apolets.main;

import org.json.JSONArray;

import java.util.HashSet;

public class Listing implements Comparable<Listing> {

    private int id;
    private String name;
    private double price;
    private String desc;
    private int stock;
    private String listDate;
    private int rating;
    private String category;
    private String storeImage;
    private HashSet<String> additionalImages = new HashSet<>();


    public Listing(int id, String name, float price, String desc, int stock, String listDate, int rating, String category, String storeImage, HashSet<String> additionalImages) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.stock = stock;
        this.listDate = listDate;
        this.rating = rating;
        this.additionalImages=additionalImages;
        this.storeImage = storeImage;
    }

    public Listing(int id, String name, double price, String desc, int stock, String listDate, int rating, String category, String storeImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.stock = stock;
        this.listDate = listDate;
        this.rating = rating;
        this.category = category;
        this.storeImage = storeImage;
    }

    public Listing(JSONArray jsonArray) {
        this.id = jsonArray.getInt(ItemCode.ID);
        this.name = jsonArray.getString(ItemCode.NAME);
        this.price = jsonArray.getDouble(ItemCode.PRICE);
        this.desc = jsonArray.getString(ItemCode.DESCRIPTION);
        this.stock = jsonArray.getInt(ItemCode.STOCK);
        this.listDate = jsonArray.getString(ItemCode.LISTDATE);
        this.rating = jsonArray.getInt(ItemCode.RATING);
        this.category = jsonArray.getString(ItemCode.CATEGORY);
        this.storeImage = jsonArray.getString(ItemCode.IMAGE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListDate() {
        return listDate;
    }

    public int getRating() {
        return rating;
    }


    public int compareTo(Listing o) {
        return this.id-o.getId();
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public HashSet<String> getAdditionalImages() {
        return additionalImages;
    }

    public void addImage(String url){
        additionalImages.add(url);
    }

    public void removeImage(String url){
        additionalImages.remove(url);
    }
}
