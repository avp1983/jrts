package com.bssys.entity;

import java.io.Serializable;

public class UMTWebProvMapCategoriesPK implements Serializable {

  private int providerID;
  private int providerCategoryID;

  public int getProviderCategoryID() {
    return providerCategoryID;
  }

  public void setProviderCategoryID(int providerCategoryID) {
    this.providerCategoryID = providerCategoryID;
  }

  public int getProviderID() {
    return providerID;
  }

  public void setProviderID(int providerID) {
    this.providerID = providerID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UMTWebProvMapCategoriesPK that = (UMTWebProvMapCategoriesPK) o;

    if (providerCategoryID != that.providerCategoryID) {
      return false;
    }
    if (providerID != that.providerID) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = providerID;
    result = 31 * result + providerCategoryID;
    return result;
  }
}
