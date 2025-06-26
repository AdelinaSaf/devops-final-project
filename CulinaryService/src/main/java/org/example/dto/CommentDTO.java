package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String createdAt;
    private Long userId;
    private String username;
    private String recipeName;
    private Long recipeId;


    public CommentDTO(Long id, String content, String createdAt, Long recipeId, String recipeName) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
    }
}
