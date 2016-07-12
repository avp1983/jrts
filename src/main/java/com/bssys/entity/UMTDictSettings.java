package com.bssys.entity;

import javax.persistence.*;

/**
 * Created by e.zemtcovsky on 19.02.2015.
 */


@NamedQueries({
  @NamedQuery(name = "UMTDictSettings.OneWinClientUniqueFields", query = "SELECT D FROM UMTDictSettings D WHERE D.sysid=0 and D.isunique=1 and D.dictid=1"),
  @NamedQuery(name = "UMTDictSettings.OneWinClientMandatoryFields", query = "SELECT D FROM UMTDictSettings D WHERE D.sysid=0 and D.ismandatory=1 and D.dictid=1")
})

@Entity
public class UMTDictSettings {
  private int id;
  private Integer sysid;
  private Integer dictid;
  private String fieldname;
  private String fieldnameloc;
  private String fieldnameint;
  private Short isunique;
  private Short ismandatory;

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  @Column(name = "SYSID")
  public Integer getSysid() {
    return sysid;
  }

  public void setSysid(Integer sysid) {
    this.sysid = sysid;
  }


  @Column(name = "DICTID")
  public Integer getDictid() {
    return dictid;
  }

  public void setDictid(Integer dictid) {
    this.dictid = dictid;
  }

  @Column(name = "FIELDNAME", nullable = false)
  public String getFieldname() {
    return fieldname;
  }

  public void setFieldname(String fieldname) {
    this.fieldname = fieldname;
  }


  @Column(name = "FIELDNAMELOC")
  public String getFieldnameloc() {
    return fieldnameloc;
  }

  public void setFieldnameloc(String fieldnameloc) {
    this.fieldnameloc = fieldnameloc;
  }


  @Column(name = "FIELDNAMEINT")
  public String getFieldnameint() {
    return fieldnameint;
  }

  public void setFieldnameint(String fieldnameint) {
    this.fieldnameint = fieldnameint;
  }


  @Column(name = "ISUNIQUE")
  public Short getIsunique() {
    return isunique;
  }

  public void setIsunique(Short isunique) {
    this.isunique = isunique;
  }


  @Column(name = "ISMANDATORY")
  public Short getIsmandatory() {
    return ismandatory;
  }

  public void setIsmandatory(Short ismandatory) {
    this.ismandatory = ismandatory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UMTDictSettings that = (UMTDictSettings) o;

    if (id != that.id) return false;
    if (dictid != null ? !dictid.equals(that.dictid) : that.dictid != null) return false;
    if (fieldname != null ? !fieldname.equals(that.fieldname) : that.fieldname != null) return false;
    if (fieldnameint != null ? !fieldnameint.equals(that.fieldnameint) : that.fieldnameint != null) return false;
    if (fieldnameloc != null ? !fieldnameloc.equals(that.fieldnameloc) : that.fieldnameloc != null) return false;
    if (ismandatory != null ? !ismandatory.equals(that.ismandatory) : that.ismandatory != null) return false;
    if (isunique != null ? !isunique.equals(that.isunique) : that.isunique != null) return false;
    if (sysid != null ? !sysid.equals(that.sysid) : that.sysid != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (sysid != null ? sysid.hashCode() : 0);
    result = 31 * result + (dictid != null ? dictid.hashCode() : 0);
    result = 31 * result + (fieldname != null ? fieldname.hashCode() : 0);
    result = 31 * result + (fieldnameloc != null ? fieldnameloc.hashCode() : 0);
    result = 31 * result + (fieldnameint != null ? fieldnameint.hashCode() : 0);
    result = 31 * result + (isunique != null ? isunique.hashCode() : 0);
    result = 31 * result + (ismandatory != null ? ismandatory.hashCode() : 0);
    return result;
  }
}
