package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
        // 부모 클래스 쪽으로 생성된 message를 전달한다는 뜻.
    }
}