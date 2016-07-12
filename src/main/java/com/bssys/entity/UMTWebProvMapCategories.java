package com.bssys.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;

@Cacheable(false)
@Entity
@Table(name = "UMTWebProvMapCategories")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProvMapCategories implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private int providerID;
  @Id
  private int providerCategoryID;
  private Boolean isUpdated;

  public UMTWebProvMapCategories() {
    setIsUpdated(true);
  }

  public int getProviderCategoryID() {
    return providerCategoryID;
  }

  public void setProviderCategoryID(int providerCategoryID) {
    this.providerCategoryID = providerCategoryID;
  }

  @XmlValue
  public int getProviderID() {
    return providerID;
  }

  public void setProviderID(int providerID) {
    this.providerID = providerID;
  }

  public Boolean getIsUpdated() {
    return isUpdated;
  }

  public void setIsUpdated(Boolean isUpdated) {
    this.isUpdated = isUpdated;
  }

  public void afterUnmarshal(Unmarshaller u, Object parent) {
    this.setProviderCategoryID(((UMTWebProvCategories) parent).getID());
  }
}
