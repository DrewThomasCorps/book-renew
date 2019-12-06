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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PotentialTradesTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private JsonNode responseRoot;
    private String authToken;

    @BeforeAll
    void setup() throws JSONException {
        this.registerFirstUser();
        this.registerSecondUser();
        this.registerThirdUser();
        this.loginFirstUser();
    }

    @AfterAll
    void deleteUsers() throws JSONException {
        this.loginFirstUser();
        deleteUser();
        this.loginSecondUser();
        deleteUser();
        this.loginThirdUser();
        deleteUser();
    }

    @Test
    @Order(1)
    void testHavingNoBooks_ReturnsEmptyPotentialTrades() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        JsonNode potentialTradesJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertTrue(potentialTradesJson.isEmpty());
    }

    @Test
    @Order(2)
    void testGetPotentialTrades_ReturnsSingleMatch() throws IOException, JSONException {
        this.addMatchingBooksToFirstAndSecondUsers();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNotNull(responseRoot.get(0));
        Assertions.assertNull(responseRoot.get(1));
    }

    @Test
    @Order(3)
    void testGetPotentialTrades_ReturnsLoggedInUserAsTraderEmail()
    {
        Assertions.assertEquals("book2@test.com", responseRoot.get(0).get("trader").get("user").get("email").asText());
    }

    @Test
    @Order(4)
    void testGetPotentialTrades_ReturnsCorrectTradeeEmail()
    {
        Assertions.assertEquals("book1@test.com", responseRoot.get(0).get("tradee").get("user").get("email").asText());
    }

    @Test
    @Order(5)
    void testGetPotentialTrades_ReturnsCorrectTraderBook()
    {
        Assertions.assertEquals("9780590353427", responseRoot.get(0).get("trader").get("book").get("isbn").asText());
    }

    @Test
    @Order(6)
    void testGetPotentialTrades_ReturnsCorrectTradeeBook()
    {
        Assertions.assertEquals("9780545010221", responseRoot.get(0).get("tradee").get("book").get("isbn").asText());
    }

    @Test
    @Order(7)
    void testTwoMatchesForSameUser_ReturnsTwoPotentialTrades() throws IOException, JSONException {
        this.addThirdBookToUserWishlist();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNotNull(responseRoot.get(0));
        Assertions.assertNotNull(responseRoot.get(1));
        Assertions.assertNull(responseRoot.get(2));
    }

    @Test
    @Order(8)
    void testThreeMatchesWithDifferentUsers_ReturnsThreePotentialTrades() throws JSONException, IOException {
        this.setupThirdUserBooks();
        this.loginSecondUser();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNotNull(responseRoot.get(0));
        Assertions.assertNotNull(responseRoot.get(1));
        Assertions.assertNotNull(responseRoot.get(2));
        Assertions.assertNull(responseRoot.get(3));
    }

    private ResponseEntity<String> sendGetMatchesRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "potential-trades", HttpMethod.GET, request, String.class);
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

    private JSONObject buildFirstBookJsonObject() throws JSONException {
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

    private void addMatchingBooksToFirstAndSecondUsers() throws IOException, JSONException {
        this.addBooksToFirstUserLibrary();
        this.addBookToFirstUserWishlist();
        this.loginSecondUser();
        this.addBookToSecondUserLibrary();
        this.addBookToSecondUserWishlist();
    }

    void addBooksToFirstUserLibrary() throws JSONException, IOException {
        JSONObject book = this.buildFirstBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
        book = this.buildThirdBookJsonObject();
        request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }

    void addBookToFirstUserWishlist() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    void addBookToSecondUserLibrary() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }

    void addBookToSecondUserWishlist() throws JSONException, IOException {
        JSONObject book = this.buildFirstBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    void addThirdBookToUserWishlist() throws JSONException, IOException {
        JSONObject book = this.buildThirdBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    void setupThirdUserBooks() throws JSONException, IOException {
        this.loginThirdUser();
        this.addBookToThirdUserLibrary();
        this.addBookToThirdUserWishlist();
    }

    void addBookToThirdUserLibrary() throws JSONException, IOException {

        JSONObject book = this.buildThirdBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }

    void addBookToThirdUserWishlist() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
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

    private void deleteUser() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
}
