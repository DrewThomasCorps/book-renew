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
    @Order(0)
    void setup() throws JSONException, IOException {
        this.setupFirstUser();
        this.setupSecondUser();
    }

    @Test
    @Order(1)
    void testOfferRenewal_ReturnsPendingRenewal() throws JSONException, IOException {
        JSONObject renewal = this.buildRenewal(secondUserLibraryBookId, firstUserLibraryBookId);
        HttpEntity<String> request = this.buildRequest(renewal);
        this.sendRenewalPostRequest(request);
        Assertions.assertEquals("pending", responseRoot.get("status").asText());
    }

    @Test
    @Order(2)
    void testOfferRenewal_ReturnsCorrectTraderBook()
    {
        Assertions.assertEquals("9780590353427", responseRoot.get("trader").get("book").get("isbn").asText());
    }

    @Test
    @Order(3)
    void testOfferRenewal_ReturnsCorrectTraderUser()
    {
        Assertions.assertEquals("bookSecond@test.com", responseRoot.get("trader").get("user").get("email").asText());
    }

    @Test
    @Order(4)
    void testOfferRenewal_ReturnsCorrectTradeeBook()
    {
        Assertions.assertEquals("9780545010221", responseRoot.get("tradee").get("book").get("isbn").asText());
    }

    @Test
    @Order(5)
    void testOfferRenewal_ReturnsCorrectTradeeUser()
    {
        Assertions.assertEquals("bookFirst@test.com", responseRoot.get("tradee").get("user").get("email").asText());
    }

    @Test
    @Order(6)
    void testOfferingFromWishListThrows400() throws JSONException {
        JSONObject renewal = this.buildRenewal(wishlistBookId, firstUserLibraryBookId);
        HttpEntity<String> request = this.buildRequest(renewal);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRenewalPostRequest(request));
        Assertions.assertEquals(400, exception.getRawStatusCode());
    }

    @Test
    @Order(7)
    void testOfferingUnownedBookThrows403() throws JSONException {
        JSONObject renewal = this.buildRenewal(firstUserLibraryBookId, secondUserLibraryBookId);
        HttpEntity<String> request = this.buildRequest(renewal);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRenewalPostRequest(request));
        Assertions.assertEquals(403, exception.getRawStatusCode());
    }

    @Test
    @Order(8)
    void testDeleteRenewals() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetRenewalsRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        String id = userJson.get(0).path("id").toString();
        response = this.sendDeleteRenewalsRequest(request, id);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @AfterAll
    void deleteUsers() throws JSONException {
        this.loginFirstUser();
        deleteUser();
        this.loginSecondUser();
        deleteUser();
    }

    private void setupFirstUser() throws JSONException, IOException {
        this.registerFirstUser();
        this.loginFirstUser();
        this.addFirstUserBooks();
    }

    private void addFirstUserBooks() throws JSONException, IOException {
        JSONObject book = this.buildFirstBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
        firstUserLibraryBookId = responseRoot.path("id").asLong();
        book = this.buildSecondBookJsonObject();
        request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
    }

    private void setupSecondUser() throws JSONException, IOException {
        this.registerSecondUser();
        this.loginSecondUser();
        this.addSecondUserBooks();
    }

    private void addSecondUserBooks() throws JSONException, IOException {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
        secondUserLibraryBookId = responseRoot.path("id").asLong();
        book = this.buildFirstBookJsonObject();
        request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
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

    private void sendRenewalPostRequest(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "renewals", request, String.class);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(userResultsAsJsonString));
    }

    private void deleteUser() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
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
}

