package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStoryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGenerateSql_createTable() {
        String userStory = "Create table employee with columns id, name, age, salary";
        String url = "http://localhost:" + port + "/api/generate-sql";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("userStory", userStory), headers);
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate
                .postForEntity(url, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        String sql = (String) response.getBody().get("sqlScript");
        assertThat(sql).containsIgnoringCase("employee");
        assertThat(sql).containsIgnoringCase("create");
    }

    @Test
    void testGenerateSql_addColumn() {
        String userStory = "Add column department to employee";
        String url = "http://localhost:" + port + "/api/generate-sql";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("userStory", userStory), headers);
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate
                .postForEntity(url, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        String sql = (String) response.getBody().get("sqlScript");
        assertThat(sql).containsIgnoringCase("employee");
        assertThat(sql).containsIgnoringCase("alter");
    }

    @Test
    void testGenerateSql_update() {
        String userStory = "Update employee set salary = 6000 where id = 1";
        String url = "http://localhost:" + port + "/api/generate-sql";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("userStory", userStory), headers);
        @SuppressWarnings("unchecked")
        ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate
                .postForEntity(url, request, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        String sql = (String) response.getBody().get("sqlScript");
        assertThat(sql).containsIgnoringCase("update");
        assertThat(sql).containsIgnoringCase("salary");
    }
}
