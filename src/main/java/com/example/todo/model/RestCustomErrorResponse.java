package com.example.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * This is Model class to handle exception response
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestCustomErrorResponse {
    private int status;
    private String message;
}
