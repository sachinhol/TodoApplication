package com.example.todo.exceptions;

/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 *  This class is for custom to-do task not found exception
 */

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(String message) {
        super(message);
    }
}