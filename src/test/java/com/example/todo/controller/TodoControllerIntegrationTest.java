package com.example.todo.controller;/*
 * Author: Sachin
 * Date: 28-Oct-24
 * Junit integration test for todocontroller
 */

import com.example.todo.entity.TodoEntity;
import com.example.todo.model.TodoDto;
import com.example.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerIntegrationTest {

    @MockBean
    TodoService todoService;

    @Autowired
    MockMvc mockMvc;

    private TodoDto todo;

    @BeforeEach
    public void setUp(){
        todo = new TodoDto();
        todo.setTitle("Drive to Airport");
        todo.setDescription("Pick up friends");
        todo.setPriority("medium");
        todo.setStatus("Pending");
        todo.setDueDate(LocalDate.now().plusDays(5));
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testCreateTodo_Success() throws Exception{
        when(todoService.createTodo(any(TodoDto.class))).thenReturn(todo);

        String toDoJson = """
                    {
                    "title": "Drive to Airport",
                    "description": "Pick up friends",
                    "status": "Pending",
                    "priority": "medium",
                    "dueDate": "2024-10-27",
                    "createdAt": "2024-10-21T09:15:00Z",
                    "updatedAt": "2024-10-21T09:15:00Z"
                    }""";

        mockMvc.perform(post("/api/todos/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toDoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Drive to Airport"))
                .andExpect(jsonPath("$.priority").value("medium"));

    }

    @Test
    public void createTodo_Fail() throws Exception{
        String toDoJson = """
                {
                    "id": 1,
                    "title": "",
                    "description": "Pick up friends",
                    "status": "Pending",
                    "priority": "medium",
                    "dueDate": "2024-10-27",
                    "createdAt": "2024-10-21T09:15:00Z",
                    "updatedAt": "2024-10-21T09:15:00Z"
                }""";

        mockMvc.perform(post("/api/todos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toDoJson))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Please add Title"));
    }

    @Test
    public void testGetAllTodos_Success() throws Exception{
        TodoEntity todo2 = new TodoEntity();
        todo2.setTitle("Book movie tickets");
        todo2.setDescription("Any movie");
        todo2.setPriority("High");
        todo2.setStatus("Done");
        todo2.setDueDate(LocalDate.now());
        todo2.setCreatedAt(LocalDateTime.now());
        todo2.setUpdatedAt(LocalDateTime.now());

        List todoList =  new ArrayList();
        todoList.add(todo);
        todoList.add(todo2);

        when(todoService.getAllTodo()).thenReturn(todoList);

        mockMvc.perform(get("/api/todos/getall")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title",containsInAnyOrder("Drive to Airport","Book movie tickets")));
    }


    @Test
    public void getTodoByIdTest_Success() throws Exception {
        when(todoService.getToDoById(1L)).thenReturn(todo);

        String toDoJson = """
                    {
                    "title": "Drive to Airport",
                    "description": "Pick up friends",
                    "status": "Pending",
                    "priority": "medium",
                    "dueDate": "2024-10-27",
                    "createdAt": "2024-10-21T09:15:00Z",
                    "updatedAt": "2024-10-21T09:15:00Z"
                    }""";

        mockMvc.perform(get("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toDoJson))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.title").value("Drive to Airport"));
    }

    @Test
    public void testDeleteById_Success() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));
    }

    @Test
    public void testUpdateTodoById_Success() throws Exception {
        TodoDto updatedTodo = new TodoDto();
        updatedTodo.setId(1L);
        updatedTodo.setTitle("Drive to Airport");
        updatedTodo.setDescription("Pick up friends and drop to home");
        updatedTodo.setStatus("Pending");
        updatedTodo.setPriority("High");
        updatedTodo.setDueDate(LocalDate.now().plusDays(5));
        updatedTodo.setCreatedAt(LocalDateTime.now());
        updatedTodo.setUpdatedAt(LocalDateTime.now());


        when(todoService.updateTodoById(anyLong(),any(TodoDto.class))).thenReturn(updatedTodo);

        String updatedToDoJson = """
                    {
                    "title": "Drive to Airport",
                    "description": "Pick up friends and drop to home",
                    "status": "Pending",
                    "priority": "High",
                    "dueDate": "2024-10-27",
                    "createdAt": "2024-10-21T09:15:00Z",
                    "updatedAt": "2024-10-21T09:15:00Z"
                    }""";

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedToDoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("High"));

    }

    @Test
    public  void testFindToDoByPriority_Success() throws Exception {
        TodoEntity todo2 = new TodoEntity();
        todo2.setTitle("Book movie tickets");
        todo2.setDescription("Any movie");
        todo2.setPriority("High");
        todo2.setStatus("Done");
        todo2.setDueDate(LocalDate.now());
        todo2.setCreatedAt(LocalDateTime.now());
        todo2.setUpdatedAt(LocalDateTime.now());

        List todoList =  new ArrayList();
        todoList.add(todo);
        todoList.add(todo2);
        when(todoService.getTodoByPriority(anyString())).thenReturn(todoList);

        mockMvc.perform(get("/api/todos/find")
                .contentType(MediaType.APPLICATION_JSON)
                .param("priority","High"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$",hasSize(2)));
    }
}