package com.bssys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "UMTTRANSFERSYSDATA")
public class UmtTransferSysData implements Serializable {
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("UnusedDeclaration")
  @Id
  private Long sysID;

  @SuppressWarnings("UnusedDeclaration")
  @Column(name = "NAME")
  private String name;

  @SuppressWarnings("UnusedDeclaration")
  @Column(name = "NAME_EN")
  private String nameEng;

  public String getName() {
    return name;
  }

  public String getNameEng() {
    return nameEng;
  }
}
