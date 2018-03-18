import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

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








        boolean loginStatus = API.signin("thesakox@gmail.com","123123");
        if (loginStatus) {
            ArrayList<ListingItem>  itemsList =  API.fetchAllItems();
            for (ListingItem li : itemsList){
                System.out.println(li.getName());
            }
        }


    }


}
