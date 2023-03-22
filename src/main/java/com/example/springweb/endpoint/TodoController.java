package com.example.springweb.endpoint;

import com.example.springweb.model.Todo;
import com.example.springweb.service.RestTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("todos")
@RequiredArgsConstructor
public class TodoController {

    private final RestTemplateService todoService;

    @GetMapping
    public ResponseEntity<List<Todo>> getTodos() {
        return ResponseEntity.ok(todoService.retrieveAllTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<Todo> getTodo(
            @PathVariable("id") int id
    ) {
        return ResponseEntity.ok(todoService.retrieveTodo(id));
    }

    @PostMapping
    public ResponseEntity<Todo> postTodo(
            @RequestBody Todo todo
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todo));
    }

    @PutMapping("{id}")
    public ResponseEntity<Todo> updateTodo(
            @RequestBody Todo todo
    ) {
        return ResponseEntity.ok(todoService.updateTodo(todo));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTodo() {
        return ResponseEntity.ok().build();
    }
}
