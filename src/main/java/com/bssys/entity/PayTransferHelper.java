package com.bssys.entity;

import com.bssys.bls.types.DboDocPK;
import com.bssys.conversion.Money;

import java.io.Serializable;
import java.util.Date;


public class PayTransferHelper implements Serializable {

  public static final int DOC_TYPE_PAY_TRANSFER_OPEN = 1;
  public static final int DOC_TYPE_PAY_LEGAL = 2;


  private UmtDocType docType;
  private Integer client;
  private Integer dateCreate;
  private Integer timeCreate;
  private Integer status;
  private String checkNumber;
  private String documentNumber;
  private Date documentDate;
  private Date docLastModificationDate;
  private Double amount;
  private String currCodeISO;
  private Date valueDateTime;

  public PayTransferHelper() {
  }

  public PayTransferHelper(Integer docType, Integer status, String checkNumber, Date documentDate,
                           String documentNumber, Date valueDateTime, Integer client, Integer dateCreate,
                           Integer timeCreate, Double amount, String currCodeISO) {
    this.docType = UmtDocType.byId(docType);
    this.status = status;
    this.checkNumber = checkNumber;
    this.documentDate = documentDate;
    this.documentNumber = documentNumber;
    this.valueDateTime = valueDateTime;
    this.client = client;
    this.dateCreate = dateCreate;
    this.timeCreate = timeCreate;
    this.amount = amount;
    this.currCodeISO = currCodeISO;
  }

  public String getStatus() {
    return status.toString();
  }

  public String getCheckNumber() {
    return checkNumber;
  }


  public Date getDocumentDate() {
    return documentDate;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public UmtDocType getDocType() {
    return docType;
  }

  public DboDocPK getDocPK() {
    return new DboDocPK(this.client, this.dateCreate, this.timeCreate);
  }

  public String getAmountWithCurrency() {
    return new Money(amount, currCodeISO).toString();
  }

  public Date getValueDateTime() {
    return valueDateTime;
  }
}
