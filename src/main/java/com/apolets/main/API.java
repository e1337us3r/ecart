package com.apolets.main;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class API {

    private static final String SITEURL = "http://localhost/shop/api/";
    private static JSONObject lastResponse = new JSONObject();


    public static boolean hasError() {

        return lastResponse.keySet().contains("error");

    }

    public static String getError() {


        return (String) lastResponse.get("error");

    }

    public static String getMessage() {

        return String.valueOf(lastResponse.get("message"));
    }


    public static ArrayList<Listing> fetchAllListingsPayload() {

        ArrayList<Listing> itemsList = new ArrayList<>();
        if (lastResponse.keySet().contains("items")) {
            JSONArray items = (JSONArray) lastResponse.get("items"); //json 'items' is array of arrays
            for (int i = 0; i < items.length(); i++) {
                String unparsedItem = items.get(i).toString(); //get an array of com.apolets.main.Listing item (which at this point is a String)
                JSONArray parsedItem = new JSONArray(unparsedItem);//parse com.apolets.main.Listing item of type String into an array
                itemsList.add(new Listing(parsedItem));

            }
        }
        return itemsList;
    }


    public static boolean loginRequest(String email, String password) {

        String url = SITEURL + "login.php";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("email", email)
                    .field("password", password)
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject();
            return true;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean fetchAllListingsRequest() {

        String url = SITEURL + "fetch_item.php";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            return true;

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean deleteListingRequest(ArrayList<String> item_ids) {

        String id_parameter = String.join(",", item_ids);

        String url = SITEURL + "delete_item.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("item_id", id_parameter)
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            return true;


        } catch (UnirestException e) {
            e.printStackTrace();

        }
        return false;

    }


    public static boolean createListingRequest(String item_name, String item_price, String item_description, String item_stock, String item_pic, String item_category) {
        String url = SITEURL + "create_item.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("name", item_name)
                    .field("price", item_price)
                    .field("description", item_description)
                    .field("stock", item_stock)
                    .field("pic", item_pic)
                    .field("category", item_category)
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            return true;


        } catch (UnirestException e) {
            e.printStackTrace();

        }
        return false;
    }

    public static boolean updateListingRequest(Listing listing) {
        String url = SITEURL + "update_item.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("id", listing.getId())
                    .field("name", listing.getName())
                    .field("price", listing.getPrice())
                    .field("description", listing.getDesc())
                    .field("stock", listing.getStock())
                    .field("pic", listing.getStoreImage())
                    .field("category", listing.getCategory())
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            return true;

        } catch (UnirestException e) {
            e.printStackTrace();

        }
        return false;
    }


}
