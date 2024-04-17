package com.example.bigsbackendtask.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException::class)
    fun globalExceptionHandle(e: GlobalException): ResponseEntity<String> {
        return ResponseEntity.status(e.status).body(e.message)
    }

}