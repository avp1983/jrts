package com.bssys.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "kp-dealer")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProviderList {

  @XmlElement(name = "provider")
  private List<UMTWebProvider> providers;

  public List<UMTWebProvider> getProviders() {
    if (providers == null) {
      providers = new ArrayList<>();
    }
    return providers;
  }

  public void setProviders(List<UMTWebProvider> providers) {
    this.providers = providers;
  }
}
