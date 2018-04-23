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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class APITest {

    private final String imagepath = "C:\\Users\\AVA\\IdeaProjects\\testapimaven\\src\\main\\resources\\view\\e-shop.png";

    private final List<File> image = Collections.singletonList(new File(imagepath));

    @Test
    void loginRequest() {
        String[] emails = {"testmerchant@gmail.com", "fake@gmail.com", "testmember@gmail.com", "testmerchant@gmail.com"};
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
        API.loginRequest("testmerchant@gmail.com", "123123");
        assertTrue(API.createListingRequest("test listing", 10.0, "test desc", 10, "category 1", 122.0, image));
        assertFalse(API.createListingRequest("test listing", 10.0, "test desc", 10, "cat", 122.0, image));
        assertFalse(API.createListingRequest("te", 10.0, "test desc", 10, "category 1", 122.0, image));

    }

    @Test
    void fetchAllListingsRequest() {

        API.loginRequest("testmerchant@gmail.com", "12312");

        assertFalse(API.fetchAllListingsRequest());

        API.loginRequest("testmerchant@gmail.com", "123123");

        assertTrue(API.fetchAllListingsRequest());


    }

    @Test
    void deleteListingRequest() {

        API.loginRequest("testmerchant@gmail.com", "123123");

        API.createListingRequest("test listing", 10.0, "test desc", 10, "category 1", 122.0, image);

        Listing newListing = new Listing(API.getMessage());

        assertTrue(API.deleteListingRequest(newListing.getId()));

    }

    @Test
    void updateListingRequest() {

        API.loginRequest("testmerchant@gmail.com", "123123");

        Listing newListing = new Listing(3, "test listing", 10.0, 5, "test desc", 5, LocalDate.now(), 0, "category 1", "http://testurl.com");

        API.createListingRequest(newListing.getName(), newListing.getPrice(), newListing.getDesc(), newListing.getStock(), newListing.getCategory(), newListing.getCost(), image);

        int listingId = new Listing(API.getMessage()).getId();
        //System.out.println(listingId);
        newListing.setId(listingId);
        newListing.setPrice(50000.5);
        File pic = null;
        boolean result = API.updateListingRequest(newListing, new ArrayList<>(), new ArrayList<>(), "");
        assertTrue(result);

    }

    @Test
    void fetchAllListingsPayload() {

        API.loginRequest("testmerchant@gmail.com", "123123");
        API.createListingRequest("test listing", 10.0, "test desc", 10, "category 1", 122.0, image);
        API.createListingRequest("test listing", 10.0, "test desc", 10, "category 1", 122.0, image);
        API.fetchAllListingsRequest();

        ObservableList<Listing> listins = API.fetchAllListingsPayload();

        assertTrue(listins.size() > 0);

    }

    @Test
    void hasError() {

        assertFalse(API.loginRequest("testmerchant@gmail.com", "wrong password")); //Invalid login


        assertFalse(API.deleteListingRequest(2)); //Unauthorized.

        assertFalse(API.fetchAllListingsRequest()); //Unauthorized.

    }

    @Test
    void getError() {
        String res;
        API.loginRequest("testmerchant@gmail.com", "wrong password"); //Invalid login
        res = API.getError();
        assertEquals("Password is incorrect.", res);

        API.deleteListingRequest(2); //Unauthorized.
        res = API.getError();
        assertEquals("Unauthorized.", res);

        API.fetchAllListingsRequest(); //Unauthorized.
        res = API.getError();
        assertEquals("Unauthorized.", res);

    }

    @Test
    void getMessage() {

        String res;
        API.loginRequest("testmerchant@gmail.com", "123123");
        res = API.getMessage();
        assertEquals("Auth successful.", res);

        API.loginRequest("testmerchant@gmail.com", "123123");
        res = API.getMessage();
        assertEquals("Already Logged In.", res);


        API.deleteListingRequest(999999999);
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