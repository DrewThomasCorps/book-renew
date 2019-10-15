package com.bookrenew.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTests {

    private RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void stage1_shouldCreateUser() throws JSONException, IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "testUser");
        userJsonObject.put("email", "test@test.com");
        userJsonObject.put("username", "testUsername");
        userJsonObject.put("password", "testPassword");

        HttpEntity<String> request = new HttpEntity<String>(userJsonObject.toString(), headers);

        String userResultsAsJsonString = restTemplate.postForObject("http://localhost:8080/users", request, String.class);
        JsonNode root = objectMapper.readTree(userResultsAsJsonString);

        Assert.assertNotNull(root);
        Assert.assertNotNull(root.path("name").asText());
    }



}
