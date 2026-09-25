package com.rps.finwallet.exception;

import com.rps.finwallet.common.ApiResponse;
import com.rps.finwallet.model.*;
import com.rps.finwallet.repository.*;
import com.rps.finwallet.service.*;
import com.rps.finwallet.controller.*;
import com.rps.finwallet.dto.*;
import com.rps.finwallet.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

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

    // Insufficient Balance
    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleInsufficientBalance(InsufficientBalanceException ex) {
        return new ApiResponse<>(ex.getMessage(), 400);
    }

    // Duplicate Payout / Idempotency Conflict
    @ExceptionHandler(DuplicateDisbursementException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleDuplicateDisbursement(DuplicateDisbursementException ex) {
        return new ApiResponse<>(ex.getMessage(), 409);
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

    // FOr Any Validation Error Handle
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<String> errors=ex.getBindingResult().getFieldErrors().
                stream().map(error->error.getDefaultMessage())
                .collect(Collectors.toList());
        String errorMessage= String.join(", ", errors);

        return new ApiResponse<>(errorMessage,
                500);
    }
}