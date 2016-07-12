package com.bssys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@NamedNativeQueries({
    @NamedNativeQuery(name = "UMTLicenceType.getNameLike", query = "SELECT Name FROM UMTLicenceType " +
        " left outer join UMTLICENCETYPEEXTERNALID ON (UMTLICENCETYPE.ID =UMTLICENCETYPEEXTERNALID.ID ) " +
        " where UPPER(NAME) like ?1" +
    " and ((UMTLICENCETYPEEXTERNALID.SystemID = ?2) OR (UMTLICENCETYPEEXTERNALID.SystemID = ?3 and 0=(select count(id) from UMTLicenceTypeExternalID where SystemID=?2)))")
})

public class UMTLicenceType implements Serializable {
  @Id
  @Column(name = "ID")
  private int id;

  @Size(max = 255)
  @Column(name = "NAME")
  private String nameRu;

  @Size(max = 255)
  @Column(name = "NAME_EN")
  private String nameEn;

  public String getNameRu() {
    return nameRu;
  }
}