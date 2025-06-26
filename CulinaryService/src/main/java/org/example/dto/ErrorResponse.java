package org.example.dto;

public class ErrorResponse {
    private String message;
    private String path;
    private int status;
    private long timestamp;

    public ErrorResponse(String message, String path, int status) {
        this.message = message;
        this.path = path;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    // Геттеры
}
