package com.bookrenew.api;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RenewalsTests {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private JsonNode responseRoot;
    private String authToken;

    @Test
    @Order(1)
    void setAuthTokenUser1() throws JSONException {
        this.registerFirstUser();
        this.loginFirstUser();
        Assertions.assertNotNull(authToken);
    }
    @Test
    @Order(2)
    void setAuthTokenUser2() throws JSONException {
        this.registerSecondUser();
        this.loginSecondUser();
        Assertions.assertNotNull(authToken);
    }





    @Test
    @AfterAll
    void testDeleteUsers() throws JSONException
    {
        this.loginFirstUser();
        deleteUsers();
        this.loginSecondUser();
        deleteUsers();
    }


    private void registerFirstUser() throws JSONException{
        JSONObject user = this.buildFirstUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }
    private void registerSecondUser() throws JSONException{
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }
    private JSONObject buildFirstUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser");
        userJsonObject.put("email", "book1@test.com");
        userJsonObject.put("password", "password");
        return userJsonObject;
    }
    private JSONObject buildSecondUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser1");
        userJsonObject.put("email", "book2@test.com");
        userJsonObject.put("password", "password");
        return userJsonObject;
    }
    private HttpEntity<String> buildRequest(JSONObject data) {
        HttpHeaders headers = this.buildHeaders();
        return new HttpEntity<>(data.toString(), headers);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.setBearerAuth(authToken);
        }
        return headers;
    }
    private void loginFirstUser() throws JSONException {
        authToken = null;
        JSONObject user = this.buildFirstUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
        Assertions.assertNotNull(authToken);
    }
    private void loginSecondUser() throws JSONException {
        authToken = null;
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
        Assertions.assertNotNull(authToken);
    }
    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST,
                request, String.class);
    }
    private void sendRegisterRequest(HttpEntity<String> request) {
        restTemplate.postForObject(baseUrl + "users/register", request, String.class);
    }
    private JSONObject buildBookJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("title", "Harry Potter and the Deathly Hollows");
        userJsonObject.put("isbn", "9780545010221");
        return userJsonObject;
    }
    private JSONObject buildSecondBookJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("title", "Harry Potter and the Sorcerer's Stone");
        userJsonObject.put("isbn", "9780590353427");
        return userJsonObject;
    }
    private void sendBookPostRequestToLibrary(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "books/library", request, String.class);
        assert userResultsAsJsonString != null;
        responseRoot = objectMapper.readTree(userResultsAsJsonString);
    }
    private void sendBookPostRequestToWishList(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "books/wishlist", request, String.class);
        assert userResultsAsJsonString != null;
        responseRoot = objectMapper.readTree(userResultsAsJsonString);
    }
    private ResponseEntity<String> sendDeleteUserRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.DELETE, request, String.class);
    }
    private void deleteUsers()
    {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    private ResponseEntity<String> sendGetMatchesRequest(HttpEntity<String> request)
    {
        return restTemplate.exchange(baseUrl+"users/potential-trades", HttpMethod.GET, request, String.class);
    }
}
