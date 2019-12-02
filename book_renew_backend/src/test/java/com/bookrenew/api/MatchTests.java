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
public class MatchTests {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private JsonNode responseRoot;
    private String authToken;

    @Test
    @Order(1)
    void setAuthTokenUser1() throws JSONException, IOException {
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
    void testNoBooks() throws JSONException, IOException
    {
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
        this.testBookResponseContainsCorrectInformation();
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
        JSONObject book = this.buildBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToWishList(request);
        this.testBookResponseContainsCorrectInformation();
    }

    @Test
    @Order(7)
    void setAuthTokenUser2() throws JSONException, IOException {
        this.registerSecondUser();
        this.loginSecondUser();
        Assertions.assertNotNull(authToken);
    }

    @Test
    @Order(8)
    void testCreateSecondBookLibrary() throws JSONException, IOException
    {
        JSONObject book = this.buildSecondBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequestToLibrary(request);
    }

    @Test
    @Order(9)
    void testNoMatches() throws IOException
    {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetMatchesRequest(request);
        JsonNode userJson = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertNull(userJson.get(0));
    }

    @Test
    @Order(10)
    void testCreateSecondBookWishList() throws  IOException
    {


    }

    @Test
    @Order(11)
    void testOneMatch() throws JSONException, IOException
    {

    }

    @Test
    @Order(12)
    void testMultipleMatchesDifferentUsers() throws JSONException, IOException
    {

    }

    @Test
    @Order(13)
    void testMultipleMatchesSameUser() throws JSONException, IOException
    {

    }

    @Test
    @AfterAll
    void testDeleteUsers() throws JSONException, IOException
    {
        this.loginFirstUser();
        deleteUsers();
        this.loginSecondUser();
        deleteUsers();
    }

    private ResponseEntity<String> sendGetAuthenticatedUserRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.GET, request, String.class);
    }

    private ResponseEntity<String> sendGetMatchesRequest(HttpEntity<String> request)
    {
        return restTemplate.exchange(baseUrl+"users/potential-trades", HttpMethod.GET, request, String.class);
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

    private void registerFirstUser() throws JSONException, IOException {
        JSONObject user = this.buildFirstUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }
    private void registerSecondUser() throws JSONException, IOException {
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }

    private void loginFirstUser() throws JSONException, IOException {
        JSONObject user = this.buildFirstUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
        Assertions.assertNotNull(authToken);
    }
    private void loginSecondUser() throws JSONException, IOException {
        JSONObject user = this.buildSecondUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
        Assertions.assertNotNull(authToken);
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

    private void sendRegisterRequest(HttpEntity<String> request) throws IOException {
        restTemplate.postForObject(baseUrl + "users/register", request, String.class);
    }

    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) throws IOException {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST, request, String.class);
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
    private void testBookResponseContainsCorrectInformation() {
        Assertions.assertNotNull(responseRoot);
        Assertions.assertEquals("Harry Potter and the Deathly Hollows", responseRoot.path("book").path("title").asText());
        Assertions.assertEquals("9780545010221", responseRoot.path("book").path("isbn").asText());
    }

    private ResponseEntity<String> sendDeleteBookRequest(HttpEntity<String> request, String id) {
        return restTemplate.exchange(baseUrl + "books/" + id, HttpMethod.DELETE, request, String.class);
    }
    private ResponseEntity<String> sendDeleteUserRequest(HttpEntity<String> request, String id) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.DELETE, request, String.class);
    }
    private void deleteUsers()
    {
        String id = responseRoot.path("user_id").asText();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteUserRequest(request, id);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
    private ResponseEntity<String> sendGetBooksRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "books", HttpMethod.GET, request, String.class);
    }
}
