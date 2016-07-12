package com.bssys.entity;

import java.io.Serializable;

public class UMTWebProviderHelper implements Serializable {

  private Integer id;
  private String name;
  private Integer rating;

  public UMTWebProviderHelper(Integer id, String name, Integer rating) {
    this.id = id;
    this.name = name;
    this.rating = rating;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return name;
  }
}
