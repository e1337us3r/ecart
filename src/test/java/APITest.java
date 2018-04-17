import com.apolets.main.API;
import com.apolets.main.Listing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class APITest {

    final String imagepath = "C:\\Users\\AVA\\IdeaProjects\\testapimaven\\src\\main\\resources\\view\\e-shop.png";

    @Test
    void loginRequest() {
        String[] emails = {"thesakox@gmail.com", "fake@gmail.com", "testmember@gmail.com", "thesakox@gmail.com"};
        String[] passwords = {"wrong password", "123123", "123123", "123123"};

        Boolean[] expectedResults = {false, false, false, true};
        Boolean[] results = new Boolean[expectedResults.length];
        for (int i = 0; i < emails.length; i++) {
            results[i] = API.loginRequest(emails[i], passwords[i]);
        }
        assertArrayEquals(expectedResults, results);

    }


    //FOR THE CRUD REQUESTS WE ARE TESTING BOTH IF THE OPERATION SUCCEEDED OR NOT
    //AND IF THE FUNCTION RETURNS TRUE OR FALSE
    @Test
    void createListingRequest() {
        API.loginRequest("thesakox@gmail.com", "123123");

        assertTrue(API.createListingRequest("test listing", 10.0, "test desc", 10, new File(imagepath), "category 1", 122.0));
        assertFalse(API.createListingRequest("test listing", 10.0, "test desc", 10, new File(imagepath), "cat", 122.0));
        assertFalse(API.createListingRequest("te", 10.0, "test desc", 10, new File(imagepath), "category 1", 122.0));
        System.out.println();
    }

    @Test
    void fetchAllListingsRequest() {

        API.loginRequest("thesakox@gmail.com", "12312");

        assertFalse(API.fetchAllListingsRequest());

        API.loginRequest("thesakox@gmail.com", "123123");

        assertTrue(API.fetchAllListingsRequest());


    }

    @Test
    void deleteListingRequest() {

        API.loginRequest("thesakox@gmail.com", "123123");

        API.createListingRequest("test listing", 10.0, "test desc", 10, new File(imagepath), "category 1", 122.0);

        ArrayList<String> listingIds = new ArrayList<>();
        listingIds.add(API.getMessage());

        assertTrue(API.deleteListingRequest(listingIds));

    }

    @Test
    void updateListingRequest() {

        API.loginRequest("thesakox@gmail.com", "123123");

        Listing newListing = new Listing(3, "test listing", 10.0, 5, "test desc", 5, LocalDate.now(), 0, "category 1", "http://testurl.com");

        API.createListingRequest(newListing.getName(), newListing.getPrice(), newListing.getDesc(), newListing.getStock(), new File(imagepath), newListing.getCategory(), newListing.getCost());

        String listingId = API.getMessage();
        System.out.println(listingId);
        newListing.setId(Integer.parseInt(listingId));
        newListing.setPrice(50000.5);
        File pic = null;
        boolean b = API.updateListingRequest(newListing, pic);
        assertTrue(b);

    }

    @Test
    void fetchAllListingsPayload() {

        API.loginRequest("thesakox@gmail.com", "123123");
        API.createListingRequest("test listing", 10.0, "test desc", 10, new File("..../main/java/resources/view/e-shop.png"), "category 1", 122.0);
        API.fetchAllListingsRequest();

        ObservableList<Listing> listins = API.fetchAllListingsPayload();

        assertTrue(listins.size() > 0);

    }

    @Test
    void hasError() {

        assertFalse(API.loginRequest("thesakox@gmail.com", "wrong password")); //Invalid login


        assertFalse(API.deleteListingRequest(new ArrayList<>())); //Unauthorized.

        assertFalse(API.fetchAllListingsRequest()); //Unauthorized.

    }

    @Test
    void getError() {
        String res;
        API.loginRequest("thesakox@gmail.com", "wrong password"); //Invalid login
        res = API.getError();
        assertEquals("Password is incorrect.", res);

        API.deleteListingRequest(new ArrayList<>()); //Unauthorized.
        res = API.getError();
        assertEquals("Unauthorized.", res);

        API.fetchAllListingsRequest(); //Unauthorized.
        res = API.getError();
        assertEquals("Unauthorized.", res);

    }

    @Test
    void getMessage() {

        String res;
        API.loginRequest("thesakox@gmail.com", "123123");
        res = API.getMessage();
        assertEquals("Auth successful.", res);

        API.loginRequest("thesakox@gmail.com", "123123");
        res = API.getMessage();
        assertEquals("Already Logged In.", res);

        ArrayList<String> tempList = new ArrayList<>();
        tempList.add("99999999999");//This listing id cannot exist.
        API.deleteListingRequest(tempList);
        res = API.getMessage();
        assertEquals("Listings deleted : 0", res);
    }


    @BeforeEach
    void setUp() {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
}