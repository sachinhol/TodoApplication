package com.example.todo.repository;

import com.example.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * This interface is To-do repository
 */


@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByPriority(String toDoPriority);
}
