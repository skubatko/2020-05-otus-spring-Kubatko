package ru.skubatko.dev.otus.spring.hw01.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw01.dao.QuestionnaireDao;
import ru.skubatko.dev.otus.spring.hw01.domain.Questionnaire;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireServiceTest {

    @Mock
    private  QuestionnaireDao dao;

    @InjectMocks
    private QuestionnaireServiceImpl service;

    @Test
    public void printOut() {
        when(dao.getOne()).thenReturn(new Questionnaire());

        service.printOut();

        verify(dao).getOne();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void printOutWhenDaoFailed() {
        when(dao.getOne()).thenThrow(new RuntimeException());

        assertThatThrownBy(()->service.printOut()).isInstanceOf(RuntimeException.class);

        verify(dao).getOne();
        verifyNoMoreInteractions(dao);
    }
}
