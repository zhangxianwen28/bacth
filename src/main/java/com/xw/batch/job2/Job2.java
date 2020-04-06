package com.xw.batch.job2;

import com.alibaba.excel.EasyExcel;
import com.xw.batch.job1.bean.InData;
import com.xw.batch.job1.bean.OutData;
import com.xw.batch.job2.bean.DemoData;
import com.xw.batch.utils.MyJobExecutionException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Auther: xw.z
 * @Date: 2020/4/3 07:35
 * @Description:
 */
@Configuration
public class Job2 {

  @Autowired
  Processor processor;
  @Autowired
  DataSource dataSource;
  // 任务创建工厂
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  // 步骤创建工厂
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job twojob() throws Exception {
    return jobBuilderFactory.get("twojob")
        .incrementer(new RunIdIncrementer())
        .start(step2())
        .build();
  }
  @Bean
  public Step step2() throws Exception {
    return stepBuilderFactory.get("step")
        .<DemoData, DemoData>chunk(50)
        .reader(mySimpleItemReader())
        .writer(System.out::println)
        .faultTolerant() // 配置错误容忍
        .skip(MyJobExecutionException.class) // 配置跳过的异常类型
        .skipLimit(1000) // 最多跳过1次，1次过后还是异常的话，则任务会结束，
        .build();
  }
  private ItemReader<DemoData> mySimpleItemReader() {
    // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    // 写法1：
    String fileName =  "demo" + File.separator + "demo.xlsx";
    // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
    //EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    List<DemoData> list = EasyExcel.read(fileName).head(DemoData.class).sheet().doReadSync();

    List<String> data = Arrays.asList("java", "c++", "javascript", "python");
    return new MySimpleItemReader(list);
  }


}
