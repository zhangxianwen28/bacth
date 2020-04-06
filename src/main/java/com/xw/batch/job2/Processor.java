package com.xw.batch.job2;

import com.xw.batch.job1.bean.OutData;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @Auther: xw.z
 * @Date: 2020/4/6 14:30
 * @Description:
 */
@Component
public class Processor implements ItemProcessor<OutData, OutData> {

  @Autowired
  JdbcTemplate jdbcTemplate;
  String sql = "SELECT DISTINCT name  FROM temp  A WHERE A.id=?";
  String sql2 = "SELECT id  FROM temp_a  A WHERE A.mod_en_name=?";

  @Override
  public OutData process(OutData inData) throws Exception {
    String name =null;
    String pid = null;


    if(!"-1".equals(inData.getPid())){
      try {
       name  = jdbcTemplate.queryForObject(sql, new Object[]{inData.getPid()}, String.class);
       pid  = jdbcTemplate.queryForObject(sql2, new Object[]{name}, String.class);
      jdbcTemplate.update("UPDATE temp_a SET PID =? where id=?", pid,inData.getId());
      }catch (Exception e){
        System.out.println(inData.getPid() +"  "+name+"    "+pid);
      }
    }

    return null;
  }
}
