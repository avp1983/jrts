package com.bssys.entity;

import com.google.common.base.Strings;

import javax.persistence.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

@NamedNativeQueries({
    @NamedNativeQuery(name = "UMTWebProvider.getProvidersByCategory",
        query =
            " SELECT distinct doc.ID, doc.Name, count(distinct ol.id) as Rating FROM UMTWEBPROVIDER doc "
                + "   left join UMTWebProvMapCategories mp on mp.PROVIDERID = doc.ID "
                + "   left join UMTPAYLEGAL ol on ol.PROVIDER = doc.ID and ol.DOCUMENTDATE between ?1 and ?2 and ol.PointId = ?3 "
                + " where (doc.isactive = 1) and mp.PROVIDERCATEGORYID = ?4 and (?5 = '' or upper(doc.Name) like upper(?5)) "
                + " and coalesce(doc.PAYMENTTYPE, 0) in (0,16010) "
                + " group by doc.ID, doc.Name "
                + " order by 3 desc ", resultSetMapping = "ProviderWithRating"),
    @NamedNativeQuery(name = "UMTWebProvider.getProviders",
        query =
            " SELECT distinct doc.ID, doc.Name, count(distinct ol.id) as Rating FROM UMTWEBPROVIDER doc "
                + "   left join UMTWebProvMapCategories mp on mp.PROVIDERID = doc.ID "
                + "   left join UMTPAYLEGAL ol on ol.PROVIDER = doc.ID and ol.DOCUMENTDATE between ?1 and ?2 and ol.PointId = ?3 "
                + " where (doc.isactive = 1) and (?4 = '' or upper(doc.Name) like upper(?4)) "
                + " and coalesce(doc.PAYMENTTYPE, 0) in (0,16010) "
                + " group by doc.ID, doc.Name "
                + " order by 3 desc, 2 ", resultSetMapping = "ProviderWithRating"),
    @NamedNativeQuery(name = "UMTWebProvider.getFavourites",
        query =
            "SELECT distinct doc.ID, doc.Name, count(distinct ol.id) as Rating FROM UMTWEBPROVIDER doc "
                + "   left join UMTWebProvMapCategories mp on mp.PROVIDERID = doc.ID "
                + "   left join UMTPAYLEGAL ol on ol.PROVIDER = doc.ID and ol.DOCUMENTDATE between ?1 and ?2 and ol.PointId = ?3 "
                + " where (doc.isactive = 1) "
                + " and coalesce(doc.PAYMENTTYPE, 0) in (0,16010) "
                + " group by doc.ID, doc.Name "
                + " having count(distinct ol.id) > 0 "
                + " order by 3 desc, 2 ", resultSetMapping = "ProviderWithRating")
})

@SqlResultSetMapping(
    name = "ProviderWithRating",
    classes = {
        @ConstructorResult(
            targetClass = UMTWebProviderHelper.class,
            columns = {
                @ColumnResult(name = "ID", type = Integer.class),
                @ColumnResult(name = "Name", type = String.class),
                @ColumnResult(name = "Rating", type = Integer.class)
            })
    })

@NamedQueries({
    @NamedQuery(name = "UMTWebProvider.getProviderCategories",
        query =
            "SELECT cat FROM UMTWebProvCategories cat "
                + "join UMTWebProvMapCategories catMap on cat.ID = catMap.providerCategoryID "
                + "where cat.isActive = 1 and catMap.providerID = :providerID")
})

@Cacheable(false)
@Entity
@Table(name = "UMTWebProvider")
@XmlRootElement(name = "provider")
@XmlAccessorType(NONE)
public class UMTWebProvider implements Serializable {
  private static final long serialVersionUID = 1L;

  public UMTWebProvider() {
    setActive(true);
    setUpdated(true);
    setLastUpdateTime(new Date());
  }

  @Column(name = "IsActive")
  private Boolean isActive;

  @Column(name = "IsUpdated")
  private Boolean isUpdated;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  @Id
  @XmlElement(name = "id", required = true)
  private int ID;

  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  @Column(name = "Name")
  @XmlElement(name = "name", required = true)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "MinAmount")
  @XmlElement(name = "minsum")
  private Float minAmount;

  @Column(name = "MaxAmount")
  @XmlElement(name = "maxsum")
  private Float maxAmount;

  @XmlElement(name = "providerPaymentType")
  private Integer paymentType;

  @Column(name = "LastUpdateTime")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdateTime;

  @Column(name = "InfoMessage")
  private String infoMessage;

  @XmlElementWrapper(name = "countries")
  @XmlElement(name = "country")
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "ProviderID")
  private List<UMTWebProviderCountry> countries;

  @XmlElementWrapper(name = "params")
  @XmlElement(name = "param")
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
  @OrderBy("autoKey DESC")
  @JoinColumn(name = "ProviderID")
  private List<UMTWebProviderParam> params;

  public Float getMinAmount() {
    return minAmount;
  }

  public void setMinAmount(Float minAmount) {
    this.minAmount = minAmount;
  }

  public Float getMaxAmount() {
    return maxAmount;
  }

  public void setMaxAmount(Float maxAmount) {
    this.maxAmount = maxAmount;
  }

  public Integer getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(Integer paymentType) {
    this.paymentType = paymentType;
  }

  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public String getInfoMessage() {
    return infoMessage;
  }

  public void setInfoMessage(String infoMessage) {
    this.infoMessage = infoMessage;
  }

  public List<UMTWebProviderParam> getParams() {
    return params;
  }

  public void setParams(List<UMTWebProviderParam> params) {
    this.params = params;
  }

  public List<UMTWebProviderCountry> getCountries() {
    return countries;
  }

  public boolean isUpdated() {
    return isUpdated;
  }

  public void setUpdated(boolean isUpdated) {
    this.isUpdated = isUpdated;
  }

  public void afterUnmarshal(Unmarshaller u, Object parent) {
    for (UMTWebProviderParam param : params) {
      if (Strings.isNullOrEmpty(param.getName())) {
        param.setName("phoneNumber");
      }
    }
  }
}
