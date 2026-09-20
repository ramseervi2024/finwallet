package com.rps.finwallet.exception;

import com.rps.finwallet.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return new ApiResponse<>(
                ex.getMessage(),
                404);
    }

    // Wrong path parameter type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return new ApiResponse<>(
                "Invalid parameter: " + ex.getName(),
                400);
    }

    // Wrong URL
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNotFound(
            NoResourceFoundException ex) {

        return new ApiResponse<>(
                "API endpoint not found",
                404);
    }

    // Any other unexpected error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGeneralException(
            Exception ex) {

        ex.printStackTrace(); // Added for debugging

        return new ApiResponse<>(
                "Something went wrong",
                500);
    }
}