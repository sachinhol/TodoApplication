package com.example.todo.repository;

import com.example.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * This interface is To-do repository
 */


@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByPriority(String toDoPriority);
}
