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

@NamedNativeQueries({
    @NamedNativeQuery(name = "UMTWebProvCategories.getCategories",
        query =
            "SELECT distinct cat.ID, cat.Name, count(distinct ol.id) as Rating FROM UMTWEBPROVCATEGORIES cat "
                + "   left join UMTWebProvMapCategories mp on mp.PROVIDERCATEGORYID = cat.ID "
                + "   left join UMTPAYLEGAL ol on ol.PROVIDER = mp.PROVIDERID and ol.DOCUMENTDATE between ?1 and ?2 and ol.PointId = ?3 "
                + " where (cat.isactive = 1) "
                + "   and exists(select p.ID from UMTWEBPROVIDER p where p.ID = mp.PROVIDERID and p.ISACTIVE = 1 and (?4 = '' or upper(p.Name) like ?4) "
                + "   and coalesce(p.PAYMENTTYPE, 0) in (0,16010)) "
                + " group by cat.ID, cat.Name "
                + " order by Rating desc, cat.Name ", resultSetMapping = "ProviderCategoryWithRating")
})

@SqlResultSetMapping(
    name = "ProviderCategoryWithRating",
    classes = {
        @ConstructorResult(
            targetClass = UMTWebProviderCategoryHelper.class,
            columns = {
                @ColumnResult(name = "ID", type = Integer.class),
                @ColumnResult(name = "Name", type = String.class),
                @ColumnResult(name = "Rating", type = Integer.class)
            })
    })

@Cacheable(false)
@Entity
@Table(name = "UMTWebProvCategories")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProvCategories implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @XmlAttribute(name = "id")
  private int ID;

  @XmlAttribute(name = "name")
  private String name;

  @Column(name = "isActive")
  private Integer isActive;

  @Column(name = "isUpdated")
  private Boolean isUpdated;

  @Column(name = "icoPath", insertable = false, updatable = false)
  private String icoPath;

  @XmlElementWrapper(name = "providerIds")
  @XmlElement(name = "id")
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval=true)
  @JoinColumn(name = "ProviderCategoryID")
  private List<UMTWebProvMapCategories> categoriesMaps;

  public UMTWebProvCategories() {
    setActive(1);
    setIsUpdated(true);
  }

  public String getName() {
    return name;
  }

  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public Integer isActive() {
    return isActive;
  }

  public void setActive(Integer isActive) {
    this.isActive = isActive;
  }

  public List<UMTWebProvMapCategories> getCategoriesMaps() {
    return categoriesMaps;
  }

  public Boolean getIsUpdated() {
    return isUpdated;
  }

  public void setIsUpdated(Boolean isUpdated) {
    this.isUpdated = isUpdated;
  }

  public void afterUnmarshal(Unmarshaller u, Object parent) {
    // Грязный хак, так как afterUnmarshall в UMTWebProvMapCategories не вызывается
    for (UMTWebProvMapCategories mapCategories : categoriesMaps) {
      mapCategories.setProviderCategoryID(getID());
    }
  }

  public String getIcoPath() {
    return icoPath;
  }

  public void setIcoPath(String icoPath) {
    this.icoPath = icoPath;
  }
}
