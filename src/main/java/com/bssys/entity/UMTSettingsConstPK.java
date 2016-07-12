package com.bssys.entity;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UMTSettingsConstPK implements Serializable {

  private String settings;
  private String constant;

  public UMTSettingsConstPK() {
  }

  public UMTSettingsConstPK(String settings, String constant) {
    this.settings = settings;
    this.constant = constant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UMTSettingsConstPK that = (UMTSettingsConstPK) o;

    if (!constant.equals(that.constant)) {
      return false;
    }
    if (!settings.equals(that.settings)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = settings.hashCode();
    result = 31 * result + constant.hashCode();
    return result;
  }

  public String getSettings() {
    return settings;
  }

  public void setSettings(String settings) {
    this.settings = settings;
  }

  public String getConstant() {
    return constant;
  }

  public void setConstant(String constant) {
    this.constant = constant;
  }
}
