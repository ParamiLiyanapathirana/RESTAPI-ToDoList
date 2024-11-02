package controller;

import lombok.extern.slf4j.Slf4j;
import model.Todo;
import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import repository.TodoRepository;
import repository.UserRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/todos")
@Slf4j
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        todo.setUser(user);
        return ResponseEntity.ok(todoRepository.save(todo));
    }



    @GetMapping
    public ResponseEntity<Page<Todo>> getTodos(Authentication authentication, Pageable pageable) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        Page<Todo> todosPage = todoRepository.findByUserId(user.getId(), pageable);
        return ResponseEntity.ok(todosPage);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails, Authentication authentication) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        if (!todo.getUser().getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.isCompleted());
        todo.setDueDate(todoDetails.getDueDate());
        return ResponseEntity.ok(todoRepository.save(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, Authentication authentication) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        if (!todo.getUser().getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        todoRepository.delete(todo);
        return ResponseEntity.noContent().build();
    }



}
