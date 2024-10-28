package com.example.todo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 *  It is an to-do entity mapped to a database table
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Please add Title")
    private String title;

    private String description;

    private String status;

    @NotBlank(message = "Please Set Priority")
    private String priority;

    private LocalDate dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
