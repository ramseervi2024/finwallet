package com.rps.finwallet;

public class ApiResponse<T> {

    private String message;
    private int status;
    private Long totalEmployees;
    private T data;

    // Constructor WITHOUT data
    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    // Constructor WITH data
    public ApiResponse(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    // Constructor WITH data and parameters
    public ApiResponse(String message, int status, T data, Long totalEmployees) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.totalEmployees = totalEmployees;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Long getTotalEmployees() {
        return totalEmployees;
    }

    public T getData() {
        return data;
    }
}