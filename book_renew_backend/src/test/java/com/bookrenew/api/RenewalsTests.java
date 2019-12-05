package com.bookrenew.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.web.client.HttpClientErrorException;
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
    private Long libraryId1;
    private Long libraryId2;
    private Long wishlistId1;


    @Test
    @Order(1)
    void setUp() throws JSONException, IOException
    {
        this.registerFirstUser();
        this.loginFirstUser();
        JSONObject book = this.buildBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
        libraryId2 = responseRoot.path("id").asLong();
        book = this.buildSecondBookJsonObject();
        request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }
    @Test
    @Order(2)
    void setUp2() throws JSONException, IOException
    {
        this.registerSecondUser();
        this.loginSecondUser();
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
        libraryId1 = responseRoot.path("id").asLong();
        book = this.buildBookJsonObject();
        request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
        wishlistId1 = responseRoot.path("id").asLong();
    }

    @Test
    @Order(3)
    void testCreateCorrectRenewal() throws JSONException, IOException
    {
        JSONObject renewal = this.buildRenewal(libraryId1, libraryId2);
        HttpEntity<String> request = this.buildRequest(renewal);
        this.sendRenewalPostRequest(request);
    }

    @Test
    @Order(4)
    void testTradingFromWishListThrows400() throws JSONException
    {
        JSONObject renewal = this.buildRenewal(wishlistId1, libraryId2);
        HttpEntity<String> request = this.buildRequest(renewal);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRenewalPostRequest(request));
        Assertions.assertEquals(400, exception.getRawStatusCode());
    }

    @Test
    @Order(5)
    void testTradingWithUnownedBookThrows403() throws JSONException
    {
        JSONObject renewal = this.buildRenewal(libraryId2, libraryId1);
        HttpEntity<String> request = this.buildRequest(renewal);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRenewalPostRequest(request));
        Assertions.assertEquals(403, exception.getRawStatusCode());
    }

    @Test
    @Order(6)
    void testDeleteRenewals() throws IOException
    {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetRenewalsRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        String id = userJson.get(0).path("id").toString();
        response = this.sendDeleteRenewalsRequest(request, id);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(7)
    void testDeleteUsers() throws JSONException {
        this.loginFirstUser();
        deleteUsers();
        this.loginSecondUser();
        deleteUsers();
    }


    private void registerFirstUser() throws JSONException {
        JSONObject user = this.buildFirstUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }

    private void registerSecondUser() throws JSONException {
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }

    private JSONObject buildFirstUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser");
        userJsonObject.put("email", "book10@test.com");
        userJsonObject.put("password", "password");
        return userJsonObject;
    }

    private JSONObject buildSecondUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser1");
        userJsonObject.put("email", "book20@test.com");
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
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
    }

    private void loginSecondUser() throws JSONException {
        authToken = null;
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
    }

    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST, request, String.class);
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
    private void sendRenewalPostRequest(HttpEntity<String> request) throws IOException{
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "renewals", request, String.class);
        assert userResultsAsJsonString != null;
        responseRoot = objectMapper.readTree(userResultsAsJsonString);
    }

    private void deleteUsers() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    private JSONObject buildRenewal(Long libraryId, Long wishlistId) throws JSONException
    {
        JSONObject renewalJsonObject = new JSONObject();
        renewalJsonObject.put("trader_book_user_id", libraryId);
        renewalJsonObject.put("tradee_book_user_id", wishlistId);
        return renewalJsonObject;
    }

    private ResponseEntity<String> sendDeleteRenewalsRequest(HttpEntity<String> request, String id)
    {
        return restTemplate.exchange(baseUrl + "renewals/"+id, HttpMethod.DELETE, request, String.class);
    }
    private ResponseEntity<String> sendGetRenewalsRequest(HttpEntity<String> request)
    {
        return restTemplate.exchange(baseUrl + "renewals", HttpMethod.GET, request, String.class);
    }
}

