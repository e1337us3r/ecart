package com.apolets.API;

import com.apolets.main.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public abstract class FetchOrdersRequest extends Request {

    public FetchOrdersRequest(String status, String startDate, String endDate) {

        HashMap<String, Object> args = new HashMap<>();
        args.put("status", status);
        args.put("startDate", startDate);
        args.put("endDate", endDate);

        start("get", "fetch_orders.php", args);


    }

    public ObservableList<Order> getOrdersPayload(JSONObject response) {

        ObservableList<Order> itemsList = FXCollections.observableArrayList();
        if (response.keySet().contains("items")) {
            JSONArray items = (JSONArray) response.get("items"); //json 'items' is array of objects
            for (int i = 0; i < items.length(); i++) {
                itemsList.add(new Order(items.getJSONObject(i)));
            }
        }
        return itemsList;

    }
}
