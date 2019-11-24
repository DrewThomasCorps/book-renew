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
class BookTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private JsonNode responseRoot;
    private String authToken;

    @AfterAll
    void deleteUser() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        this.sendDeleteUserRequest(request);
    }

    @Test
    @Order(1)
    void testReturns403WhenNotAuthenticated() throws JSONException {
        JSONObject book = this.buildBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendBookPostRequest(request));
        Assertions.assertEquals(403, exception.getRawStatusCode());
    }

    @Test
    @Order(2)
    void setAuthToken() throws JSONException, IOException {
        this.registerUser();
        this.login();
        Assertions.assertNotNull(authToken);
    }

    @Test
    @Order(3)
    void testCreateBook() throws JSONException, IOException {
        JSONObject book = this.buildBookJsonObject();
        HttpEntity<String> request = this.buildRequest(book);
        this.sendBookPostRequest(request);
        this.testBookResponseContainsCorrectInformation();
    }

    @Test
    @Order(4)
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
    @Order(5)
    void testDeleteBook() {
        String id = responseRoot.path("id").asText();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteBookRequest(request, id);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(6)
    void testDeleteBookWithNonExistentIdReturns400() {
        String id = responseRoot.path("id").asText();
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendDeleteBookRequest(request, id));
        Assertions.assertEquals(400, exception.getRawStatusCode());
    }
    @Test
    @Order(7)
    void testDuplicateBookthrows409() throws JSONException, IOException
    {
        JSONObject userJsonObject = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRegisterRequest(request));
        Assertions.assertEquals(409, exception.getRawStatusCode());
    }

    private ResponseEntity<String> sendDeleteBookRequest(HttpEntity<String> request, String id) {
        return restTemplate.exchange(baseUrl + "books/" + id, HttpMethod.DELETE, request, String.class);
    }

    private JSONObject buildBookJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("title", "Harry Potter and the Deathly Hollows");
        userJsonObject.put("isbn", "9780545010221");
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

    private void sendBookPostRequest(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "books/library", request, String.class);
        assert userResultsAsJsonString != null;
        responseRoot = objectMapper.readTree(userResultsAsJsonString);
    }

    private void registerUser() throws JSONException, IOException {
        JSONObject user = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        this.sendRegisterRequest(request);
    }

    private JSONObject buildUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser");
        userJsonObject.put("email", "book@test.com");
        userJsonObject.put("password", "password");
        return userJsonObject;
    }

    private void sendRegisterRequest(HttpEntity<String> request) throws IOException {
        restTemplate.postForObject(baseUrl + "users/register", request, String.class);
    }

    private void login() throws JSONException, IOException {
        JSONObject user = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(user);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
    }

    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) throws IOException {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST,
                request, String.class);
    }

    private void testBookResponseContainsCorrectInformation() {
        Assertions.assertNotNull(responseRoot);
        Assertions.assertEquals("Harry Potter and the Deathly Hollows", responseRoot.path("book").path("title").asText());
        Assertions.assertEquals("9780545010221", responseRoot.path("book").path("isbn").asText());
    }

    private void sendDeleteUserRequest(HttpEntity<String> request) {
        restTemplate.exchange(baseUrl + "users/self", HttpMethod.DELETE, request, String.class);
    }

    private ResponseEntity<String> sendGetBooksRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "books", HttpMethod.GET, request, String.class);
    }
}
