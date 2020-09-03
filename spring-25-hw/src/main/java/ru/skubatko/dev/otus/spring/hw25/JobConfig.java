package ru.skubatko.dev.otus.spring.hw25;

import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlBook;
import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlBook;
import ru.skubatko.dev.otus.spring.hw25.service.TransformService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static final String TRANSFER_LIBRARY_JOB_NAME = "transferLibraryJob";
    private static final int CHUNK_SIZE = 5;

    @Bean
    public ItemReader<SqlBook> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<SqlBook>()
                       .name("BookReader")
                       .entityManagerFactory(entityManagerFactory)
                       .queryString("SELECT b FROM SqlBook b JOIN FETCH b.comments")
                       .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<SqlBook, NoSqlBook> processor(TransformService transformService) {
        return transformService::transform;
    }

    @StepScope
    @Bean
    public ItemWriter<NoSqlBook> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<NoSqlBook>()
                       .collection("books")
                       .template(mongoTemplate)
                       .build();
    }

    @Bean
    public Job transferLibraryJob(Step transferBook) {
        return jobBuilderFactory.get(TRANSFER_LIBRARY_JOB_NAME)
                       .incrementer(new RunIdIncrementer())
                       .flow(transferBook)
                       .end()
                       .listener(new JobExecutionListener() {
                           @Override
                           public void beforeJob(JobExecution jobExecution) {
                               log.info("Начало job");
                           }

                           @Override
                           public void afterJob(JobExecution jobExecution) {
                               log.info("Конец job");
                           }
                       })
                       .build();
    }

    @Bean
    public Step transferBook(ItemWriter<NoSqlBook> writer, ItemReader<SqlBook> reader, ItemProcessor<SqlBook, NoSqlBook> processor) {
        return stepBuilderFactory.get("transferBook")
                       .<SqlBook, NoSqlBook>chunk(CHUNK_SIZE)
                       .reader(reader)
                       .processor(processor)
                       .writer(writer)
                       .listener(new ItemReadListener<>() {
                           @Override
                           public void beforeRead() {
                               log.info("Начало чтения");
                           }

                           @Override
                           public void afterRead(SqlBook o) {
                               log.info("Конец чтения");
                           }

                           @Override
                           public void onReadError(Exception e) {
                               log.info("Ошибка чтения");
                           }
                       })
                       .listener(new ItemWriteListener<NoSqlBook>() {
                           @Override
                           public void beforeWrite(List<? extends NoSqlBook> list) {
                               log.info("Начало записи");
                           }

                           @Override
                           public void afterWrite(List<? extends NoSqlBook> list) {
                               log.info("Конец записи");
                           }

                           @Override
                           public void onWriteError(Exception e, List list) {
                               log.info("Ошибка записи");
                           }
                       })
                       .listener(new ItemProcessListener<SqlBook, NoSqlBook>() {
                           @Override
                           public void beforeProcess(SqlBook o) {
                               log.info("Начало обработки");
                           }

                           @Override
                           public void afterProcess(SqlBook o, NoSqlBook o2) {
                               log.info("Конец обработки");
                           }

                           @Override
                           public void onProcessError(SqlBook o, Exception e) {
                               log.info("Ошбка обработки");
                           }
                       })
                       .listener(new ChunkListener() {
                           @Override
                           public void beforeChunk(ChunkContext chunkContext) {
                               log.info("Начало пачки");
                           }

                           @Override
                           public void afterChunk(ChunkContext chunkContext) {
                               log.info("Конец пачки");
                           }

                           @Override
                           public void afterChunkError(ChunkContext chunkContext) {
                               log.info("Ошибка пачки");
                           }
                       })
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                       .build();
    }
}
