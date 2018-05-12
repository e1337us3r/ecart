package com.apolets.API;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;
import org.json.JSONObject;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Request {
    public static final String DOMAIN = "http://localhost/shop/"; //online= http://shop.apolets.com/
    public static final String SITEURL = DOMAIN + "api/";
    private String error = "";

    protected Request() {
    }

    protected void start(String method, String url, HashMap<String, Object> args) {
        new Thread(() -> {
            JSONObject response = null;
            if (method.equalsIgnoreCase("get")) {
                HttpRequest preparedRequest = prepareGetRequest(url, args);
                response = sendGetRequest(preparedRequest);
            } else if (method.equalsIgnoreCase("post")) {
                MultipartBody preparedRequest = preparePostRequest(url, args);
                response = sendPostRequest(preparedRequest);
            }

            if (response != null) {
                success(response);
            } else {
                fail(getError());
            }
        }).start();
    }


    protected JSONObject sendPostRequest(MultipartBody preparedRequest) {

        try {
            HttpResponse<JsonNode> response = preparedRequest.asJson();
            JSONObject resObject = response.getBody().getObject();
            if (!hasError(resObject))
                return resObject;


            this.error = resObject.getString("error");
        } catch (UnirestException e) {
            e.printStackTrace();
            this.error = "No Internet Connection.";
        }

        return null;
    }

    protected JSONObject sendGetRequest(HttpRequest preparedRequest) {
        try {
            HttpResponse<JsonNode> response = preparedRequest.asJson();
            JSONObject resObject = response.getBody().getObject();
            if (!hasError(resObject))
                return resObject;
            this.error = resObject.getString("error");
        } catch (UnirestException e) {
            e.printStackTrace();
            this.error = "No Internet Connection.";
        }
        return null;
    }

    protected MultipartBody preparePostRequest(String url, HashMap<String, Object> args) {
        String requestUrl = SITEURL + url;
        MultipartBody request = Unirest.post(requestUrl)
                .header("accept", "application/json")
                .field("", "");


        for (Map.Entry<String, Object> arg : args.entrySet()) {

            if (arg.getValue() instanceof Collection) {

                request.field(arg.getKey(), (Collection) arg.getValue());

            } else {
                if (arg.getValue() instanceof File)
                    request.field(arg.getKey(), (File) arg.getValue());
                else
                    request.field(arg.getKey(), arg.getValue());
            }


        }
        return request;

    }

    protected HttpRequest prepareGetRequest(String url, HashMap<String, Object> args) {
        String requestUrl = SITEURL + url;
        HttpRequest request = Unirest.get(requestUrl).header("Content-Type", "application/x-www-form-urlencoded");
        for (Map.Entry<String, Object> arg : args.entrySet()) {
            request.queryString(arg.getKey(), arg.getValue());
        }
        return request;
    }

    protected abstract void success(JSONObject response);

    protected abstract void fail(String error);

    private boolean hasError(JSONObject response) {

        return response.keySet().contains("error");
    }

    private String getError() {

        return this.error;
    }

    protected String getMessage(JSONObject response) {

        return response.getString("message");
    }


}
