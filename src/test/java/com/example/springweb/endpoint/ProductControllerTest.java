package com.example.springweb.endpoint;

import com.example.springweb.SpringWebApplication;
import com.example.springweb.model.Product;
import com.example.springweb.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = SpringWebApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllProducts() throws Exception {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("maximalPrice", "350");
        queryParams.add("minimalPrice", "200");

        List<Product> expectedProducts = ProductService.products.stream()
                .filter(product -> product.price() >= 200 && product.price() <= 350)
                .toList();

        mvc.perform(MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .queryParams(queryParams))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(toJson(expectedProducts), JSONCompareMode.STRICT.hasStrictOrder()));
    }

    @Test
    void getProduct() throws Exception {
        var firstProduct = ProductService.products.get(0);

        mvc.perform(MockMvcRequestBuilders.get("/products/{id}", firstProduct.id())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(toJson(firstProduct), JSONCompareMode.STRICT.hasStrictOrder()));
    }

    @Test
    void postProduct() throws Exception {
        var product = Product.builder()
                .name("PC Gaming")
                .description("For the hardcore gamers")
                .createdAt(LocalDateTime.now())
                .price(3200)
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/products")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(toJson(product))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header()
                        .exists("Location"));
    }

    @Test
    void putProduct() throws Exception {
        var productIdToUpdate = ProductService.products.get(0).id();
        var newProduct = Product.builder()
                .name("PC Gaming")
                .description("For the hardcore gamers")
                .createdAt(LocalDateTime.now())
                .price(3200)
                .build();
        var newProductPersisted = newProduct.id(productIdToUpdate);

        mvc.perform(MockMvcRequestBuilders.put("/products/{id}", productIdToUpdate)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(newProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(toJson(newProductPersisted), JSONCompareMode.STRICT.hasStrictOrder()));
    }

    @Test
    void patchProduct() throws Exception {
        var newPrice = "2900.0";
        var productToUpdateId = ProductService.products.get(0).id();

        mvc.perform(MockMvcRequestBuilders.patch("/products/{id}", productToUpdateId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .queryParam("price", newPrice))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(newPrice));
    }

    @Test
    void deleteProduct() throws Exception {
        var productIdToDelete = ProductService.products.get(0).id();

        mvc.perform(MockMvcRequestBuilders.delete("/products/{id}", productIdToDelete)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
