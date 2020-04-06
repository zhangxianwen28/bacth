package com.xw.batch.job2;

import com.xw.batch.job2.bean.DemoData;
import java.util.Iterator;
import java.util.List;
import org.springframework.batch.item.ItemReader;


/**
 * @Auther: xw.z
 * @Date: 2020/4/6 16:50
 * @Description:
 */
public class MySimpleItemReader implements ItemReader<DemoData> {

  private Iterator<DemoData> iterator;

  public MySimpleItemReader(List<DemoData> data) {
    this.iterator = data.iterator();
  }

  @Override
  public DemoData read()   {
    return iterator.hasNext() ? iterator.next() : null;
  }
}
