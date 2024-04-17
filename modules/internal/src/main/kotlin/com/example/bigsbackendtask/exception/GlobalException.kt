package com.example.bigsbackendtask.exception

class GlobalException(status: Int, message: String): RuntimeException(message) {
    val status: Int = status
}