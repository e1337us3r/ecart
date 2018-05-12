package com.apolets.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CategoriesRequest extends Request {


    public CategoriesRequest() {

        start("get", "fetch_categories.php", new HashMap<>());

    }

    public static ArrayList<String> getCategories(JSONObject response) {
        JSONArray jsoncatArray = response.getJSONArray("categories");
        ArrayList<String> categories = new ArrayList<>();

        for (int i = 0; i < jsoncatArray.length(); i++) {
            categories.add(jsoncatArray.getString(i));
        }
        return categories;

    }

}
