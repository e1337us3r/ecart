package com.apolets.API;

import com.apolets.main.Listing;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public abstract class CreateListingRequest extends Request {

    public CreateListingRequest(String listing_name, Double listing_price, String listing_description, int listing_stock, String listing_category, Double listing_cost, List<File> listing_pics) {

        HashMap<String, Object> args = new HashMap<>();

        args.put("name", listing_name);
        args.put("price", listing_price);
        args.put("description", listing_description);
        args.put("stock", listing_stock);
        args.put("category", listing_category);
        args.put("cost", listing_cost);
        args.put("pic[]", listing_pics);

        start("post", "create_listing.php", args);


    }

    public Listing getListing(JSONObject response) {

        return new Listing(response.getJSONObject("message"));
    }

}
