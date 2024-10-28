package com.example.todo.service;

import com.example.todo.entity.Todo;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * The TodoServiceImpl class is the concrete implementation of the TodoService interface
 */


@Service
public class TodoServiceImpl implements TodoService{

    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Autowired
    private TodoRepository todoRepository;

    /**
     * createTodo - Creates a new to-do item.
     *
     * @param todo - The to-do object to be created.
     * @return - The created To-do object.
     */
    @Override
    public Todo createTodo(Todo todo) {
        logger.info("Creating new Todo: {}", todo);
        Todo savedTodo = todoRepository.save(todo);
        logger.info("Todo created with ID: {}", savedTodo.getId());
        return savedTodo;
    }

    /**
     * getAllTodo - Retrieves all to-do
     *
     * @return - A list of To-do objects
     */
    @Override
    public List<Todo> getAllTodo() {
        logger.info("Fetching all Todos");
        List<Todo> todos = todoRepository.findAll();

        if(todos.isEmpty()){
            logger.error("No Todos found");
            throw new TodoNotFoundException("No Todo items found");
        }

        logger.info("Total Todos fetched: {}", todos.size());
        return todos;
    }

    /**
     * $getToDoById - Get To-Do object form id.
     *
     * @param id - to-do id.
     * @return - Returns to-do object.
     */
    @Override
    public Todo getToDoById(Long id) {
        logger.info("Fetching Todo with ID: {}", id);
        return todoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Todo with ID: {} not found", id);
                    return new TodoNotFoundException("Todo task not found with ID: " + id);
                });
    }

    /**
     * deleteTodoById - Delete To-Do object form id.
     *
     * @param todoId - to-do id.
     */
    @Override
    public void deleteTodoById(Long todoId) {
        logger.info("Deleting Todo with ID: {}", todoId);
        boolean exists =  todoRepository.existsById(todoId);
        if(!exists){
            logger.error("Todo with ID: {} not found, deletion failed", todoId);
            throw new TodoNotFoundException("Todo task not found with ID: " + todoId);
        }
        todoRepository.deleteById(todoId);
        logger.info("Todo with ID: {} deleted", todoId);
    }

    /**
     * updateTodoById - Update To-Do object form id.
     *
     * @param todoId - to-do id.
     * @param todoDetails - to-do object which needs to be updated
     * @return - update to-do object.
     */
    @Override
    public Todo updateTodoById(Long todoId, Todo todoDetails) {
        logger.info("Updating Todo with ID: {}", todoId);
        Optional<Todo> existingTodoOptional = todoRepository.findById(todoId);

        if (existingTodoOptional.isPresent()) {
            Todo existingTodo = existingTodoOptional.get();

            // Updated fields only if the value is present
            existingTodo.setTitle(todoDetails.getTitle() != null ? todoDetails.getTitle() : existingTodo.getTitle());
            existingTodo.setDescription(todoDetails.getDescription() != null ? todoDetails.getDescription() : existingTodo.getDescription());
            existingTodo.setPriority(todoDetails.getPriority() != null ? todoDetails.getPriority() : existingTodo.getPriority());
            existingTodo.setStatus(todoDetails.getStatus() != null ? todoDetails.getStatus() : existingTodo.getStatus());
            existingTodo.setDueDate(todoDetails.getDueDate() != null ? todoDetails.getDueDate() : existingTodo.getDueDate());
            existingTodo.setCreatedAt(existingTodo.getCreatedAt());
            existingTodo.setUpdatedAt(getCurrentDateAndTime());

            Todo updatedTodo = todoRepository.save(existingTodo);
            logger.info("Todo with ID: {} updated successfully", todoId);
            return updatedTodo;
        } else {
            logger.error("Todo with ID: {} not found for update", todoId);
            throw new TodoNotFoundException("Todo task not found with ID: " + todoId);
        }
    }

    /**
     * getTodoByPriority - get To-Do object by priority.
     *
     * @param toDoPriority - Priority - High, Medium
     * @return - get to-do object.
     */
    @Override
    public List<Todo> getTodoByPriority(String toDoPriority) {
        logger.info("Fetching Todos by priority: {}", toDoPriority);
        List<Todo> todoListByPriority = todoRepository.findByPriority(toDoPriority);
        if(todoListByPriority.isEmpty()){
            logger.error("No Todos found with priority: {}", toDoPriority);
            throw new TodoNotFoundException("No Todos found with priority: " + toDoPriority);
        }
        logger.info("Total Todos fetched with priority {}: {}", toDoPriority, todoListByPriority.size());
        return todoListByPriority;
    }

    /**
     * getCurrentDateAndTime - Retrieves the current date and time.
     *
     * @return - The current date and time as a LocalDateTime object.
     */
    private LocalDateTime getCurrentDateAndTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(currentDateTime.format(formatter));
    }

}
