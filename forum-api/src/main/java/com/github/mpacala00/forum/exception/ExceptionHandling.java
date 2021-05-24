package com.github.mpacala00.forum.exception;

import com.github.mpacala00.forum.pojos.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException e) {
        return HttpResponse.createResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ActivationEmailException.class)
    public final ResponseEntity<HttpResponse> activationEmailException(ActivationEmailException e) {
        return HttpResponse.createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    // ExceptionHandler for defautl exception overrides every other Exception handler
//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<HttpResponse> unknownException(Exception e) {
//        return HttpResponse.createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occurred");
//    }

}
