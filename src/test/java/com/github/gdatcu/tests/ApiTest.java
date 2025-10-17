package com.github.numele_tau_de_github.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.io.InputStream;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTest {
    private String baseUrl;

    @BeforeClass
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // Încarcă fișierul ca o resursă din classpath (din src/test/resources)
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("app.yaml");
        if (inputStream == null) {
            throw new IOException("Nu s-a putut găsi fișierul de configurare 'app.yaml' în src/test/resources");
        }
        JsonNode root = mapper.readTree(inputStream);
        this.baseUrl = root.path("service").path("baseUrl").asText();
    }

    @Test(description = "Verifică preluarea unui 'todo' item și validarea titlului")
    public void verifyTodoItemRetrieval() {
        // -- ARRANGE --
        String endpoint = "/todos/1";
        String fullUrl = this.baseUrl + endpoint;
        System.out.println("Testing endpoint: " + fullUrl);

        // -- ACT & ASSERT --
        given()
        .when()
            .get(fullUrl)
        .then()
            .assertThat()
            .statusCode(200)
            .body("title", not(emptyOrNullString()))
            .body("id", equalTo(1));
            
        System.out.println("Test API a trecut cu succes!");
    }
}