package org.example.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private String content;
    private Long recipeId;
}