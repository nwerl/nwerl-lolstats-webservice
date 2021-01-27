package com.nwerl.lolstats.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<ErrorResponse> tooManyRequest(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.API_TOO_MANY_REQUEST);
        log.error("Called Api responsed: {}", ErrorCode.API_TOO_MANY_REQUEST.getStatus(), ErrorCode.API_TOO_MANY_REQUEST.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> notFoundException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.API_DATA_NOT_FOUND);
        log.error("Called Api responsed: {} {}", ErrorCode.API_DATA_NOT_FOUND.getStatus(), ErrorCode.API_DATA_NOT_FOUND.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> forbiddenException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.API_FORBIDDEN);
        log.error("Called Api responsed: {} {}", ErrorCode.API_FORBIDDEN.getStatus(), ErrorCode.API_FORBIDDEN.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        log.error("Server Internal Error: ", e);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}