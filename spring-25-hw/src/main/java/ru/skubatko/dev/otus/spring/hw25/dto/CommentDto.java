package ru.skubatko.dev.otus.spring.hw25.dto;

import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommentDto {

    private String content;

    public static CommentDto toDto(SqlComment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentDto().setContent(comment.getContent());
    }
}
