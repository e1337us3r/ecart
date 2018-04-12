import com.apolets.main.API;
import com.apolets.main.Listing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class APITest {


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

        assertTrue(API.createListingRequest("test listing", "10.0", "test desc", "10", "http://testurl.com", "category 1"));
        assertFalse(API.createListingRequest("test listing", "10.0", "test desc", "10", "http://testurl.com", "qqqq"));
        assertFalse(API.createListingRequest("", "10.0", "test desc", "10", "http://testurl.com", "category 1"));
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

        API.createListingRequest("test listing", "10.0", "test desc", "10", "http://testurl.com", "category 1");

        ArrayList<String> listingIds = new ArrayList<>();
        listingIds.add(API.getMessage());

        assertTrue(API.deleteListingRequest(listingIds));

    }

    @Test
    void updateListingRequest() {

        API.loginRequest("thesakox@gmail.com", "123123");

        Listing newListing = new Listing(3, "test listing", 10.0, "test desc", 5, LocalDateTime.now(), 0, "category 1", "http://testurl.com");

        API.createListingRequest(newListing.getName(), String.valueOf(newListing.getPrice()), newListing.getDesc(), String.valueOf(newListing.getStock()), newListing.getStoreImage(), newListing.getCategory());

        String listingId = API.getMessage();

        newListing.setId(Integer.parseInt(listingId));
        newListing.setPrice(50000.5);

        assertTrue(API.updateListingRequest(newListing));

    }

    @Test
    void fetchAllListingsPayload() {

        API.loginRequest("thesakox@gmail.com", "123123");
        API.createListingRequest("test listing", "10.0", "test desc", "10", "http://testurl.com", "category 1");
        API.fetchAllListingsRequest();

        ArrayList<Listing> listins = API.fetchAllListingsPayload();

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