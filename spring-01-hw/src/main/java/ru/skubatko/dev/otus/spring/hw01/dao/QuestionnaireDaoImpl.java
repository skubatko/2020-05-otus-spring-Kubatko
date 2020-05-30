package ru.skubatko.dev.otus.spring.hw01.dao;

import ru.skubatko.dev.otus.spring.hw01.domain.Questionnaire;

import lombok.Setter;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Setter
public class QuestionnaireDaoImpl implements QuestionnaireDao {

    private String fileName;

    @Override
    public Questionnaire getOne() {
        try {
            Resource resource = new ClassPathResource(fileName);
            Collection<String> lines = IOUtil.readLines(resource.getInputStream());
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestions(new ArrayList<>(lines));
            return questionnaire;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
