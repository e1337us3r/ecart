package com.apolets.main;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.body.MultipartBody;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class API {

    public static final String SITEURL = "http://localhost/shop/"; //online= http://shop.apolets.com/
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

    public static Listing getListing() {
        return new Listing(lastResponse.getJSONObject("message"));
    }

    public static ObservableList<Listing> fetchAllListingsPayload() {

        ObservableList<Listing> itemsList = FXCollections.observableArrayList();
        if (lastResponse.keySet().contains("items")) {
            JSONArray items = (JSONArray) lastResponse.get("items"); //json 'items' is array of objects
            for (int i = 0; i < items.length(); i++) {
                itemsList.add(new Listing(items.getJSONObject(i)));
            }
        }
        return itemsList;
    }

    public static ObservableList<Order> fetchOrdersPayload() {

        ObservableList<Order> itemsList = FXCollections.observableArrayList();
        if (lastResponse.keySet().contains("items")) {
            JSONArray items = (JSONArray) lastResponse.get("items"); //json 'items' is array of objects
            for (int i = 0; i < items.length(); i++) {
                itemsList.add(new Order(items.getJSONObject(i)));
            }
        }
        return itemsList;

    }

    public static boolean loginRequest(String email, String password) {

        String url = SITEURL + "api/login.php";

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

        String url = SITEURL + "api/fetch_all_listings.php";

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


    public static boolean deleteListingRequest(int item_id) {


        String url = SITEURL + "api/delete_listing.php";
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("item_id", item_id)
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


    public static boolean createListingRequest(String listing_name, Double listing_price, String listing_description, int listing_stock, String listing_category, Double listing_cost, List<File> listing_pics) {
        String url = SITEURL + "api/create_listing.php";
        try {
            MultipartBody mb = Unirest.post(url)
                    .header("accept", "application/json")
                    .field("name", listing_name)
                    .field("price", listing_price)
                    .field("description", listing_description)
                    .field("stock", listing_stock)
                    .field("category", listing_category)
                    .field("cost", listing_cost);

            for (File f : listing_pics) {
                mb.field("pic[]", f);
            }


            HttpResponse<JsonNode> jsonResponse = mb.asJson();
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

    public static boolean fetchOrdersRequest(String status, String startDate, String endDate) {
        String url = SITEURL + "api/fetch_orders.php";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .queryString("status", status)
                    .queryString("startDate", startDate)
                    .queryString("endDate", endDate)
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Request timed-out or failed.");
        }

        return false;

    }


    public static boolean updateListingRequest(Listing listing, ArrayList<File> additionalImages, ArrayList<String> deletedImages, String profileImage) {
        String url = SITEURL + "api/update_listing.php";
        try {
            MultipartBody preparedRequest = Unirest.post(url)
                    .header("accept", "application/json")
                    .field("id", listing.getId())
                    .field("name", listing.getName())
                    .field("price", listing.getPrice())
                    .field("description", listing.getDesc())
                    .field("stock", listing.getStock())
                    .field("category", listing.getCategory())
                    .field("cost", listing.getCost());

            additionalImages.forEach((File f) -> preparedRequest.field("additionalImages[]", f));
            deletedImages.forEach((String image) -> preparedRequest.field("deletedImages[]", image));
            if (profileImage.startsWith("file")) preparedRequest.field("profilePic", new File(new URI(profileImage)));


            HttpResponse<JsonNode> jsonResponse = preparedRequest.asJson();

            lastResponse = jsonResponse.getBody().getObject(); //Get json body
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Request timed-out or failed.");
        }
        return false;
    }

    public static ArrayList<String> fetchCategories() {
        JSONArray jsoncatArray = lastResponse.getJSONArray("categories");
        ArrayList<String> categories = new ArrayList<>();

        for (int i = 0; i < jsoncatArray.length(); i++) {
            categories.add(jsoncatArray.getString(i));
        }
        return categories;

    }

    public static boolean fetchCategoriesRequest() {
        String url = SITEURL + "api/fetch_categories.php";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .asJson();
            lastResponse = jsonResponse.getBody().getObject();
            if (!hasError()) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            lastResponse.put("error", "Request timed-out or failed.");
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
        String url = SITEURL + "api/finance_info.php";
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
