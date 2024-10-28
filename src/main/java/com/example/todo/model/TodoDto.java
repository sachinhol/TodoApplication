package com.example.todo.model;/*
 * Author: Sachin
 * Date: 28-Oct-24
 * Time: DTO TO-DO Class
 */

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoDto {

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
