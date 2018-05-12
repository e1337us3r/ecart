package com.apolets.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class StatsRequest extends Request {

    public StatsRequest() {
        start("get", "get_stats.php", new HashMap<>());
    }

    public String[] getTop5Listings(JSONObject response) {
        String[] listingNames = new String[5];

        JSONArray jsonArray = response.getJSONArray("toplistings");

        for (int i = 0; i < 5; i++) {
            listingNames[i] = jsonArray.getString(i);
        }

        return listingNames;

    }

    public LinkedHashMap<String, Integer> getTopCategories(JSONObject response) {

        LinkedHashMap<String, Integer> cats = new LinkedHashMap<>();

        JSONArray jsonArrayall = response.getJSONArray("topcats");

        for (int i = 0; i < jsonArrayall.length(); i++) {
            JSONArray singleCat = jsonArrayall.getJSONArray(i);
            cats.put(singleCat.getString(0), singleCat.getInt(1));
        }
        return cats;

    }

    public double getFulfillmentRate(JSONObject response) {

        return response.getDouble("fullfillment");
    }

}
