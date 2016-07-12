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
    @NamedNativeQuery(name = "FreeAgExDoc.getStatusByPK",
        query = "SELECT CAST (Status AS DECIMAL) AS Status FROM FreeAgExDoc "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "FreeAgExDoc.getDocumentNumberByPK",
        query = "SELECT DocumentNumber from FreeAgExDoc "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "FreeAgExDoc.getDocumentDateByPK",
        query = "SELECT DocumentDate from FreeAgExDoc "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "FreeAgExDoc.getDocumentTypeStrByPK",
        query = "SELECT FreeDocType.Description from FreeAgExDoc "
            + "inner join FreeDocType on FreeAgExDoc.DocType = FreeDocType.DocType "
            + "where FreeAgExDoc.CLIENT = ?1 and FreeAgExDoc.DATECREATE = ?2 and FreeAgExDoc.TIMECREATE = ?3"),
})
@Cacheable(false)
public class FreeAgExDoc implements Serializable {
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
