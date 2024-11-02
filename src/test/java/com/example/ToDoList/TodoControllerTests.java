package com.example.ToDoList;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.AuthController;
import controller.TodoController;
import model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
@SpringJUnitConfig
@AutoConfigureMockMvc
public class TodoControllerTests {

    //create todo

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    //reterive todo
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    public void testGetTodos() throws Exception {
        mockMvc.perform(get("/api/todos?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isLessThanOrEqualTo(10));
    }

    //update
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

    //delete
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", 1))
                .andExpect(status().isNoContent());
    }



}
