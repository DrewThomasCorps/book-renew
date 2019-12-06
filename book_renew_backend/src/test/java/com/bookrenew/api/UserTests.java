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
class UserTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private JsonNode responseRoot;
    private String authToken;

    @Test
    @Order(1)
    void testUserRegister_ReturnsUserName() throws JSONException, IOException {
        JSONObject userJsonObject = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        this.sendRegisterRequest(request);
        Assertions.assertEquals("testUser", responseRoot.path("name").asText());
    }

    @Test
    @Order(2)
    void testUserRegister_ReturnsUserEmail() {
        Assertions.assertEquals("test@test.com", responseRoot.path("email").asText());
    }

    @Test
    @Order(3)
    void testUserRegister_DoesNotReturnUserPassword() {
        Assertions.assertEquals("", responseRoot.path("password").asText());
    }

    @Test
    @Order(4)
    void testUserRegister_ReturnsUserId() {
        Assertions.assertNotNull(responseRoot.path("id"));
    }

    @Test
    @Order(5)
    void testUserRegisterWithoutCredentials_Returns400() throws JSONException {
        JSONObject userJsonObject = this.buildEmptyLoginCredentials();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRegisterRequest(request));
        Assertions.assertEquals(400, exception.getRawStatusCode());
    }

    @Test
    @Order(6)
    void testCreatingDuplicateUser_Returns409() throws JSONException {
        JSONObject userJsonObject = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRegisterRequest(request));
        Assertions.assertEquals(409, exception.getRawStatusCode());
    }

    @Test
    @Order(7)
    void testLoginFailsWithIncorrectCredentials_Returns403() throws JSONException {
        JSONObject incorrectLoginCredentials = this.buildIncorrectLoginCredentials();
        HttpEntity<String> request = this.buildRequest(incorrectLoginCredentials);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendLoginRequest(request));
        Assertions.assertEquals(403, exception.getRawStatusCode());
    }

    @Test
    @Order(8)
    void testLoginSuccess_ReturnsAuthToken() throws JSONException {
        JSONObject loginCredentials = this.buildLoginCredentials();
        HttpEntity<String> request = this.buildRequest(loginCredentials);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
        Assertions.assertNotNull(authToken);
    }

    @Test
    @Order(9)
    void testGetAuthenticatedUser_ReturnsAuthenticatedUser() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetAuthenticatedUserRequest(request);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
        Assertions.assertEquals("testUser", responseRoot.path("name").asText());
    }

    @Test
    @Order(10)
    void testDeleteUser() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    private ResponseEntity<String> sendDeleteRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.DELETE, request, String.class);
    }

    private ResponseEntity<String> sendGetAuthenticatedUserRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.GET, request, String.class);
    }

    private JSONObject buildLoginCredentials() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("email", "test@test.com");
        credentials.put("password", "testPassword");
        return credentials;
    }

    private JSONObject buildEmptyLoginCredentials() throws JSONException{
        JSONObject credentials = new JSONObject();
        credentials.put("email", "");
        credentials.put("password", "");
        return credentials;
    }

    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST,
                request, String.class);
    }

    private JSONObject buildIncorrectLoginCredentials() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("email", "test@test.com");
        credentials.put("password", "wrong_password");
        return credentials;
    }

    private JSONObject buildUserJsonObject() throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser");
        userJsonObject.put("email", "test@test.com");
        userJsonObject.put("password", "testPassword");
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

    private void sendRegisterRequest(HttpEntity<String> request) throws IOException {
        String userResultsAsJsonString = restTemplate.postForObject(baseUrl + "users/register", request, String.class);
        responseRoot = objectMapper.readTree(Objects.requireNonNull(userResultsAsJsonString));
    }
}

