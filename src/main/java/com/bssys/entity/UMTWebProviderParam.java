package com.bssys.entity;

import javax.persistence.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.List;

@Cacheable(false)
@Entity
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProviderParam implements Serializable {
  private static final long serialVersionUID = 1L;

  public UMTWebProviderParam() {
    setUpdated(true);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int autoKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ProviderID")
  private UMTWebProvider provider;

  @XmlAttribute(name = "name")
  private String name;
  @XmlElement(name = "minlength")
  private Integer minLength;
  @XmlElement(name = "maxlength")
  private Integer maxLength;
  @XmlElement(name = "pattern")
  private String pattern;
  @XmlElement(name = "patterndesc")
  private String patternDesc;
  @XmlElement(name = "mask")
  private String mask;
  @Column(name = "ParamType")
  @XmlElement(name = "type")
  private String type;
  @Column(name = "ParamTitle")
  @XmlElement(name = "title")
  private String title;
  @Column(name = "IsUpdated", updatable = true)
  private Boolean updated;
  @XmlElementWrapper(name = "elements")
  @XmlElement(name = "item")
  @OneToMany(mappedBy = "param", orphanRemoval = true, cascade = {})
  @OrderBy("autoKey DESC")
  private List<UMTWebProvParamElem> items;

  public String getTitle() {
    return title;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setProvider(UMTWebProvider provider) {
    this.provider = provider;
  }

  public String getMask() {
    return mask;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public Integer getMinLength() {
    return minLength;
  }

  public String getPatternDesc() {
    return patternDesc;
  }

  public int getAutoKey() {
    return autoKey;
  }

  public void setAutoKey(int autoKey) {
    this.autoKey = autoKey;
  }

  public boolean isUpdated() {
    return updated;
  }

  public void setUpdated(boolean isUpdated) {
    this.updated = isUpdated;
  }

  public List<UMTWebProvParamElem> getItems() {
    return items;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UMTWebProviderParam param = (UMTWebProviderParam) o;

    if (autoKey != param.autoKey) {
      return false;
    }
    if (updated != null ? !updated.equals(param.updated) : param.updated != null) {
      return false;
    }
    if (mask != null ? !mask.equals(param.mask) : param.mask != null) {
      return false;
    }
    if (maxLength != null ? !maxLength.equals(param.maxLength) : param.maxLength != null) {
      return false;
    }
    if (minLength != null ? !minLength.equals(param.minLength) : param.minLength != null) {
      return false;
    }
    if (name != null ? !name.equals(param.name) : param.name != null) {
      return false;
    }
    if (pattern != null ? !pattern.equals(param.pattern) : param.pattern != null) {
      return false;
    }
    if (patternDesc != null ? !patternDesc.equals(param.patternDesc) : param.patternDesc != null) {
      return false;
    }
    if (provider != null ? !provider.equals(param.provider) : param.provider != null) {
      return false;
    }
    if (title != null ? !title.equals(param.title) : param.title != null) {
      return false;
    }
    if (type != null ? !type.equals(param.type) : param.type != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = autoKey;
    result = 31 * result + (provider != null ? provider.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (minLength != null ? minLength.hashCode() : 0);
    result = 31 * result + (maxLength != null ? maxLength.hashCode() : 0);
    result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
    result = 31 * result + (patternDesc != null ? patternDesc.hashCode() : 0);
    result = 31 * result + (mask != null ? mask.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (updated != null ? updated.hashCode() : 0);
    return result;
  }

  public void afterUnmarshal(Unmarshaller u, Object parent) {
    this.setProvider((UMTWebProvider) parent);
  }
}
