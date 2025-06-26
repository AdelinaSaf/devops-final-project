package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
