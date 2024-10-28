package com.example.todo.service;

import com.example.todo.entity.TodoEntity;
import com.example.todo.exceptions.TodoNotFoundException;
import com.example.todo.model.TodoDto;
import com.example.todo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public TodoDto createTodo(TodoDto todo) {
        logger.info("Creating new Todo: {}", todo);

        TodoEntity todoEntity =  TodoEntity.builder()
                .title(todo.getTitle())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();

        TodoEntity savedTodoEntity = todoRepository.save(todoEntity);

        TodoDto savedtodoDto = new TodoDto();
        savedtodoDto.setId(savedTodoEntity.getId());
        BeanUtils.copyProperties(savedTodoEntity,savedtodoDto);

        logger.info("Todo created with ID: {}", savedtodoDto.getId());

        return savedtodoDto;
    }

    /**
     * getAllTodo - Retrieves all to-do
     *
     * @return - A list of To-do objects
     */
    @Override
    public List<TodoDto> getAllTodo() {
        logger.info("Fetching all Todos");
        List<TodoEntity> todos = todoRepository.findAll();

        if(todos.isEmpty()){
            logger.error("No Todos found");
            throw new TodoNotFoundException("No Todo items found");
        }
        List<TodoDto> todoDtoList = todos.stream()
                .map(todo -> {
                    TodoDto todoDto = new TodoDto();
                    BeanUtils.copyProperties(todo, todoDto);
                    return todoDto;
                })
                .collect(Collectors.toList());
        logger.info("Total Todos fetched: {}", todoDtoList.size());
        return todoDtoList;
    }

    /**
     * $getToDoById - Get To-Do object form id.
     *
     * @param id - to-do id.
     * @return - Returns to-do object.
     */
    @Override
    public TodoDto getToDoById(Long id) {
        logger.info("Fetching Todo with ID: {}", id);
        TodoEntity todoEntity =  todoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Todo with ID: {} not found", id);
                    return new TodoNotFoundException("Todo task not found with ID: " + id);
                });

        TodoDto todoDto = new TodoDto();
        BeanUtils.copyProperties(todoEntity, todoDto); // Copy properties from the entity to the DTO

        logger.info("Todo fetched: {}", todoDto);
        return todoDto; // Return the DTO
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
    public TodoDto updateTodoById(Long todoId, TodoDto todoDetails) {
        logger.info("Updating Todo with ID: {}", todoId);
        Optional<TodoEntity> existingTodoOptional = todoRepository.findById(todoId);

        if (existingTodoOptional.isPresent()) {
            TodoEntity existingTodo = existingTodoOptional.get();

            // Updated fields only if the value is present
            existingTodo.setTitle(todoDetails.getTitle() != null ? todoDetails.getTitle() : existingTodo.getTitle());
            existingTodo.setDescription(todoDetails.getDescription() != null ? todoDetails.getDescription() : existingTodo.getDescription());
            existingTodo.setPriority(todoDetails.getPriority() != null ? todoDetails.getPriority() : existingTodo.getPriority());
            existingTodo.setStatus(todoDetails.getStatus() != null ? todoDetails.getStatus() : existingTodo.getStatus());
            existingTodo.setDueDate(todoDetails.getDueDate() != null ? todoDetails.getDueDate() : existingTodo.getDueDate());
            existingTodo.setCreatedAt(existingTodo.getCreatedAt());
            existingTodo.setUpdatedAt(getCurrentDateAndTime());

            TodoEntity updatedTodo = todoRepository.save(existingTodo);
            TodoDto updatedTodoDto = new TodoDto();
            BeanUtils.copyProperties(updatedTodo,updatedTodoDto);
            logger.info("Todo with ID: {} updated successfully", todoId);
            return updatedTodoDto;
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
    public List<TodoDto> getTodoByPriority(String toDoPriority) {
        logger.info("Fetching Todos by priority: {}", toDoPriority);
        List<TodoEntity> todoListByPriority = todoRepository.findByPriority(toDoPriority);
        if(todoListByPriority.isEmpty()){
            logger.error("No Todos found with priority: {}", toDoPriority);
            throw new TodoNotFoundException("No Todos found with priority: " + toDoPriority);
        }
        List<TodoDto> todoDtoPriorityList = todoListByPriority.stream().map(todoEntity -> {
           TodoDto todoDto = new TodoDto();
           BeanUtils.copyProperties(todoEntity,todoDto);
           return todoDto;
        }).collect(Collectors.toList());
        logger.info("Total Todos fetched with priority {}: {}", toDoPriority, todoDtoPriorityList.size());
        return todoDtoPriorityList;
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
