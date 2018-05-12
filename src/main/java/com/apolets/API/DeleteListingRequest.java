package com.apolets.API;

import java.util.HashMap;

public abstract class DeleteListingRequest extends Request {

    public DeleteListingRequest(int listing_id) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("item_id", listing_id);

        start("post", "delete_listing.php", args);
    }

}
