package com.bssys.entity;

import com.bssys.bls.types.DboDocPK;
import com.bssys.conversion.Money;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@NamedNativeQueries({
    @NamedNativeQuery(name = "UmtPayTransferClose.getDocumentNumberByPK",
        query = "SELECT DocumentNumber FROM UmtPayTransferClose "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "UmtPayTransferClose.getStatusByPK",
        query = "SELECT CAST (Status AS DECIMAL) AS Status FROM UmtPayTransferClose "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
})
@Cacheable(false)
public class UmtPayTransferClose implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  private DboDocPK dboDocPK;
  @Size(max = 50)
  private String senderlastname;
  @Size(max = 50)
  private String senderfirstname;
  @Size(max = 50)
  private String receiverlastname;
  @Size(max = 50)
  private String receiverfirstname;
  @Size(max = 3)
  private String currcodeiso;
  private Double amount;
  private Integer status;

  public UmtPayTransferClose() {
  }

  public UmtPayTransferClose(final DboDocPK pk) {
    this.dboDocPK = pk;
  }

  @Override
  public int hashCode() {
    return (dboDocPK != null ? dboDocPK.hashCode() : 0);
  }

  @Override
  public boolean equals(final Object object) {
    if (!(object instanceof UmtPayTransferClose)) {
      return false;
    }
    UmtPayTransferClose other = (UmtPayTransferClose) object;
    return (this.dboDocPK != null || other.dboDocPK == null)
        && (this.dboDocPK == null || this.dboDocPK.equals(other.dboDocPK));
  }

  @Override
  public String toString() {
    return "com.bssys.paytransferclose.UmtPayTransferClose[ dboDocPK=" + dboDocPK + " ]";
  }

  public String getSenderLastname() {
    return senderlastname;
  }

  public String getSenderFirstname() {
    return senderfirstname;
  }

  public String getReceiverLastname() {
    return receiverlastname;
  }

  public String getReceiverFirstname() {
    return receiverfirstname;
  }

  public String getAmountWithCurrency() {
    return new Money(amount, currcodeiso).toString();
  }

  public Integer getStatus() {
    return status;
  }

  public DboDocPK getDboDocPK() {
    return dboDocPK;
  }
}
