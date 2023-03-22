package com.example.springweb.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class MultipartControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void should_get_file() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/files/{filename}", "Capture2.png")
                .characterEncoding("UTF-8")
        ).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.IMAGE_PNG)
        );
    }

    @Test
    void should_get_all_file_names() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/files")
                .characterEncoding("UTF-8"))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.file_names[0]", is("Capture2.PNG"))
                );
    }

    @Test
    void should_post_document() throws Exception {
        var sampleFile = new MockMultipartFile(
                "file", // @RequestParam attribute name
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is the file content".getBytes()
        );
        var filePath = Paths.get("uploads", sampleFile.getOriginalFilename());

        mvc.perform(MockMvcRequestBuilders.multipart("/files").file(sampleFile))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        assertThat(Files.exists(filePath)).isTrue();
        Files.delete(filePath);
    }
}
