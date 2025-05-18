package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class UserStoryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${openai.api.key:}")
    private String openaiApiKey;

    private String callTogetherAiForSql(String userStory, String dbSchema) {
        String togetherApiKey = openaiApiKey; // Reuse the same property for simplicity
        String prompt = "Given the following user story: '" + userStory + "' and the database schema: \n" + dbSchema
                + "\nIMPORTANT: Whenever a row is added to any of the tables RT_Utility_MV, RT_Asset_MV, RT_Service_MV, or RT_Config_MV, also generate an appropriate INSERT statement for RT_All_MV referencing the new row and the latest rows from the other tables.\nGenerate the SQL script needed to fulfill the user story.";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + togetherApiKey);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("model", "mistralai/Mixtral-8x7B-Instruct-v0.1"); // You can change model if needed
            bodyMap.put("messages", List.of(message));
            String body = mapper.writeValueAsString(bodyMap);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<HashMap> response = restTemplate.exchange(
                    "https://api.together.xyz/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    HashMap.class);
            HashMap<String, Object> respBody = response.getBody();
            if (respBody != null && respBody.containsKey("choices")) {
                List<?> choices = (List<?>) respBody.get("choices");
                if (!choices.isEmpty()) {
                    HashMap<?, ?> choice = (HashMap<?, ?>) choices.get(0);
                    HashMap<?, ?> messageResp = (HashMap<?, ?>) choice.get("message");
                    return messageResp.get("content").toString();
                }
            }
        } catch (Exception e) {
            return "-- Error calling Together AI: " + e.getMessage();
        }
        return "-- No SQL generated.";
    }

    @PostMapping(value = "/generate-sql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> generateSql(@RequestBody Map<String, String> request) {
        String userStory = request.getOrDefault("userStory", "");
        String dbSchema = getDatabaseSchema();
        String sqlScript;
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            sqlScript = callTogetherAiForSql(userStory, dbSchema);
        } else {
            sqlScript = "-- Please provide a Together AI API key in application.properties.";
        }
        return ResponseEntity.ok(Map.of("sqlScript", sqlScript));
    }

    private String getDatabaseSchema() {
        StringBuilder sb = new StringBuilder();
        List<Map<String, Object>> tables = jdbcTemplate.queryForList("SHOW TABLES");
        for (Map<String, Object> table : tables) {
            String tableName = table.values().iterator().next().toString();
            sb.append("Table: ").append(tableName).append("\n");
            List<Map<String, Object>> columns = jdbcTemplate.queryForList("SHOW COLUMNS FROM " + tableName);
            for (Map<String, Object> col : columns) {
                sb.append("  ").append(col.get("FIELD")).append(" ").append(col.get("TYPE")).append("\n");
            }
        }
        return sb.toString();
    }
}
