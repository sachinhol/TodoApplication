package com.example.todo.service;

import com.example.todo.entity.Todo;

import java.util.List;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * The TodoService interface defines the contract for managing
 * to-do items within the TodoMVC application
 */


public interface TodoService {
    Todo createTodo(Todo todo);

    List<Todo> getAllTodo();

    Todo getToDoById(Long todoId);

    void deleteTodoById(Long todoId);

    Todo updateTodoById(Long todoId, com.example.todo.entity.Todo todo);

    List<Todo> getTodoByPriority(String toDoPriority);
}
