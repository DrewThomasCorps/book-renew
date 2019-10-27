package com.bookrenew.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private JsonNode responseRoot;
    private String authToken;

    @Test
    void testUser() throws JSONException, IOException {
        this.testUserRegister();
        this.testDuplicateUser();
        this.testLoginFailsWithIncorrectCredentials();
        this.testLoginSuccess();
        this.testGetAuthenticatedUser();
        this.testDeleteUser();
    }

    private void testDeleteUser() {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendDeleteRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    private ResponseEntity<String> sendDeleteRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.DELETE, request, String.class);
    }

    private void testGetAuthenticatedUser() throws IOException {
        HttpEntity<String> request = this.buildRequest(new JSONObject());
        ResponseEntity<String> response = this.sendGetAuthenticatedUserRequest(request);
        this.testAuthenticatedUserResponseResults(response);
    }

    private void testAuthenticatedUserResponseResults(ResponseEntity<String> response) throws IOException {
        responseRoot = objectMapper.readTree(response.getBody());
        Assertions.assertNotNull(responseRoot);
        Assertions.assertEquals("testUser", responseRoot.path("name").asText());
    }

    private ResponseEntity<String> sendGetAuthenticatedUserRequest(HttpEntity<String> request) {
        return restTemplate.exchange(baseUrl + "users/self", HttpMethod.GET, request, String.class);
    }

    private void testLoginSuccess() throws JSONException, IOException {
        JSONObject loginCredentials = this.buildLoginCredentials();
        HttpEntity<String> request = this.buildRequest(loginCredentials);
        ResponseEntity<String> response = this.sendLoginRequest(request);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        authToken = response.getHeaders().toSingleValueMap().get("Authorization");
        Assertions.assertNotNull(authToken);
    }

    private JSONObject buildLoginCredentials() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("email", "test@test.com");
        credentials.put("password", "testPassword");
        return credentials;
    }

    private void testLoginFailsWithIncorrectCredentials() throws JSONException {
        JSONObject incorrectLoginCredentials = this.buildIncorrectLoginCredentials();
        HttpEntity<String> request = this.buildRequest(incorrectLoginCredentials);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendLoginRequest(request));
        Assertions.assertEquals(403, exception.getRawStatusCode());
    }

    private ResponseEntity<String> sendLoginRequest(HttpEntity<String> request) throws IOException {
        return restTemplate.exchange(baseUrl + "login", HttpMethod.POST,
                request, String.class);
    }

    private JSONObject buildIncorrectLoginCredentials() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("email", "test@test.com");
        credentials.put("password", "wrong_password");
        return credentials;
    }

    private void testUserRegister() throws JSONException, IOException {
        JSONObject userJsonObject = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        this.sendRegisterRequest(request);
        this.testRegisterResponseContainsCorrectData();
    }


    private void testDuplicateUser() throws JSONException, IOException {
        JSONObject userJsonObject = this.buildUserJsonObject();
        HttpEntity<String> request = this.buildRequest(userJsonObject);
        HttpClientErrorException exception =
                Assertions.assertThrows(HttpClientErrorException.class, () -> this.sendRegisterRequest(request));
        Assertions.assertEquals(409, exception.getRawStatusCode());
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
        responseRoot = objectMapper.readTree(userResultsAsJsonString);
    }

    private void testRegisterResponseContainsCorrectData() {
        Assertions.assertNotNull(responseRoot);
        Assertions.assertEquals("testUser", responseRoot.path("name").asText());
        Assertions.assertEquals("test@test.com", responseRoot.path("email").asText());
        Assertions.assertEquals("", responseRoot.path("password").asText());
        Assertions.assertNotNull(responseRoot.path("id"));
    }
}

