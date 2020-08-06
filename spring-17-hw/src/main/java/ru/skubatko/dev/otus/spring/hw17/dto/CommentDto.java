package ru.skubatko.dev.otus.spring.hw17.dto;

import ru.skubatko.dev.otus.spring.hw17.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommentDto {

    @NotBlank(message = "Content is mandatory")
    private String content;

    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentDto().setContent(comment.getContent());
    }
}
