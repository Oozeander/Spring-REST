package com.example.springweb.service;

import com.example.springweb.model.Todo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateService(
            @Value("${api.json-placeholder.base-url}") String baseUrl
    ) {
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .build();
    }

    public List<Todo> retrieveAllTodos() {
        return restTemplate.exchange("/todos", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Todo>>() {
                }).getBody();
    }

    public Todo retrieveTodo(int id) {
        return restTemplate.exchange("/todos/{id}", HttpMethod.GET, null,
                Todo.class, id).getBody();
    }

    public Todo createTodo(Todo todo) {
        return restTemplate.exchange("/todos", HttpMethod.POST, new HttpEntity<>(todo),
                Todo.class).getBody();
    }

    public Todo updateTodo(Todo todo) {
        return restTemplate.exchange("/todos/{id}", HttpMethod.PUT, new HttpEntity<>(todo),
                Todo.class, todo.id()).getBody();
    }

    public void deleteTodo(int id) {
        restTemplate.exchange("/todos/{id}", HttpMethod.DELETE, null,
                Void.class, id);
    }
}
