package org.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class UserUpdateDTO {
    private Long id;
    private String username;
    private String email;
    private MultipartFile avatar;
    private Set<String> preferences;
}
