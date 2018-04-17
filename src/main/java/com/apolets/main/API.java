package com.apolets.main;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class API {

    private static final String SITEURL = "http://localhost/shop/api/";
    private static JSONObject lastResponse = new JSONObject();
    public JSONObject lastResponseDynamic = new JSONObject();


    private static boolean hasError() {

        return lastResponse.keySet().contains("error");

    }

    public static String getError() {


        return (String) lastResponse.get("error");

    }

    public static String getMessage() {

        return String.valueOf(lastResponse.get("message"));
    }


    public static ObservableList<Listing> fetchAllListingsPayload() {

        ObservableList<Listing> itemsList = FXCollections.observableArrayList();
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
            if (!hasError() && getMessage().equalsIgnoreCase("Auth successful.")) {
                return true;
            }



        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Login request timed-out or failed.");
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
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Login request timed-out or failed.");
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
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Login request timed-out or failed.");
        }
        return false;

    }


    public static boolean createListingRequest(String item_name, Double item_price, String item_description, int item_stock, File item_pic, String item_category, Double cost) {
        String url = SITEURL + "create_item.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("accept", "application/json")
                    .field("name", item_name)
                    .field("price", item_price)
                    .field("description", item_description)
                    .field("stock", item_stock)
                    .field("pic", item_pic)
                    .field("category", item_category)
                    .field("cost", cost)
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Login request timed-out or failed.");
        }
        return false;
    }

    public static boolean updateListingRequest(Listing listing, File pic) {
        String url = SITEURL + "update_item.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("accept", "application/json")
                    .field("id", listing.getId())
                    .field("name", listing.getName())
                    .field("price", listing.getPrice())
                    .field("description", listing.getDesc())
                    .field("stock", listing.getStock())
                    .field("pic", (pic == null) ? "" : pic)
                    .field("category", listing.getCategory())
                    .field("cost", listing.getCost())
                    .asJson();

            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Login request timed-out or failed.");
        }
        return false;
    }

    //these methods are not static because they will be used with multiple threads and that would cause problems in future
    public double calculateTotalFinanceInfo() {
        Double total = 0.0;


        JSONArray entries = lastResponseDynamic.getJSONArray("items");
        for (int i = 0; i < entries.length(); i++) {
            JSONArray entry = entries.getJSONArray(i);
            total += entry.getDouble(1);
        }


        return total;
    }

    public boolean getFinanceInfoRequest(String timePeriod, String status) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime current_date = LocalDateTime.now();
        String url = SITEURL + "finance_info.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .queryString("time", timePeriod)
                    .queryString("date", dtf.format(current_date))
                    .queryString("status", status)
                    .asJson();

            lastResponseDynamic = jsonResponse.getBody().getObject(); //Get json body

            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(lastResponse);
            lastResponse.put("error", "Request timed-out or failed.");
        }
        return false;
    }

    public HashMap<Integer, Double> getGraphContent(int units) {

        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 1; i <= units; i++) {
            map.put(i, 0.0);
        }
        JSONArray entries = lastResponseDynamic.getJSONArray("items");
        for (int i = 0; i < entries.length(); i++) {
            JSONArray entry = entries.getJSONArray(i);
            map.put(entry.getInt(0), entry.getDouble(1));
        }


        return map;
    }

}
