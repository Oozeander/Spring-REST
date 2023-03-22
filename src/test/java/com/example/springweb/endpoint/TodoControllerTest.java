package com.example.springweb.endpoint;

import com.example.springweb.SpringWebApplication;
import com.example.springweb.model.Todo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(classes = SpringWebApplication.class)
@AutoConfigureMockMvc
public class TodoControllerTest {

    private static ClientAndServer mockServer;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    static void setUp(
            @Value("${api.json-placeholder.base-url}") String baseUrl
    ) {
        mockServer = startClientAndServer(
                Integer.parseInt(baseUrl.split(":")[2])
        );
    }

    @AfterAll
    static void destroy() {
        mockServer.stop();
    }

    @Test
    void getTodo() throws Exception {
        // given
        var todo = new Todo(100, 10, "ça marche!", true);
        mockServer.when(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/todos/10")
        ).respond(
                HttpResponse.response()
                        .withBody(toJson(todo))
                        .withContentType(MediaType.JSON_UTF_8)
                        .withDelay(TimeUnit.MILLISECONDS, 220)
        );

        // when
        mvc.perform(
                MockMvcRequestBuilders.get("/todos/{id}", 10)
                        .characterEncoding("UTF-8")
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().json(toJson(todo), JSONCompareMode.STRICT.hasStrictOrder())
        );

        //then
        mockServer.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/todos/10"),
                VerificationTimes.once()
        );
    }

    private String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
