package com.apolets.API;

import com.apolets.main.Listing;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class UpdateListingRequest extends Request {

    public UpdateListingRequest(Listing listing, ArrayList<File> additionalImages, ArrayList<String> deletedImages, String profileImage) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("id", listing.getId());
        args.put("name", listing.getName());
        args.put("price", listing.getPrice());
        args.put("description", listing.getDesc());
        args.put("stock", listing.getStock());
        args.put("category", listing.getCategory());
        args.put("cost", listing.getCost());

        args.put("additionalImages[]", additionalImages);
        args.put("deletedImages[]", deletedImages);
        if (profileImage.startsWith("file")) {
            try {
                args.put("profilePic", new File(new URI(profileImage)));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                fail("Unexpected URI exception.");
            }
        }

        start("post", "update_listing.php", args);

    }

    public Listing getListing(JSONObject response) {

        return new Listing(response.getJSONObject("message"));
    }
}
