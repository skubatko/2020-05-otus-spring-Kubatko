/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.skubatko.dev.otus.spring.hw01;

import ru.skubatko.dev.otus.spring.hw01.service.QuestionnaireService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        QuestionnaireService service = context.getBean(QuestionnaireService.class);
        service.printOut();
    }
}
