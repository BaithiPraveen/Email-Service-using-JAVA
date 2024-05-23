package com.emailservice.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private LocalDateTime timeStamp;
    private String message;
    private String path;
    private String errorCode;
}
