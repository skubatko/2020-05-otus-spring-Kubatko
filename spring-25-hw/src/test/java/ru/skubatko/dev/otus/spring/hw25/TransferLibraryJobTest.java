package ru.skubatko.dev.otus.spring.hw25;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skubatko.dev.otus.spring.hw25.JobConfig.TRANSFER_LIBRARY_JOB_NAME;

import ru.skubatko.dev.otus.spring.hw25.service.NoSqlLibraryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Задание по переносу библиотеки")
@SpringBatchTest
@SpringBootTest
class TransferLibraryJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private NoSqlLibraryService noSqlLibraryService;

    @BeforeEach
    void setUp() {
        jobRepositoryTestUtils.removeJobExecutions();
        noSqlLibraryService.deleteAll();
    }

    @DisplayName("должно переносить библиотеку")
    @Test
    void shouldTransferLibrary() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(TRANSFER_LIBRARY_JOB_NAME);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertThat(noSqlLibraryService.findAllBooks()).hasSize(6);
    }
}
