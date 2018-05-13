package com.apolets.API;

import java.util.HashMap;

public abstract class UpdateTrackingCode extends Request {

    public UpdateTrackingCode(int order_id, String tracking_code) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("order_id", order_id);
        args.put("tracking_code", tracking_code);
        start("post", "update_tracking.php", args);
    }

}
