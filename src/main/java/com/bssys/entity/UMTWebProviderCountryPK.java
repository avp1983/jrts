package com.bssys.entity;

import java.io.Serializable;

public class UMTWebProviderCountryPK implements Serializable {

  private int providerID;
  private String countryCode;

  public UMTWebProviderCountryPK() {
  }

  public UMTWebProviderCountryPK(int providerID, String countryCode) {
    this.providerID = providerID;
    this.countryCode = countryCode;
  }

  public int getProviderID() {
    return providerID;
  }

  public void setProviderID(int providerID) {
    this.providerID = providerID;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UMTWebProviderCountryPK that = (UMTWebProviderCountryPK) o;

    if (providerID != that.providerID) {
      return false;
    }
    if (!countryCode.equals(that.countryCode)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = providerID;
    result = 31 * result + countryCode.hashCode();
    return result;
  }
}
