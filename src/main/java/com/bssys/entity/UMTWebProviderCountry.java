package com.bssys.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

@Cacheable(false)
@Entity
@Table(name = "UMTWebProviderCountry")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProviderCountry implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  int providerID;
  @Id
  private String countryCode;
  private boolean isUpdated;

  public UMTWebProviderCountry() {
    setUpdated(true);
  }

  @XmlAttribute(name = "code")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public int getProviderID() {
    return this.providerID;
  }

  public void setProviderID(int providerID) {
    this.providerID = providerID;
  }

  public boolean isUpdated() {
    return isUpdated;
  }

  public void setUpdated(boolean isUpdated) {
    this.isUpdated = isUpdated;
  }

  public void afterUnmarshal(Unmarshaller u, Object parent) {
    this.setProviderID(((UMTWebProvider) parent).getID());
  }
}
