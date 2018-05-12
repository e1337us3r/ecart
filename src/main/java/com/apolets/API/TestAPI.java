package com.apolets.API;

import org.json.JSONObject;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class TestAPI {
    private static final String imagepath = "C:\\Users\\AVA\\IdeaProjects\\testapimaven\\src\\main\\resources\\view\\e-shop.png";
    private static final List<File> image = Collections.singletonList(new File(imagepath));

    public static void main(String[] args) {

        new LoginRequest("testmerchant@gmail.com", "123123") {
            @Override
            protected void success(JSONObject response) {
                System.out.println("LoginRequest : success");

                testCreateListingRequest();


            }

            @Override
            protected void fail(String error) {
                System.out.println("LoginRequest : " + error);
            }
        };
    }


    public static void testCreateListingRequest() {
        new CreateListingRequest("test listing", 10.0, "test desc", 10, "Personal Care", 122.0, image) {
            @Override
            protected void success(JSONObject response) {
                //System.out.println(getListing(response));
                System.out.println("CreateListingRequest : success");
            }

            @Override
            protected void fail(String error) {
                System.out.println("CreateListingRequest :" + error);
            }
        };
    }

}
