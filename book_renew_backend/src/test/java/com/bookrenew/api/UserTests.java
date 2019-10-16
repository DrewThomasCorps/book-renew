package com.bookrenew.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTests {

    private RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private JsonNode responseRoot;

    @Test
    public void testCRUD() throws JSONException, IOException {
        this.testCreateUser();
    }

    private void testCreateUser() throws JSONException, IOException {
        JSONObject userJsonObject = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        this.sendPostRequest(request);
        this.testPostResponseContainsCorrectData();
    }

    private JSONObject buildUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser");
        userJsonObject.put("email", "test@test.com");
        userJsonObject.put("username", "testUsername");
        userJsonObject.put("password", "testPassword");
        return userJsonObject;
    }

    private HttpEntity<String> buildRequest(JSONObject data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data.toString(), headers);
    }

    private void sendPostRequest(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "users", request, String.class);
        responseRoot = objectMapper.readTree(userResultsAsJsonString);
    }

    private void testPostResponseContainsCorrectData() {
        Assert.assertNotNull(responseRoot);
        Assert.assertEquals("testUser", responseRoot.path("name").asText());
        Assert.assertEquals("test@test.com", responseRoot.path("email").asText());
        Assert.assertEquals("testUsername", responseRoot.path("username").asText());
        Assert.assertEquals("", responseRoot.path("password").asText());
        Assert.assertNotNull(responseRoot.path("id"));
    }


}
