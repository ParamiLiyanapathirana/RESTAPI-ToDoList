package controller;

import lombok.extern.slf4j.Slf4j;
import model.Todo;
import model.User;

import exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/search")
    public ResponseEntity<Page<Todo>> searchTodos(
            Authentication authentication,
            @RequestParam String keyword,
            Pageable pageable) {

        Long userId = userRepository.findByEmail(authentication.getName())
                .orElseThrow()
                .getId();

        Page<Todo> todos = todoRepository.searchByUserIdAndKeyword(userId, keyword, pageable);

        if (todos.isEmpty()) {
            throw new ResourceNotFoundException("No Todo items found matching keyword: " + keyword);
        }

        return ResponseEntity.ok(todos);
    }

    //sorting
    @GetMapping
    public ResponseEntity<Page<Todo>> getTodos(
            Authentication authentication,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        Long userId = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();

        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<Todo> todos = todoRepository.findByUserId(userId, (Pageable) sort);

        return ResponseEntity.ok(todos);
    }


}
