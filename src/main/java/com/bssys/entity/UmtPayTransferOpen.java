/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.bssys.entity;

import com.bssys.bls.types.DboDocPK;
import com.bssys.conversion.Money;
import com.bssys.umt.Statuses;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import static com.bssys.entity.PayTransferHelper.DOC_TYPE_PAY_LEGAL;
import static com.bssys.entity.PayTransferHelper.DOC_TYPE_PAY_TRANSFER_OPEN;

/**
 * @author sozvv
 */
@Entity
@NamedNativeQueries({
    @NamedNativeQuery(name = "Umtpaytransferopen.getDocumentNumberByPK",
        query = "SELECT DocumentNumber FROM Umtpaytransferopen "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),
    @NamedNativeQuery(name = "Umtpaytransferopen.getStatusByPK",
        query = "SELECT CAST (Status AS DECIMAL) AS Status FROM Umtpaytransferopen "
            + "where CLIENT = ?1 and DATECREATE = ?2 and TIMECREATE = ?3"),

    @NamedNativeQuery(name = "Umtpaytransferopen.getLastSendTransfers",
        query = "SELECT " + DOC_TYPE_PAY_TRANSFER_OPEN + " as docType, doc.status, doc.checkNumber, " +
            "doc.documentDate, doc.documentNumber, "
            + "   CASE "
            + "      WHEN doc.userpayer = ?1 "
            + "      THEN doc.statuslastchangetime "
            + "      ELSE doc.valuedatetime "
            + "   END as valueDateTime, "
            + " doc.client, doc.dateCreate, doc.timeCreate, chargeAmount as amount, chargeCurrCodeISO as currCodeISO "
            + "  FROM UmtPayTransferOpen doc "
            + " where ((doc.usersender = ?1) or (doc.userpayer = ?1)) and "
            + " doc.status not in ("
            + Statuses.STS_BSI_FOO + ", " + Statuses.STS_TEMPLATE + ", " + Statuses.STS_NEW + ", "
            + Statuses.STS_FORSEND + ", " + Statuses.STS_BSI_NEW + ", " + Statuses.STS_UMT_TEMPLATE + ", "
            + Statuses.STS_READY_TOSEND + ", " + Statuses.STS_BSI_DELETED + ") "
            + " union all "
            + " SELECT " + DOC_TYPE_PAY_LEGAL + " as docType, lg.status, lg.checkNumber, lg.statusLastChangeTime, " +
            "lg.documentNumber, "
            + " documentDate as valueDateTime, "
            + " lg.client, lg.dateCreate, lg.timeCreate, amount, currCodeISO "
            + " FROM UmtPayLegal lg "
            + " where (lg.usersender = ?1) and "
            + " lg.status not in ("
            + Statuses.STS_BSI_FOO + ", " + Statuses.STS_TEMPLATE + ", " + Statuses.STS_NEW + ", "
            + Statuses.STS_FORSEND + ", " + Statuses.STS_BSI_NEW + ", " + Statuses.STS_UMT_TEMPLATE + ", "
            + Statuses.STS_READY_TOSEND + ", " + Statuses.STS_BSI_DELETED + ") "
            + " ORDER BY "
            + "   6 DESC", resultSetMapping = "unionDocs")
})
@SqlResultSetMapping(
    name = "unionDocs",
    classes = {
        @ConstructorResult(
            targetClass = PayTransferHelper.class,
            columns = {
                @ColumnResult(name = "docType", type = Integer.class),
                @ColumnResult(name = "status", type = Integer.class),
                @ColumnResult(name = "checkNumber", type = String.class),
                @ColumnResult(name = "documentDate", type = Date.class),
                @ColumnResult(name = "documentNumber", type = String.class),
                @ColumnResult(name = "valueDateTime", type = Date.class),
                @ColumnResult(name = "client", type = Integer.class),
                @ColumnResult(name = "dateCreate", type = Integer.class),
                @ColumnResult(name = "timeCreate", type = Integer.class),
                @ColumnResult(name = "amount", type = Double.class),
                @ColumnResult(name = "currCodeISO", type = String.class)
            })
    })
@Cacheable(false)
public class UmtPayTransferOpen implements Serializable {
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("UnusedDeclaration")
  @EmbeddedId
  private DboDocPK dboDocPK;
  private int custid;
  private Double amount;
  @SuppressWarnings("UnusedDeclaration")
  @Size(max = 25)
  private String checkNumber;
  @SuppressWarnings("UnusedDeclaration")
  @Size(max = 3)
  private String currcodeiso;
  @SuppressWarnings("UnusedDeclaration")
  @Size(max = 50)
  private String receiverlastname;
  @SuppressWarnings("UnusedDeclaration")
  @Size(max = 50)
  private String receiverfirstname;
  @SuppressWarnings("UnusedDeclaration")
  @Size(max = 50)
  private String senderlastname;
  @SuppressWarnings("UnusedDeclaration")
  @Size(max = 50)
  private String senderfirstname;
  @SuppressWarnings("UnusedDeclaration")
  private int destclient;
  private int status;
  @SuppressWarnings("UnusedDeclaration")
  @Temporal(TemporalType.TIMESTAMP)
  private Date statuslastchangetime;
  @SuppressWarnings("UnusedDeclaration")
  @Temporal(TemporalType.TIMESTAMP)
  private Date valuedatetime;
  private int sendertransfersys;
  private int receivertransfersys;
  @SuppressWarnings("UnusedDeclaration")
  private int usersender;
  @SuppressWarnings("UnusedDeclaration")
  private int userpayer;

  @Column(name = "DOCUMENTNUMBER")
  @Size(max = 25)
  String documentNumber;

  @Override
  public int hashCode() {
    return (dboDocPK != null ? dboDocPK.hashCode() : 0);
  }

  @Override
  public boolean equals(final Object object) {
    if (!(object instanceof UmtPayTransferOpen)) {
      return false;
    }
    UmtPayTransferOpen other = (UmtPayTransferOpen) object;
    return (this.dboDocPK != null || other.dboDocPK == null)
        && (this.dboDocPK == null || this.dboDocPK.equals(other.dboDocPK));
  }

  @Override
  public String toString() {
    return "com.bssys.entity.UmtPayTransferOpen[ dboDocPK=" + dboDocPK + " ]";
  }

  public int getSendertransfersys() {
    return sendertransfersys;
  }

  public void setSendertransfersys(final int transfersys) {
    this.sendertransfersys = transfersys;
  }

  public int getReceivertransfersys() {
    return receivertransfersys;
  }

  public void setReceivertransfersys(final int transfersys) {
    this.receivertransfersys = transfersys;
  }

  public String getSenderlastname() {
    return senderlastname;
  }

  public String getSenderfirstname() {
    return senderfirstname;
  }

  public String getReceiverlastname() {
    return receiverlastname;
  }

  public String getReceiverfirstname() {
    return receiverfirstname;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(final Double newAmount) {
    this.amount = newAmount;
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public DboDocPK getDboDocPK() {
    return dboDocPK;
  }

  public int getStatus() {
    return status;
  }

  public String getAmountWithCurrency() {
    return new Money(amount, currcodeiso).toString();
  }

  public Date getValuedatetime() {
    return valuedatetime;
  }

  public int getCustid() {
    return custid;
  }

  public int getDestclient() {
    return destclient;
  }

  public int getUserpayer() {
    return userpayer;
  }

  public Date getStatuslastchangetime() {
    return statuslastchangetime;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public String getCurrcodeiso() {
    return currcodeiso;
  }

  public void setCurrcodeiso(String currcodeiso) {
    this.currcodeiso = currcodeiso;
  }
}
