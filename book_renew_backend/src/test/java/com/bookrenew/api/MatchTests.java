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
class MatchTests {
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
    void testGetAuthenticatedUser() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetAuthenticatedUserRequest(request);
        this.testAuthenticatedUserResponseResults(response);
    }

    @Test
    @Order(3)
    void testNoBooks() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetAuthenticatedUserRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNull(userJson.get("bookUsers"));
    }

    @Test
    @Order(4)
    void testCreateFirstBookLibrary() throws JSONException, IOException {
        JSONObject book = this.buildBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
        book = this.buildThirdBookJsonObject();
        request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }

    @Test
    @Order(5)
    void testUserOwnsBook() throws JsonProcessingException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetBooksRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertEquals("Harry Potter and the Deathly Hollows", userJson
                .get(0)
                .path("book")
                .path("title")
                .asText());
    }

    @Test
    @Order(6)
    void testCreateFirstBookWishList() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    @Test
    @Order(7)
    void testNoMatches() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNull(userJson.get(0));
    }

    @Test
    @Order(8)
    void setAuthTokenUser2() throws JSONException, IOException {
        this.registerSecondUser();
        this.loginSecondUser();
        Assertions.assertNotNull(authToken);
    }

    @Test
    @Order(9)
    void testCreateSecondBookLibrary() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }


    @Test
    @Order(10)
    void testCreateSecondBookWishList() throws JSONException, IOException {
        JSONObject book = this.buildBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);

    }

    @Test
    @Order(11)
    void testOneMatch() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        request = this.buildRequest(new JSONObject());
        response = this.sendGetAuthenticatedUserRequest(request);
        JsonNode userInfo = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertEquals(userJson.get(0).path("trader").path("user").path("id"), userInfo.path("id"));
        Assertions.assertNull(userJson.get(1));
    }

    @Test
    @Order(11)
    void testCreateThirdBookWishList() throws JSONException, IOException {
        JSONObject book = this.buildThirdBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    @Test
    @Order(12)
    void testMultipleMatchesSameUser() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        request = this.buildRequest(new JSONObject());
        response = this.sendGetAuthenticatedUserRequest(request);
        JsonNode userInfo = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertEquals(userJson.get(0).path("trader").path("user").path("id"), userInfo.path("id"));
        Assertions.assertEquals(userJson.get(1).path("trader").path("user").path("id"), userInfo.path("id"));
        Assertions.assertEquals(userJson.get(0).path("tradee").path("user").path("id"), userJson.get(1).path("tradee").path("user").path("id"));
        Assertions.assertNull(userJson.get(2));
    }

    @Test
    @Order(13)
    void setAuthTokenUser3() throws JSONException {
        this.registerThirdUser();
        this.loginThirdUser();
        Assertions.assertNotNull(authToken);
    }

    @Test
    @Order(14)
    void testCreateThirdBookLibrary() throws JSONException, IOException {
        JSONObject book = this.buildThirdBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }

    @Test
    @Order(15)
    void testCreateFourthBookWishList() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    @Test
    @Order(16)
    void testMultipleMatchesDifferentUsers() throws JSONException, IOException {
        this.loginSecondUser();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        request = this.buildRequest(new JSONObject());
        response = this.sendGetAuthenticatedUserRequest(request);
        JsonNode userInfo = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertEquals(userJson.get(0).path("trader").path("user").path("id"), userInfo.path("id"));
        Assertions.assertEquals(userJson.get(1).path("trader").path("user").path("id"), userInfo.path("id"));
        Assertions.assertEquals(userJson.get(2).path("trader").path("user").path("id"), userInfo.path("id"));
        Assertions.assertNotEquals(userJson.get(0).path("tradee").path("user").path("id"), userJson.get(2).path("tradee").path("user").path("id"));
        Assertions.assertNull(userJson.get(3));
    }

    @AfterAll
    void testDeleteUsers() throws JSONException {
        this.loginFirstUser();
        deleteUsers();
        this.loginSecondUser();
        deleteUsers();
        this.loginThirdUser();
        deleteUsers();
    }

    private ResponseEntity<String> sendGetAuthenticatedUserRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.GET, request, String.class);
    }

    private ResponseEntity<String> sendGetMatchesRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/potential-trades", HttpMethod.GET, request, String.class);
    }

    private void testAuthenticatedUserResponseResults(ResponseEntity<String> response) throws IOException {
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNotNull(responseRoot);
        Assertions.assertEquals("testUser", responseRoot.path("name").asText());
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

    private void registerThirdUser() throws JSONException {
        JSONObject user = this.buildThirdUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }

    private void loginFirstUser() throws JSONException {
        JSONObject user = this.buildFirstUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
    }

    private void loginSecondUser() throws JSONException {
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
    }

    private void loginThirdUser() throws JSONException {
        JSONObject user = this.buildThirdUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
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

    private JSONObject buildThirdUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser2");
        userJsonObject.put("email", "book3@test.com");
        userJsonObject.put("password", "password");
        return userJsonObject;
    }

    private void sendRegisterRequest(HttpEntity<String> request) {
        restTemplate.postForObject(baseUrl + "users/register", request, String.class);
    }

    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST, request, String.class);
    }

    private JSONObject buildBookJsonObject() throws JSONException {
        JSONObject bookJsonObject = new JSONObject();
        bookJsonObject.put("title", "Harry Potter and the Deathly Hollows");
        bookJsonObject.put("isbn", "9780545010221");
        return bookJsonObject;
    }

    private JSONObject buildSecondBookJsonObject() throws JSONException {
        JSONObject bookJsonObject = new JSONObject();
        bookJsonObject.put("title", "Harry Potter and the Sorcerer's Stone");
        bookJsonObject.put("isbn", "9780590353427");
        return bookJsonObject;
    }

    private JSONObject buildThirdBookJsonObject() throws JSONException {
        JSONObject bookJsonObject = new JSONObject();
        bookJsonObject.put("title", "Harry Potter and the Goblet of Fire");
        bookJsonObject.put("isbn", "9781781101544");
        return bookJsonObject;
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

    private void deleteUsers() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    private ResponseEntity<String> sendGetBooksRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "books", HttpMethod.GET, request, String.class);
    }
}
