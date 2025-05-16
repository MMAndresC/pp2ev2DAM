package com.svalero.musicApi.controller;

import com.svalero.musicApi.domain.AuthTokenDto;
import com.svalero.musicApi.domain.ErrorResponse;
import com.svalero.musicApi.domain.UserLoginDto;
import com.svalero.musicApi.exception.PasswordIncorrectException;
import com.svalero.musicApi.exception.UserNotFoundException;
import com.svalero.musicApi.service.LoginService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService authService;

    public LoginController(LoginService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@Valid @RequestBody UserLoginDto userLogin) throws UserNotFoundException, PasswordIncorrectException {
        String token = this.authService.login(userLogin.getEmail(), userLogin.getPassword());
        AuthTokenDto authTokenDto = new AuthTokenDto(token);
        return new ResponseEntity<>(authTokenDto, HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        ErrorResponse error = ErrorResponse.generalError(404, exception.getMessage());
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorResponse> handlePasswordIncorrectException(PasswordIncorrectException exception) {
        ErrorResponse error = ErrorResponse.generalError(401, exception.getMessage());
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(ErrorResponse.validationError(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = ErrorResponse.generalError(500, "Internal Server Error");
        this.logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
