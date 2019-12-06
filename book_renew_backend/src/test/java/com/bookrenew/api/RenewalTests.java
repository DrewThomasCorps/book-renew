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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RenewalTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private JsonNode responseRoot;
    private String authToken;
    private Long firstUserLibraryBookId;
    private Long secondUserLibraryBookId;
    private Long wishlistBookId;


    @BeforeAll
    void setup() throws JSONException, IOException {
        this.setupFirstUser();
        this.setupSecondUser();
    }

    @AfterAll
    void deleteUsers() throws JSONException {
        this.loginFirstUser();
        deleteUser();
        this.loginSecondUser();
        deleteUser();
    }

    @Test
    @Order(1)
    void testOfferingFromWishList_Throws400() throws JSONException {
        JSONObject renewal = this.buildRenewal(wishlistBookId, firstUserLibraryBookId);
        HttpEntity<String> request = this.buildRequest(renewal);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendOfferRenewalRequest(request));
        Assertions.assertEquals(400, exception.getRawStatusCode());
    }

    @Test
    @Order(2)
    void testOfferingOtherUsersBook_Throws403() throws JSONException {
        JSONObject renewal = this.buildRenewal(firstUserLibraryBookId, secondUserLibraryBookId);
        HttpEntity<String> request = this.buildRequest(renewal);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendOfferRenewalRequest(request));
        Assertions.assertEquals(403, exception.getRawStatusCode());
    }

    @Test
    @Order(3)
    void testOfferRenewal_ReturnsPendingRenewal() throws JSONException, IOException {
        JSONObject renewal = this.buildRenewal(secondUserLibraryBookId, firstUserLibraryBookId);
        HttpEntity<String> request = this.buildRequest(renewal);
        this.sendOfferRenewalRequest(request);
        Assertions.assertEquals("pending", responseRoot.get("status").asText());
    }

    @Test
    @Order(4)
    void testOfferRenewal_ReturnsCorrectTraderBook() {
        Assertions.assertEquals("9780590353427", responseRoot.get("trader").get("book").get("isbn").asText());
    }

    @Test
    @Order(5)
    void testOfferRenewal_ReturnsCorrectTraderUser() {
        Assertions.assertEquals("bookSecond@test.com", responseRoot.get("trader").get("user").get("email").asText());
    }

    @Test
    @Order(6)
    void testOfferRenewal_ReturnsCorrectTradeeBook() {
        Assertions.assertEquals("9780545010221", responseRoot.get("tradee").get("book").get("isbn").asText());
    }

    @Test
    @Order(7)
    void testOfferRenewal_ReturnsCorrectTradeeUser() {
        Assertions.assertEquals("bookFirst@test.com", responseRoot.get("tradee").get("user").get("email").asText());
    }

    @Test
    @Order(8)
    void testCompleteRenewal_ReturnsCompleteStatus() throws JSONException, JsonProcessingException {
        JSONObject updateRenewalJSON = new JSONObject();
        updateRenewalJSON.put("status", "completed");
        HttpEntity<String> request = this.buildRequest(updateRenewalJSON);
        String id = responseRoot.path("id").toString();
        ResponseEntity<String> response = this.sendPutRenewalsRequest(request, id);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertEquals("completed", responseRoot.get("status").asText());
    }

    @Test
    @Order(9)
    void testCompletedRenewal_TraderHasTradedStatus() {
        Assertions.assertEquals("traded", responseRoot.get("trader").get("status").asText());
    }

    @Test
    @Order(10)
    void testCompletedRenewal_TradeeHasTradedStatus() {
        Assertions.assertEquals("traded", responseRoot.get("tradee").get("status").asText());
    }

    @Test
    @Order(11)
    void testGetRenewals_ReturnsRenewal() throws JsonProcessingException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetRenewalsRequest(request);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNotNull(responseRoot.get(0).get("id"));
        Assertions.assertNull(responseRoot.get(1));
    }

    @Test
    @Order(12)
    void testDeleteRenewal_Returns200() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        String id = responseRoot.get(0).path("id").toString();
        ResponseEntity<String> response = this.sendDeleteRenewalsRequest(request, id);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(13)
    void testDeleteMissingRenewal_Throws404() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        String id = responseRoot.get(0).path("id").toString();
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendDeleteRenewalsRequest(request, id));
        Assertions.assertEquals(404, exception.getRawStatusCode());
    }

    private void setupFirstUser() throws JSONException, IOException {
        this.registerFirstUser();
        this.loginFirstUser();
        this.addFirstUserBooks();
    }

    private void addFirstUserBooks() throws JSONException, IOException {
        JSONObject book = this.buildFirstBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendAddToLibraryRequest(request);
        firstUserLibraryBookId = responseRoot.path("id").asLong();
        book = this.buildSecondBookJsonObject();
        request = this.buildRequest(book);
        this.sendAddToWishListRequest(request);
    }

    private void setupSecondUser() throws JSONException, IOException {
        this.registerSecondUser();
        this.loginSecondUser();
        this.addSecondUserBooks();
    }

    private void addSecondUserBooks() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendAddToLibraryRequest(request);
        secondUserLibraryBookId = responseRoot.path("id").asLong();
        book = this.buildFirstBookJsonObject();
        request = this.buildRequest(book);
        this.sendAddToWishListRequest(request);
        wishlistBookId = responseRoot.path("id").asLong();
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
        userJsonObject.put("email", "bookFirst@test.com");
        userJsonObject.put("password", "password");
        return userJsonObject;
    }

    private JSONObject buildSecondUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser1");
        userJsonObject.put("email", "bookSecond@test.com");
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

    private void sendAddToLibraryRequest(HttpEntity<String> request) throws IOException {
        String bookResultsAsJsonString = restTemplate.postForObject(baseUrl + "books/library", request, String.class);
        assert bookResultsAsJsonString != null;
        responseRoot = objectMapper.readTree(bookResultsAsJsonString);
    }

    private void sendAddToWishListRequest(HttpEntity<String> request) throws IOException {
        String bookResultsAsJsonString = restTemplate.postForObject(baseUrl + "books/wishlist", request, String.class);
        assert bookResultsAsJsonString != null;
        responseRoot = objectMapper.readTree(bookResultsAsJsonString);
    }

    private void sendOfferRenewalRequest(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "renewals", request, String.class);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(userResultsAsJsonString));
    }

    private void deleteUser() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    private ResponseEntity<String> sendDeleteUserRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.DELETE, request, String.class);
    }

    private JSONObject buildRenewal(Long libraryId, Long wishlistId) throws JSONException {
        JSONObject renewalJsonObject = new JSONObject();
        renewalJsonObject.put("trader_book_user_id", libraryId);
        renewalJsonObject.put("tradee_book_user_id", wishlistId);
        return renewalJsonObject;
    }

    private ResponseEntity<String> sendDeleteRenewalsRequest(HttpEntity<String> request, String id) {
        return restTemplate.exchange(baseUrl + "renewals/" + id, HttpMethod.DELETE, request, String.class);
    }

    private ResponseEntity<String> sendGetRenewalsRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "renewals", HttpMethod.GET, request, String.class);
    }

    private ResponseEntity<String> sendPutRenewalsRequest(HttpEntity<String> request, String id) {
        return restTemplate.exchange(baseUrl + "renewals/" + id, HttpMethod.PUT, request, String.class);
    }
}

