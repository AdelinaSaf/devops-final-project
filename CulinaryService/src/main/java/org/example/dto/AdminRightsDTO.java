package org.example.dto;

import lombok.Data;

@Data
public class AdminRightsDTO {
    private Long userId;
    private boolean grant;
}
