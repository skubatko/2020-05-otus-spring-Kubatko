package ru.skubatko.dev.otus.spring.hw11.dto;

import ru.skubatko.dev.otus.spring.hw11.domain.Comment;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentDto {
    private String content;

    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentDto().setContent(comment.getContent());
    }
}
