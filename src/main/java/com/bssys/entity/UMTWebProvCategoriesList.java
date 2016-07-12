package com.bssys.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "kp-dealer")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProvCategoriesList {

  @XmlElement(name = "serviceProviderGroup")
  private List<UMTWebProvCategories> categories;

  public List<UMTWebProvCategories> getCategories() {
    return categories;
  }

  public void setCategories(List<UMTWebProvCategories> categories) {
    this.categories = categories;
  }
}
