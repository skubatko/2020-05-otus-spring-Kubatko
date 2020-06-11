package ru.skubatko.dev.otus.spring.hw03.dao.impl;

import ru.skubatko.dev.otus.spring.hw03.dao.QuizDao;
import ru.skubatko.dev.otus.spring.hw03.domain.Answer;
import ru.skubatko.dev.otus.spring.hw03.domain.Question;
import ru.skubatko.dev.otus.spring.hw03.domain.Quiz;

import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collection;

@Repository
public class QuizDaoImpl implements QuizDao {
    private static final String COLON = ":";
    private static final String QUESTION_TAG = "Q";
    private static final char CORRECT_ANSWER_TAG = '+';

    private Quiz quiz;

    public QuizDaoImpl(@Value("${app.quiz}") String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            quiz = new Quiz();
            Multimap<Question, Answer> quizContent = quiz.getContent();

            Collection<String> lines = IOUtil.readLines(resource.getInputStream());
            String currentQuestion = StringUtils.EMPTY;
            for (String line : lines) {
                String[] buffer = line.split(COLON);
                String prefix = buffer[0];
                String content = buffer[1].trim();

                if (prefix.startsWith(QUESTION_TAG)) {
                    currentQuestion = content;
                } else {
                    Question question = new Question(currentQuestion);

                    boolean isCorrect = prefix.charAt(1) == CORRECT_ANSWER_TAG;
                    Answer answer = new Answer(content, isCorrect);
                    quizContent.put(question, answer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Quiz get() {
        return quiz;
    }
}
