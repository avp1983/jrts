package com.bssys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@NamedQueries({
    @NamedQuery(name = "Country.codeOrNameStartWith", query = "SELECT C FROM Country C WHERE " +
        "lower(C.nameShortRu) like :query or C.code like :query "),
    @NamedQuery(name = "Country.all", query = "SELECT C FROM Country C "),
    @NamedQuery(name = "Country.findByCode", query = "SELECT C FROM Country C where c.code = :code")
})
public class Country implements Serializable {

  @Id
  private int autoKey;

  @Size(max = 3)
  private String code;

  @Size(max = 50)
  @Column(name = "nameShort_ru")
  private String nameShortRu;

  public String getNameShortRu() {
    return nameShortRu;
  }

  public String getCode() {
    return code;
  }
}