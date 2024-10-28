package com.example.todo.service;

import com.example.todo.entity.TodoEntity;
import com.example.todo.model.TodoDto;

import java.util.List;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * The TodoService interface defines the contract for managing
 * to-do items within the TodoMVC application
 */


public interface TodoService {
    TodoDto createTodo(TodoDto todo);

    List<TodoDto> getAllTodo();

    TodoDto getToDoById(Long todoId);

    void deleteTodoById(Long todoId);

    TodoDto updateTodoById(Long todoId, TodoDto todo);

    List<TodoDto> getTodoByPriority(String toDoPriority);
}
