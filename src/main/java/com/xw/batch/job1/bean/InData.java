package com.xw.batch.job1.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xw.z
 * @Date: 2020/4/3 21:18
 * @Description:
 */

public class InData {


  private String id;
  private String name;
  private String flied00;
  private String flied01;
  private String flied02;
  private String flied03;
  private String flied04;
  private String flied05;
  private String flied06;
  private String flied07;
  private String flied08;
  private String flied09;
  private String flied10;
  private String flied11;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFlied00() {
    return flied00;
  }

  public void setFlied00(String flied00) {
    this.flied00 = flied00;
  }

  public String getFlied01() {
    return flied01;
  }

  public void setFlied01(String flied01) {
    this.flied01 = flied01;
  }

  public String getFlied02() {
    return flied02;
  }

  public void setFlied02(String flied02) {
    this.flied02 = flied02;
  }

  public String getFlied03() {
    return flied03;
  }

  public void setFlied03(String flied03) {
    this.flied03 = flied03;
  }

  public String getFlied04() {
    return flied04;
  }

  public void setFlied04(String flied04) {
    this.flied04 = flied04;
  }

  public String getFlied05() {
    return flied05;
  }

  public void setFlied05(String flied05) {
    this.flied05 = flied05;
  }

  public String getFlied06() {
    return flied06;
  }

  public void setFlied06(String flied06) {
    this.flied06 = flied06;
  }

  public String getFlied07() {
    return flied07;
  }

  public void setFlied07(String flied07) {
    this.flied07 = flied07;
  }

  public String getFlied08() {
    return flied08;
  }

  public void setFlied08(String flied08) {
    this.flied08 = flied08;
  }

  public String getFlied09() {
    return flied09;
  }

  public void setFlied09(String flied09) {
    this.flied09 = flied09;
  }

  public String getFlied10() {
    return flied10;
  }

  public void setFlied10(String flied10) {
    this.flied10 = flied10;
  }

  public String getFlied11() {
    return flied11;
  }

  public void setFlied11(String flied11) {
    this.flied11 = flied11;
  }

  public List<String> buildList() {
    List<String> list = new ArrayList<>();
    if(this.getFlied01()!=null && !"".equals(this.getFlied01())){
      list.add(this.getFlied01());
    }
    if(this.getFlied02()!=null && !"".equals(this.getFlied02())){
      list.add(this.getFlied02());
    }
    if(this.getFlied03()!=null && !"".equals(this.getFlied03())){
      list.add(this.getFlied03());
    }
    if(this.getFlied04()!=null && !"".equals(this.getFlied04())){
      list.add(this.getFlied04());
    }
    if(this.getFlied05()!=null && !"".equals(this.getFlied05())){
      list.add(this.getFlied05());
    }
    if(this.getFlied06()!=null && !"".equals(this.getFlied06())){
      list.add(this.getFlied06());
    }
    if(this.getFlied07()!=null && !"".equals(this.getFlied07())){
      list.add(this.getFlied07());
    }
    if(this.getFlied08()!=null && !"".equals(this.getFlied08())){
      list.add(this.getFlied08());
    }
    if(this.getFlied09()!=null && !"".equals(this.getFlied09())){
      list.add(this.getFlied09());
    }
    if(this.getFlied10()!=null && !"".equals(this.getFlied10())){
      list.add(this.getFlied10());
    }
    if(this.getFlied11()!=null && !"".equals(this.getFlied11())){
      list.add(this.getFlied11());
    }
    return list;
  }
}
