package com.apolets.API;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public abstract class FinanceInfoRequest extends Request {

    public FinanceInfoRequest(String timePeriod, String status) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime current_date = LocalDateTime.now();

        HashMap<String, Object> args = new HashMap<>();
        args.put("time", timePeriod);
        args.put("date", dtf.format(current_date));
        args.put("status", status);

        start("get", "finance_info.php", args);

    }


}
