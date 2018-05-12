package com.apolets.API;


import com.apolets.main.Listing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public abstract class FetchAllListingsRequest extends Request {

    public FetchAllListingsRequest() {

        start("get", "fetch_all_listings.php", new HashMap<>());
    }

    public ObservableList<Listing> getAllListingsPayload(JSONObject response) {

        ObservableList<Listing> itemsList = FXCollections.observableArrayList();
        if (response.keySet().contains("items")) {
            JSONArray items = (JSONArray) response.get("items"); //json 'items' is array of objects
            for (int i = 0; i < items.length(); i++) {
                itemsList.add(new Listing(items.getJSONObject(i)));
            }
        }
        return itemsList;
    }


}
