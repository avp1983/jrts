package com.bssys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "UMTTrSysMandatoryFields")
@NamedQueries(
    @NamedQuery(name = "UMTTrSysMandatoryFields.allBySysIdAndTransferType",
        query = "select f from UMTTrSysMandatoryFields f where f.sysId = :sysId and f.transferType = :transferType")
)
public class UMTTrSysMandatoryFields implements Serializable {
  @Id
  @Column(name = "ORDERKEY")
  private Integer orderKey;
  @Column(name = "SYSID")
  private int sysId;
  @Column(name = "TRANSFERTYPE")
  private int transferType;
  @Column(name = "FIELDNAME", length = 50)
  @Size(max = 50)
  private String fieldName;

  public String getFieldName() {
    return fieldName;
  }
}