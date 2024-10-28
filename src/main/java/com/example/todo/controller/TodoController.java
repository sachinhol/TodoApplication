package com.example.todo.controller;

import com.example.todo.entity.TodoEntity;
import com.example.todo.model.TodoDto;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
 * Author: Sachin
 * Date: 27-Oct-24
 * The TodoController class is a RESTful API controller that manages CRUD and other
 *  operations for to-do items in a TodoMVC application.
 */

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    //Create a new to-do task
    @PostMapping("/create")
    public ResponseEntity<TodoDto> createTodo(@Valid @RequestBody TodoDto todo){
        TodoDto createTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createTodo, HttpStatus.CREATED);
    }

    //Get all to-do tasks
    @GetMapping("/getall")
    public ResponseEntity<List<TodoDto>> getAllTodos(){
        List<TodoDto> todosList = todoService.getAllTodo();
        return new ResponseEntity<>(todosList,HttpStatus.OK);
    }

    //Get a particular to-da task by id
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoByID(@PathVariable("id") Long todoId){
        TodoDto todoById  = todoService.getToDoById(todoId);
        return new ResponseEntity<>(todoById, HttpStatus.FOUND);
    }

    //Delete a particular to-do task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteToById(@PathVariable("id") Long todoId){
        todoService.deleteTodoById(todoId);
        return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }

    //Update a particular to-do task
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodoById(@PathVariable("id") Long todoId, @RequestBody TodoDto todo){
         TodoDto updatedTodo= todoService.updateTodoById(todoId,todo);
         return new ResponseEntity<>(updatedTodo,HttpStatus.OK);
    }

    //Find To do task by query param
    @GetMapping("/find")
    public ResponseEntity<List <TodoDto>> findToDoByPriority(@RequestParam("priority") String toDoPriority){
        logger.info("To do list found to be by Priority : {} ",toDoPriority);
        List<TodoDto> priorityTodoList = todoService.getTodoByPriority(toDoPriority);
        return new ResponseEntity<>(priorityTodoList, HttpStatus.FOUND);
    }
}
