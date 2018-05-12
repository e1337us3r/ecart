package com.apolets.main;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Listing extends RecursiveTreeObject<Listing> implements Comparable<Listing> {

    private int id;
    private String name;
    private double price;
    private double cost;
    private String desc;
    private int stock;
    private LocalDate listDate;
    private int rating;
    private String category;
    private String storeImage;
    private ArrayList<String> additionalImages = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Listing(int id, String name, double price, double cost, String desc, int stock, LocalDate listDate, int rating, String category, String storeImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.desc = desc;
        this.stock = stock;
        this.listDate = listDate;
        this.rating = rating;
        this.category = category;
        this.storeImage = storeImage;
    }


    public Listing(String unparsedListing) {

        JSONObject parsedListing = new JSONObject(unparsedListing);

        this.id = parsedListing.getInt("id");
        this.name = parsedListing.getString("name");
        this.price = parsedListing.getDouble("price");
        this.cost = parsedListing.getDouble("cost");
        this.desc = parsedListing.getString("description");
        this.stock = parsedListing.getInt("stock");
        this.category = parsedListing.getString("category");
        this.listDate = LocalDate.parse(parsedListing.getString("list_date"));
        this.rating = parsedListing.getInt("rating");
        this.storeImage = parsedListing.getString("pic");
    }


    public Listing(int id, String name, double price, double cost, String desc, int stock, String listDate, int rating, String category, String storeImage, ArrayList<String> additionalImages) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.desc = desc;
        this.stock = stock;
        this.category = category;
        this.listDate = LocalDate.parse(listDate, formatter);
        this.rating = rating;
        this.additionalImages = additionalImages;
        this.storeImage = storeImage;
    }


    public Listing(JSONObject jsonObject) {
        this.id = jsonObject.getInt("id");
        this.name = jsonObject.getString("name");
        this.price = jsonObject.getDouble("price");
        this.cost = jsonObject.getDouble("cost");
        this.desc = jsonObject.getString("description");
        this.stock = jsonObject.getInt("stock");
        this.listDate = LocalDate.parse(jsonObject.getString("list_date"), formatter);
        this.rating = jsonObject.getInt("rating");
        this.category = jsonObject.getString("category");
        this.storeImage = jsonObject.getString("pic");

        JSONArray imagesArray = jsonObject.getJSONArray("additionalImages");
        for (int i = 0; i < imagesArray.length(); i++) {
            this.additionalImages.add(imagesArray.getString(i));
        }
    }

    public ArrayList<String> getAdditionalImages() {
        return additionalImages;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getListDate() {
        return listDate.toString();
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

    public void setListDate(String listDate) {
        this.listDate = LocalDate.parse(listDate, formatter);
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

    public void setAdditionalImages(ArrayList<String> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public void addImage(String url){
        additionalImages.add(url);
    }

    public void removeImage(String url){
        additionalImages.remove(url);
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", cost=" + cost +
                ", desc='" + desc + '\'' +
                ", stock=" + stock +
                ", listDate=" + listDate +
                ", rating=" + rating +
                ", category='" + category + '\'' +
                ", storeImage='" + storeImage + '\'' +
                ", additionalImages=" + additionalImages +
                '}';
    }
}
