package com.xw.batch.job1;

import com.xw.batch.job1.bean.InData;
import com.xw.batch.job1.bean.OutData;
import com.xw.batch.utils.ID;
import com.xw.batch.utils.MyJobExecutionException;
import java.util.List;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @Auther: xw.z
 * @Date: 2020/4/3 20:38
 * @Description:
 */
@Component
public class ItemProcessor1 implements ItemProcessor<InData, OutData> {

  @Autowired
  JdbcTemplate jdbcTemplate;
  String sql = "SELECT DISTINCT id  FROM temp  A WHERE A.name=?";
  //DateFormat df = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
  private static  int count = 0;

  String sq2 = "SELECT DISTINCT name  FROM temp  A WHERE A.id=?";
  String sql3 = "SELECT id  FROM temp_a  A WHERE A.mod_en_name=?";

  @Override
  public OutData process(InData person) throws Exception {
    List<String> strings = person.buildList();
    String pid = null;
    StringBuffer path_id = new StringBuffer();
    StringBuffer path_name = new StringBuffer();
    String node_level = null;
    String id = null;
    String name = null;
    String flag = null;
    String date = "2019/9/9 12:04:01";
    count++;


    for (int i = 0; i < strings.size(); i++) {
      //System.out.println(" name :"+strings.get(i));
      try {
        id = jdbcTemplate.queryForObject(sql, new Object[]{strings.get(i)}, String.class);
      } catch (Exception e) {
        throw new MyJobExecutionException("行"+count+"找不到ID " + strings.get(i));
        //System.err.println("行"+count+"找不到ID " + strings.get(i));
        //break;
      }

      if (i == 0) {
        pid = "-1";
        flag = "0";
      } else {
        try {
          pid = jdbcTemplate.queryForObject(sql, new Object[]{strings.get(i - 1)}, String.class);
        }catch (Exception e){
          throw new MyJobExecutionException("行"+count+"找不到ID " + strings.get(i));

          //System.err.println("行"+count+"找不到PID " + strings.get(i));
        }
        flag = "1";
      }
      name = strings.get(i);

      path_id.append("/").append(id);
      path_name.append("/").append(strings.get(i));
      node_level = strings.size() + "";
    }

    path_name.append("/");
    path_id.append("/");
    OutData outData = new OutData();
    outData.setId(ID.atomicLong.getAndIncrement()+"");
    outData.setPid(pid);
    outData.setMoa_id(id);
    outData.setMod_en_name(name);
    outData.setId_path(path_id.toString());
    outData.setName_path(path_name.toString());
    outData.setNode_level(node_level);
    outData.setIs_leaf(flag);
    outData.setCreate_by("1");
    outData.setCreate_date(date);
    outData.setUpdate_by("1");
    outData.setUpdate_date(date);





    return outData;
  }


}
