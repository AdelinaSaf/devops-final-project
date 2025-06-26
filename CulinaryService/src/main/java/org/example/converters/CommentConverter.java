package org.example.converters;

import org.example.dto.CommentCreateDTO;
import org.example.dto.CommentDTO;
import org.example.entity.Comment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CommentConverter {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Comment toEntity(CommentCreateDTO dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }

    public CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt().format(formatter));
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setRecipeId(comment.getRecipe().getId());
        return dto;
    }
}
