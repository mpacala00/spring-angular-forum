package com.github.mpacala00.forum.exception;

import com.github.mpacala00.forum.exception.model.*;
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

    @ExceptionHandler(UserLockedException.class)
    public final ResponseEntity<HttpResponse> userLockedException(UserLockedException e) {
        return HttpResponse.createResponseEntity(HttpStatus.LOCKED, e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<HttpResponse> resourceNotFoundException(ResourceNotFoundException e) {
        return HttpResponse.createResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ActivationEmailException.class)
    public final ResponseEntity<HttpResponse> activationEmailException(ActivationEmailException e) {
        return HttpResponse.createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(EmailTakenException.class)
    public final ResponseEntity<HttpResponse> emailTakenException(EmailTakenException e) {
        return HttpResponse.createResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<HttpResponse> userAlreadyExistException(UserAlreadyExistException e) {
        return HttpResponse.createResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public final ResponseEntity<HttpResponse> invalidCredentialsException(InvalidCredentialsException e) {
        return HttpResponse.createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // ExceptionHandler for defautl exception overrides every other Exception handler
//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<HttpResponse> unknownException(Exception e) {
//        return HttpResponse.createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occurred");
//    }

}
