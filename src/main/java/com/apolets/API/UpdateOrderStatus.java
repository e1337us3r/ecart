package com.apolets.API;

import java.util.HashMap;

public abstract class UpdateOrderStatus extends Request {

    public UpdateOrderStatus(int order_id, String newStatus) {

        HashMap<String, Object> args = new HashMap<>();
        args.put("order_id", order_id);
        args.put("status", newStatus);

        start("post", "update_status.php", args);

    }
}
