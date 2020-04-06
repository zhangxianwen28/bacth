package com.xw.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther: xw.z
 * @Date: 2020/4/2 21:23
 * @Description:
 */
@SpringBootApplication
@EnableBatchProcessing //开启Spring Batch批处理功能
public class SpringBatchApplication implements ApplicationRunner {

  @Autowired
  Job fileItemReaderJob;
  @Autowired
  Job twojob;
  @Autowired
  private JobLauncher jobLauncher;

  public static void main(String[] args) {
    SpringApplication.run(SpringBatchApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    JobParameters parameters = new JobParametersBuilder()
        .addString("message", "mmmmmmmmmmmmmm")
        .toJobParameters();
    jobLauncher.run(twojob, parameters);
  }
}
