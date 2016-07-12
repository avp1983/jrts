package com.bssys.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Cacheable(false)
@Entity
@Table(name = "UMTSettingsConstant")
public class UMTSettingsConst {

  public UMTSettingsConst() {
  }

  public UMTSettingsConst(UMTSettingsConstPK pk) {
    this.pk = pk;
  }

  @EmbeddedId
  private UMTSettingsConstPK pk;

  @Column(name = "Descrip")
  private String description;
  private Integer intValue;
  private String strValue;
  private Double doubleValue;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTimeValue;

  public Integer getIntValue() {
    return intValue;
  }

  public void setIntValue(Integer intValue) {
    this.intValue = intValue;
  }

  public String getStringValue() {
    return strValue;
  }

  public void setStringValue(String stringValue) {
    this.strValue = stringValue;
  }

  public Double getDoubleValue() {
    return doubleValue;
  }

  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  public Date getDateTimeValue() {
    return dateTimeValue;
  }

  public void setDateTimeValue(Date dateTimeValue) {
    this.dateTimeValue = dateTimeValue;
  }
}
