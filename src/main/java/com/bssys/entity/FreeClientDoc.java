package com.bssys.entity;

import com.bssys.bls.types.DboDocPK;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedNativeQueries({
    @NamedNativeQuery(name = "FreeClientDoc.getStatusByPK",
        query = "SELECT CAST (Status AS DECIMAL) AS Status FROM FreeClientDoc "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "FreeClientDoc.getDocumentNumberByPK",
        query = "SELECT DocumentNumber from FreeClientDoc "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "FreeClientDoc.getDocumentDateByPK",
        query = "SELECT DocumentDate from FreeClientDoc "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "FreeClientDoc.getDocumentTypeStrByPK",
        query = "SELECT FreeDocType.Description from FreeClientDoc "
            + "inner join FreeDocType on FreeClientDoc.DocType = FreeDocType.DocType "
            + "where FreeClientDoc.CLIENT = ?1 and FreeClientDoc.DATECREATE = ?2 and FreeClientDoc.TIMECREATE = ?3"),
})
@Cacheable(false)
public class FreeClientDoc implements Serializable {
  private static final long serialVersionUID = 1L;
  @EmbeddedId
  private DboDocPK dboDocPK;

  @Size(max = 40)
  private String documentNumber;

  @Temporal(TemporalType.TIMESTAMP)
  private Date documentDate;

  private int status;

  public Integer getStatus() {
    return status;
  }

  public DboDocPK getDboDocPK() {
    return dboDocPK;
  }
}
