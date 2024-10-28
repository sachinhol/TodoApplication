package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoServiceImplTest {

    @InjectMocks
    TodoServiceImpl todoService;

    @Mock
    TodoRepository todoRepository;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo(1L,"Drive to airport","Pickup firends","Pending","High", LocalDate.now(), LocalDateTime.now(),LocalDateTime.now());
    }

    @Test
    void testCreateTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo newTodo = todoService.createTodo(todo);

        assertNotNull(newTodo);
        assertEquals("Drive to airport",newTodo.getTitle());
    }

    @Test
    void testGetAllTodo_TodoPresent() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo));

        List todoList =todoService.getAllTodo();

        assertEquals(1,todoList.size());
        verify(todoRepository,times(1)).findAll();
    }

    @Test
    void testGetAllTodo_TodoNotPresent() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList());

        Exception exception = assertThrows(TodoNotFoundException.class, ()->{
           todoService.getAllTodo();
        });

        String errorMSg = "No Todo items found";
        String actualMsg =exception.getMessage();

        assertEquals(actualMsg,errorMSg);
    }

    @Test
    void testGetToDoById_TodoPresent() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todo));

        Todo fetchedTodo =   todoService.getToDoById(anyLong());

        assertEquals("Drive to airport",fetchedTodo.getTitle());
    }

    @Test
    void testGetToDoById_TodoNotPresent() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TodoNotFoundException.class, ()->{
            todoService.getToDoById(1L);
        });

        String errorMsg = "Todo task not found with ID: 1";
        String actualMsg = exception.getMessage();

        assertEquals(errorMsg,actualMsg);
    }

    @Test
    void testDeleteTodoById_TodoPresent() {
        when(todoRepository.existsById(1L)).thenReturn(true);

        todoService.deleteTodoById(1L);

        verify(todoRepository, times(1)).deleteById(1L);
    }


    @Test
    void testDeleteTodoById_TodoNotPresent() {
        when(todoRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(TodoNotFoundException.class, ()->{
            todoService.deleteTodoById(1L);
        });

        String expectedMsg = "Todo task not found with ID: 1";
        String acutalMsg = exception.getMessage();

        assertEquals(expectedMsg,acutalMsg);
    }

    @Test
    void testUpdateTodoById_TodoPresent() {
        Todo updatedTodo = new Todo(1L,"Book movie tickets", "Any movie", "Pending", "high", null,LocalDateTime.now(), LocalDateTime.now());
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        Todo result = todoService.updateTodoById(1L, updatedTodo);

        assertNotNull(result);
        assertEquals("high", result.getPriority());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void testUpdateTodoById_TodoNotPresent() {
        Todo updatedTodo = new Todo(1L,"Book movie tickets", "Any movie", "Pending", "high", null,LocalDateTime.now(), LocalDateTime.now());
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        Exception exception = assertThrows(TodoNotFoundException.class,()->{
            todoService.updateTodoById(1L, updatedTodo);
        });


        String expectedMsg = "Todo task not found with ID: 1";
        String acutalMsg = exception.getMessage();

        assertEquals(expectedMsg,acutalMsg);
    }

    @Test
    void testGetTodoByPriority_TodoPresent() {
        Todo todo1 = new Todo(1L,"Drive to airport","Pickup firends","Pending","High", LocalDate.now(), LocalDateTime.now(),LocalDateTime.now());
        Todo todo2 = new Todo(2L,"Book movie tickets", "Any movie", "Pending", "High", null,LocalDateTime.now(), LocalDateTime.now());
        Todo todo3 = new Todo(3L,"Any Task", "Any Description", "Completed", "Low", null,LocalDateTime.now(), LocalDateTime.now());
        List todoList = new ArrayList();
        todoList.add(todo1);
        todoList.add(todo2);
        todoList.add(todo3);

        when(todoRepository.findByPriority("High")).thenReturn(List.of(todo1,todo2));

        List todoListWithHighPriority = todoService.getTodoByPriority("High");

        assertEquals(2,todoListWithHighPriority.size());
    }

    @Test
    void testGetTodoByPriority_TodoNotPresent() {
        Todo todo1 = new Todo(1L,"Drive to airport","Pickup firends","Pending","High", LocalDate.now(), LocalDateTime.now(),LocalDateTime.now());
        Todo todo2 = new Todo(2L,"Book movie tickets", "Any movie", "Pending", "High", null,LocalDateTime.now(), LocalDateTime.now());
        Todo todo3 = new Todo(3L,"Any Task", "Any Description", "Completed", "Low", null,LocalDateTime.now(), LocalDateTime.now());
        List todoList = new ArrayList();
        todoList.add(todo1);
        todoList.add(todo2);
        todoList.add(todo3);

        when(todoRepository.findByPriority("High")).thenReturn(List.of());

        Exception exception = assertThrows(TodoNotFoundException.class,()->{
            todoService.getTodoByPriority("High");
        });


        String expectedMsg = "No Todos found with priority: High";
        String acutalMsg = exception.getMessage();

        assertEquals(expectedMsg,acutalMsg);
    }
}