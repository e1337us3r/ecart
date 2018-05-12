package com.apolets.API;


import java.util.HashMap;

public abstract class LoginRequest extends Request {


    public LoginRequest(String email, String password) {

        HashMap<String, Object> args = new HashMap<>();
        args.put("email", email);
        args.put("password", password);
        start("post", "login.php", args);

    }


}
