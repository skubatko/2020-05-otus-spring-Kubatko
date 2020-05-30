package ru.skubatko.dev.otus.spring.hw01.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ru.skubatko.dev.otus.spring.hw01.domain.Questionnaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireDaoTest {

    @InjectMocks
    private QuestionnaireDaoImpl dao;

    @Test
    public void getOne() {
        dao.setFileName("questionnaire-test.csv");

        Questionnaire actual = dao.getOne();

        assertThat(actual).isNotNull();
        assertThat(actual.getQuestions()).isNotNull();
        assertThat(actual.getQuestions().get(0)).isEqualTo("test");
    }

    @Test
    public void getOneFailed() {
        dao.setFileName("unavailable.csv");

        assertThatThrownBy(() -> dao.getOne()).isInstanceOf(RuntimeException.class);
    }
}
