package com.bssys.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;
import java.util.Objects;

@Cacheable(false)
@Entity
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProvParamElem implements Serializable {
  private static final long serialVersionUID = 1L;

  public UMTWebProvParamElem() {
    setUpdated(true);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int autoKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ProviderParamID")
  private UMTWebProviderParam param;

  @XmlAttribute(name = "value")
  @Column(name = "EnumKey")
  private String name;
  @XmlValue
  @Column(name = "EnumValue")
  private String value;

  @Column(name = "IsUpdated", updatable = true)
  private Boolean updated;

  public int getAutoKey() {
    return autoKey;
  }

  public void setAutoKey(int autoKey) {
    this.autoKey = autoKey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Boolean getUpdated() {
    return updated;
  }

  public void setUpdated(Boolean updated) {
    this.updated = updated;
  }

  public void setParam(UMTWebProviderParam param) {
    this.param = param;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UMTWebProvParamElem elem = (UMTWebProvParamElem) o;

    if (Objects.equals(updated, elem.updated)) {
      return false;
    }
    if (Objects.equals(name, elem.name)) {
      return false;
    }
    if (Objects.equals(value, elem.value)) {
      return false;
    }
    if (Objects.equals(param.getAutoKey(), elem.param.getAutoKey())) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = autoKey;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (param != null ? param.hashCode() : 0);
    result = 31 * result + (updated != null ? updated.hashCode() : 0);
    return result;
  }
  public void afterUnmarshal(Unmarshaller u, Object parent) {
    this.setParam((UMTWebProviderParam) parent);
  }
}
