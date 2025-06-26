package org.example.dto;

import lombok.Data;

@Data
public class UserAdminDTO {
    private Long id;
    private String username;
    private String email;
    private boolean admin;
    private String createdAt;
}
