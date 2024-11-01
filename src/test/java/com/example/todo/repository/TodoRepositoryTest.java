package com.example.todo.repository;

import com.example.todo.entity.TodoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;


    @BeforeEach
    void setUp() {
        todoRepository.save(new TodoEntity(1L,"Drive to airpot","Pick up Friends from airport","Pending","High",null,null,null));
        todoRepository.save(new TodoEntity(2L,"Go to Market","Buy fruits","Pending","High",null,null,null));
        todoRepository.save(new TodoEntity(3L,"Book Movie Tickets","Any Movies","Done","Medium",null,null,null));
    }

    @Test
    public void testFindByPriority() {
        List<TodoEntity> highPriorityTodos = todoRepository.findByPriority("High");

        assertThat(highPriorityTodos).isNotEmpty();
        assertThat(highPriorityTodos).hasSize(2); // Expecting 2 todos with High priority
        assertThat(highPriorityTodos).extracting(TodoEntity::getTitle)
                .containsExactlyInAnyOrder("Drive to airpot", "Go to Market");
    }
}