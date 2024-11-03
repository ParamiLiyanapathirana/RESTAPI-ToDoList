package com.example.ToDoList;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.TodoController;
import model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@SpringJUnitConfig
@AutoConfigureMockMvc
public class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Create todo
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    public void testCreateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Sample Todo");
        todo.setDescription("Sample Description");

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Todo"));
    }

    // Retrieve todo
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    public void testGetTodos() throws Exception {
        mockMvc.perform(get("/api/todos?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(10))); // Use the value() method here
    }



    // Update todo
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    public void testUpdateTodo() throws Exception {
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Todo Title");
        updatedTodo.setDescription("Updated Description");

        mockMvc.perform(put("/api/todos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Todo Title"));
    }

    // Delete todo
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
