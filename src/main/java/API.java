import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class API {

    private static final String SITEURL = "http://localhost/shop/api/";


    public static boolean signin(String email, String password) {

        String url = SITEURL + "login.php";

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("email", email)
                    .field("password", password)
                    .asJson();
            JSONObject respjson = jsonResponse.getBody().getObject();
            // System.out.println(jsonResponse.getHeaders().get("Set-Cookie").get(0).substring(10,36));
            if (!checkError(respjson)) {
                for (Object key : respjson.keySet()) {
                    System.out.println(key + " : " + respjson.get((String) key));

                }
                return true;
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean checkError(JSONObject response) {

        return response.keySet().contains("error");

    }

    private static String getMessage(JSONObject response) {

        return (String) response.get("message");

    }

    private static String[] getErrors(JSONObject response) {
        return (String[]) response.get("error");
    }


    public static ArrayList<ListingItem> fetchAllItems() {

        String url = SITEURL + "fetch_item.php";
        ArrayList<ListingItem> itemsList = null;
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .asJson();
            JSONObject respjson = jsonResponse.getBody().getObject(); //Get json body
            if (!checkError(respjson)) {
                itemsList = new ArrayList<ListingItem>();
                JSONArray items = (JSONArray) respjson.get("items"); //json 'items' is array of arrays
                for (int i = 0; i < items.length(); i++) {
                    String unparsedItem = items.get(i).toString(); //get an array of Listing item (which at this point is a String)
                    JSONArray parsedItem = new JSONArray(unparsedItem);//parse Listing item of type String into an array
                    itemsList.add(new ListingItem(parsedItem));
                }


            } else {

                System.out.println("There has been an error: " + getErrors(respjson));
            }


        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return itemsList;
    }


}
