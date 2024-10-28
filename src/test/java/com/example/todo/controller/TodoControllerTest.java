package com.example.todo.controller;

import com.example.todo.entity.TodoEntity;
import com.example.todo.model.TodoDto;
import com.example.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @InjectMocks
    TodoController todoController;

    @Mock
    TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    }

    @Test
    void testCreateTodo_Valid() {
        TodoDto todo = new TodoDto();
        todo.setId(1L);
        todo.setTitle("Drive to Airport");
        todo.setPriority("High");
        todo.setStatus("Pending");
        todo.setDueDate(LocalDate.now());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());

        when(todoService.createTodo(any(TodoDto.class))).thenReturn(todo);

        ResponseEntity<TodoDto> response = todoController.createTodo(todo);
        assertEquals("Drive to Airport", response.getBody().getTitle());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(todoService, times(1)).createTodo(todo);
    }

    @Test
    public void testGetAllTodos() {
        List<TodoDto> todoList = new ArrayList<>();
        TodoDto todo1 = new TodoDto();
        todo1.setId(1L);
        todo1.setTitle("Drive to Airport");
        todo1.setPriority("High");
        todo1.setStatus("Pending");
        todo1.setDueDate(LocalDate.now());
        todo1.setCreatedAt(LocalDateTime.now());
        todo1.setUpdatedAt(LocalDateTime.now());

        TodoDto todo2 = new TodoDto();
        todo2.setId(2L);
        todo2.setTitle("Go to swimming");
        todo2.setPriority("Medium");
        todo2.setStatus("Done");
        todo2.setDueDate(LocalDate.now());
        todo2.setCreatedAt(LocalDateTime.now());
        todo2.setUpdatedAt(LocalDateTime.now());

        todoList.add(todo1);
        todoList.add(todo2);

        when(todoService.getAllTodo()).thenReturn(todoList);

        ResponseEntity<List<TodoDto>> response = todoController.getAllTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(todoService, times(1)).getAllTodo();
    }

    @Test
    public void testGetTodoById() {
        TodoDto todo = new TodoDto();
        todo.setId(1L);
        todo.setTitle("Drive to Airport");
        todo.setPriority("High");
        todo.setStatus("Pending");
        todo.setDueDate(LocalDate.now());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());

        when(todoService.getToDoById(1L)).thenReturn(todo);

        ResponseEntity<TodoDto> response = todoController.getTodoByID(1L);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("Drive to Airport", response.getBody().getTitle());
        verify(todoService, times(1)).getToDoById(1L);
    }

    @Test
    public void testDeleteTodoById() {
        doNothing().when(todoService).deleteTodoById(1L);

        ResponseEntity<String> response = todoController.deleteToById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody());
        verify(todoService, times(1)).deleteTodoById(1L);
    }

    @Test
    public void testUpdateTodoById() {
        TodoDto todo = new TodoDto();
        todo.setId(1L);
        todo.setTitle("Updated Todo");
        todo.setPriority("High");
        todo.setStatus("Pending");
        todo.setDueDate(LocalDate.now());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());

        when(todoService.updateTodoById(any(Long.class), any(TodoDto.class))).thenReturn(todo);

        ResponseEntity<TodoDto> response = todoController.updateTodoById(1L, todo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Todo", response.getBody().getTitle());
        verify(todoService, times(1)).updateTodoById(1L, todo);
    }

    @Test
    public void testFindToDoByPriority() {
        List<TodoDto> todoList = new ArrayList<>();
        TodoDto todo = new TodoDto();
        todo.setId(1L);
        todo.setTitle("Drive to Airport");
        todo.setPriority("High");
        todo.setStatus("Pending");
        todo.setDueDate(LocalDate.now());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        todoList.add(todo);

        when(todoService.getTodoByPriority("High")).thenReturn(todoList);

        ResponseEntity<List<TodoDto>> response = todoController.findToDoByPriority("High");

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Drive to Airport", response.getBody().get(0).getTitle());
        verify(todoService, times(1)).getTodoByPriority("High");
    }
}