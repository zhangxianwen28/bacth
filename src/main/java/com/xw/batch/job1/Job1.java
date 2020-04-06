package com.xw.batch.job1;

import com.xw.batch.job1.bean.InData;
import com.xw.batch.job1.bean.OutData;
import com.xw.batch.utils.MyJobExecutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/**
 * @Auther: xw.z
 * @Date: 2020/4/3 07:35
 * @Description:
 */
@Configuration
public class Job1 {

  @Autowired
  DataSource dataSource;
  // 任务创建工厂
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  // 步骤创建工厂
  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  @Autowired
  private ItemProcessor1 itemProcessor1;


  @Bean
  public Job fileItemReaderJob() throws Exception {
    return jobBuilderFactory.get("fileItemReaderJob")
        .incrementer(new RunIdIncrementer())
        .start(step())
        .build();
  }

  @Bean
  public Step step() throws Exception {
    return stepBuilderFactory.get("step")
        .<InData, OutData>chunk(50)
        .reader(fileItemReader())
        .processor(itemProcessor1)
        .writer(fileItemWriter2())
        .faultTolerant() // 配置错误容忍
        .skip(MyJobExecutionException.class) // 配置跳过的异常类型
        .skipLimit(1000) // 最多跳过1次，1次过后还是异常的话，则任务会结束，
        .build();
  }

  @Bean
  public ItemReader<OutData> fileItemReader2() throws Exception {
    JdbcPagingItemReader<OutData> reader = new JdbcPagingItemReader<>();
    reader.setDataSource(dataSource); // 设置数据源
    reader.setFetchSize(5); // 每次取多少条记录
    reader.setPageSize(5); // 设置每页数据量

    // 指定sql查询语句 select id,field1,field2,field3 from TEST
    MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
    provider.setSelectClause("id,pid,moa_id,mod_en_name,id_path,name_path,node_level,is_leaf"); //设置查询字段
    provider.setFromClause("from temp_a"); // 设置从哪张表查询

    // 将读取到的数据转换为TestData对象
    reader.setRowMapper((resultSet, rowNum) -> {
      OutData data = new OutData();
      data.setId(resultSet.getString(1));
      data.setPid(resultSet.getString(2));
      data.setMoa_id(resultSet.getString(3));
      data.setMod_en_name(resultSet.getString(4));
      data.setId_path(resultSet.getString(5));
      data.setName_path(resultSet.getString(6));
      data.setNode_level(resultSet.getString(7));
      data.setIs_leaf(resultSet.getString(8));
      return data;
    });

    Map<String, Order> sort = new HashMap<>(1);
    sort.put("id", Order.ASCENDING);
    provider.setSortKeys(sort); // 设置排序,通过id 升序*/

    reader.setQueryProvider(provider);

    // 设置namedParameterJdbcTemplate等属性
    reader.afterPropertiesSet();
    return reader;
  }

  @Bean
  public ItemReader<InData> fileItemReader() {
    FlatFileItemReader<InData> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("batch-data-source.csv")); // 设置文件资源地址
    reader.setLinesToSkip(1); // 忽略第一行

    // AbstractLineTokenizer的三个实现类之一，以固定分隔符处理行数据读取,
    // 使用默认构造器的时候，使用逗号作为分隔符，也可以通过有参构造器来指定分隔符
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

    // 设置属性名，类似于表头
    tokenizer.setNames("flied01", "flied02", "flied03", "flied04", "flied05", "flied06",
        "flied07", "flied08", "flied09", "flied10", "flied11");
    // 将每行数据转换为Person对象
    DefaultLineMapper<InData> mapper = new DefaultLineMapper<>();
    mapper.setLineTokenizer(tokenizer);
    // 设置映射方式
    mapper.setFieldSetMapper(fieldSet -> {
      //
      InData data = new InData();

      data.setFlied01(fieldSet.readString("flied01"));
      data.setFlied02(fieldSet.readString("flied02"));
      data.setFlied03(fieldSet.readString("flied03"));
      data.setFlied04(fieldSet.readString("flied04"));
      data.setFlied05(fieldSet.readString("flied05"));
      data.setFlied06(fieldSet.readString("flied06"));
      data.setFlied07(fieldSet.readString("flied07"));
      data.setFlied08(fieldSet.readString("flied08"));
      data.setFlied09(fieldSet.readString("flied09"));
      data.setFlied10(fieldSet.readString("flied10"));
      data.setFlied11(fieldSet.readString("flied11"));
      return data;
    });

    reader.setLineMapper(mapper);
    return reader;
  }

  @Bean
  public FlatFileItemWriter<OutData> fileItemWriter() throws Exception {
    FlatFileItemWriter<OutData> writer = new FlatFileItemWriter<>();

    FileSystemResource file = new FileSystemResource("C:\\Users\\zhangxianwen\\Desktop\\file.csv");
    Path path = Paths.get(file.getPath());
    if (!Files.exists(path)) {
      Files.createFile(path);
    }
    writer.setResource(file); // 设置目标文件路径
    // 把读到的每个Person对象转换为字符串
    LineAggregator<OutData> aggregator = item -> {
      StringBuilder stringBuffer = new StringBuilder();
      stringBuffer.append(item.getId()).append(",");
      stringBuffer.append(item.getPid()).append(",");
      stringBuffer.append(item.getMoa_id()).append(",");
      stringBuffer.append(item.getMod_en_name()).append(",");
      stringBuffer.append(item.getId_path()).append(",");
      stringBuffer.append(item.getName_path()).append(",");
      stringBuffer.append(item.getNode_level()).append(",");
      stringBuffer.append(item.getIs_leaf()).append(",");
      stringBuffer.append(item.getCreate_by()).append(",");
      stringBuffer.append(item.getCreate_date()).append(",");
      stringBuffer.append(item.getUpdate_by()).append(",");
      stringBuffer.append(item.getUpdate_date()).append(",");
      return stringBuffer.toString();
    };

    writer.setLineAggregator(aggregator);
    writer.afterPropertiesSet();
    return writer;
  }

  @Bean
  public JdbcBatchItemWriter<OutData> fileItemWriter2() throws Exception {
    JdbcBatchItemWriter<OutData> writer = new JdbcBatchItemWriter<>();
    writer.setDataSource(dataSource); // 设置数据源
    String sql = "insert into temp_a(id,pid,moa_id,mod_en_name,id_path,name_path,node_level,is_leaf,create_by,"
        + "create_date,update_by,update_date) "
        + "values (:id,:pid,:moa_id,:mod_en_name,:id_path,:name_path,:node_level,:is_leaf,:create_by,:create_date,"
        + ":update_by,:update_date)";
    writer.setSql(sql); // 设置插入sql脚本

    // 映射TestData对象属性到占位符中的属性
    BeanPropertyItemSqlParameterSourceProvider<OutData> provider = new BeanPropertyItemSqlParameterSourceProvider<>();
    writer.setItemSqlParameterSourceProvider(provider);

    writer.afterPropertiesSet(); // 设置一些额外属性

    return writer;
  }
}
